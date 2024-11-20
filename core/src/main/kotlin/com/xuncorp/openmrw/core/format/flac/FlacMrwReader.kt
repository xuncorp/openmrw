/**
 * OpenMrw
 * Copyright (C) 2024 Xuncorp
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */

package com.xuncorp.openmrw.core.format.flac

import com.xuncorp.openmrw.core.format.MrwFormat
import com.xuncorp.openmrw.core.rw.MrwReader
import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString

internal class FlacMrwReader : MrwReader() {
    private fun handleStreamInfo(
        source: Source,
        flacHeader: FlacHeader,
        flacMrwFormat: FlacMrwFormat
    ) {
        val flacStreamInfo = FlacStreamInfo(source.readByteString(flacHeader.length))

        flacMrwFormat.mrwStreamInfo.apply {
            sampleRate = flacStreamInfo.sampleRate
            channelCount = flacStreamInfo.channelCount
            bits = flacStreamInfo.bits
            sampleCount = flacStreamInfo.sampleCount
        }
    }

    private fun handleVorbisComment(
        source: Source,
        flacMrwFormat: FlacMrwFormat
    ) {
        val flacVorbisComment = FlacVorbisComment(source)

        flacVorbisComment.userComments.forEach { userComment ->
            val parts = userComment.split('=', limit = 2)
            if (parts.size == 2) {
                val field = parts[0].trim()
                val value = parts[1].trim()
                flacMrwFormat.mrwComment.add(field, value)
            }
        }
    }

    override fun match(source: Source): Boolean {
        val peek = source.peek()
        val magicHeader = peek.readByteString(4)
        peek.close()
        return magicHeader == MAGIC_HEADER
    }

    override fun fetch(source: Source): MrwFormat {
        val peek = source.peek()
        val flacMrwFormat = FlacMrwFormat()

        // Skip magic header
        peek.skip(MAGIC_HEADER.size.toLong())

        var flacHeader: FlacHeader
        do {
            flacHeader = FlacHeader(peek.readByteString(4))

            when (flacHeader.blockType) {
                FlacHeader.BLOCK_TYPE_STREAMINFO -> handleStreamInfo(
                    peek,
                    flacHeader,
                    flacMrwFormat
                )

                FlacHeader.BLOCK_TYPE_VORBIS_COMMENT -> handleVorbisComment(peek, flacMrwFormat)

                else -> {
                    peek.skip(flacHeader.length.toLong())
                }
            }
        } while (!flacHeader.isLastMetadataBlock)

        peek.close()
        return flacMrwFormat
    }

    companion object {
        /**
         * "fLaC"
         */
        private val MAGIC_HEADER = ByteString(0x66, 0x4C, 0x61, 0x43)
    }
}