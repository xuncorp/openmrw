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
import com.xuncorp.openmrw.core.format.MrwFormatType
import com.xuncorp.openmrw.core.rw.MrwReader
import com.xuncorp.openmrw.core.rw.ReaderProperties
import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString

internal class FlacMrwReader : MrwReader() {
    private fun readStreamInfo(
        source: Source,
        flacHeader: FlacHeader,
        mrwFormat: MrwFormat
    ) {
        val flacStreamInfo = FlacStreamInfo(source.readByteString(flacHeader.length))

        mrwFormat.mrwStreamInfo.apply {
            sampleRate = flacStreamInfo.sampleRate
            channelCount = flacStreamInfo.channelCount
            bits = flacStreamInfo.bits
            sampleCount = flacStreamInfo.sampleCount
        }
    }

    private fun readVorbisComment(
        source: Source,
        mrwFormat: MrwFormat
    ) {
        val flacVorbisComment = FlacVorbisComment(source)

        flacVorbisComment.userComments.forEach { userComment ->
            val parts = userComment.split('=', limit = 2)
            if (parts.size == 2) {
                val field = parts[0].trim()
                val value = parts[1].trim()
                mrwFormat.mrwComment.add(field, value)
            }
        }
    }

    override fun match(source: Source) {
        val magicHeader = source.readByteString(4)
        require(magicHeader == MAGIC_HEADER)
    }

    override fun fetch(source: Source, properties: ReaderProperties): MrwFormat {
        val mrwFormat = MrwFormat(MrwFormatType.Flac)

        // Skip magic header.
        source.skip(MAGIC_HEADER.size.toLong())

        var flacHeader: FlacHeader
        do {
            flacHeader = FlacHeader(source.readByteString(4))

            when (flacHeader.blockType) {
                FlacHeader.BLOCK_TYPE_STREAMINFO -> readStreamInfo(
                    source,
                    flacHeader,
                    mrwFormat
                )

                FlacHeader.BLOCK_TYPE_VORBIS_COMMENT -> readVorbisComment(source, mrwFormat)

                else -> {
                    source.skip(flacHeader.length.toLong())
                }
            }
        } while (!flacHeader.isLastMetadataBlock)

        return mrwFormat
    }

    companion object {
        /**
         * "fLaC".
         */
        private val MAGIC_HEADER = ByteString(0x66, 0x4C, 0x61, 0x43)
    }
}