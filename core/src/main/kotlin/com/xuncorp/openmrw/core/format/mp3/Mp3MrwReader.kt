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

package com.xuncorp.openmrw.core.format.mp3

import com.xuncorp.openmrw.core.format.MrwFormat
import com.xuncorp.openmrw.core.rw.MrwReader
import com.xuncorp.openmrw.core.rw.ReaderProperties
import com.xuncorp.openmrw.core.rw.id3v2.Id3v2ExtendedHeader
import com.xuncorp.openmrw.core.rw.id3v2.Id3v2FrameHeader
import com.xuncorp.openmrw.core.rw.id3v2.Id3v2Header
import kotlinx.io.Source
import kotlinx.io.bytestring.decodeToString

internal class Mp3MrwReader : MrwReader() {
    override fun match(source: Source) {
        Id3v2Header(source)
    }

    override fun fetch(source: Source, properties: ReaderProperties): MrwFormat {
        val mp3MrwFormat = Mp3MrwFormat()

        val id3v2Header = Id3v2Header(source)
        val id3v2ExtendedHeaderSize = if (id3v2Header.extendedHeader()) {
            Id3v2ExtendedHeader(source).extendedHeaderSize.toInt() + 4
        } else {
            0
        }

        val totalId3v2FrameSize = id3v2Header.size - id3v2ExtendedHeaderSize
        var readFrameSize = 0
        while (readFrameSize < totalId3v2FrameSize) {
            val id3V2FrameHeader = Id3v2FrameHeader(
                source = source,
                id3v2Version = id3v2Header.version,
                id3v2Charset = properties.id3v2Charset
            )

            if (id3V2FrameHeader.isPaddingFrame()) {
                break
            }

            val frameSize = id3V2FrameHeader.frameSize

            when (id3V2FrameHeader.frameType) {
                Id3v2FrameHeader.FrameType.TextInformation -> {
                    val textInformation = id3V2FrameHeader.getTextInformation(source)
                    mp3MrwFormat.mrwComment.add(
                        field = id3V2FrameHeader.frameId.decodeToString(),
                        value = textInformation
                    )
                }

                Id3v2FrameHeader.FrameType.UnsynchronizedLyrics -> {
                    val unsynchronizedLyrics = id3V2FrameHeader.getUnsynchronizedLyrics(source)
                    mp3MrwFormat.mrwComment.add(
                        field = id3V2FrameHeader.frameId.decodeToString(),
                        value = unsynchronizedLyrics
                    )
                }

                Id3v2FrameHeader.FrameType.Comment -> {
                    val comment = id3V2FrameHeader.getComment(source)
                    mp3MrwFormat.mrwComment.add(
                        field = id3V2FrameHeader.frameId.decodeToString(),
                        value = comment
                    )
                }

                else -> {
                    source.skip(frameSize.toLong())
                }
            }

            readFrameSize += frameSize + 10
        }

        return mp3MrwFormat
    }
}