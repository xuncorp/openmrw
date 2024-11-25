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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.xuncorp.openmrw.core.rw.id3v2

import com.xuncorp.openmrw.core.rw.ReaderProperties
import com.xuncorp.openmrw.core.util.first
import com.xuncorp.openmrw.core.util.last
import com.xuncorp.openmrw.core.util.lastIndex
import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.decodeToString
import kotlinx.io.readByteString
import java.nio.charset.Charset

/**
 * As the tag consists of a tag header and a tag body with one or more frames, all the frames
 * consists of a frame header followed by one or more fields containing the actual information.
 *
 * @param id3v2Charset See [ReaderProperties.id3v2Charset] default value [Charsets.ISO_8859_1].
 */
@Suppress("SpellCheckingInspection")
internal class Id3v2FrameHeader(
    source: Source,
    private val id3v2Version: Int,
    private val id3v2Charset: Charset
) {
    /**
     * The frame ID made out of the characters capital A-Z and 0-9. Identifiers beginning with "X",
     * "Y" and "Z" are for experimental use and free for everyone to use, without the need to set
     * the experimental bit in the tag header. Have in mind that someone else might have used the
     * same identifier as you. All other identifiers are either used or reserved for future use.
     */
    val frameId = source.readByteString(4)

    /**
     * The size is calculated as frame size excluding frame header (frame size - 10).
     */
    val frameSize = when (id3v2Version) {
        3 -> source.readInt()
        4 -> (source.readByte().toInt() and 0x7F shl 21) or
                (source.readByte().toInt() and 0x7F shl 14) or
                (source.readByte().toInt() and 0x7F shl 7) or
                (source.readByte().toInt() and 0x7F)

        else -> error("Invalid ID3v2 version: $id3v2Version.")
    }

    /**
     * ID3v2.3.0 %abc00000 %ijk00000.
     *
     * - a: [tagAlterPreservation]
     * - b: [fileAlterPreservation]
     * - c: [readOnly]
     * - i: [compression]
     * - j: [encryption]
     * - k: [groupingIdentity]
     *
     * ID3v2.4.0 %0abc0000 %0h00kmnp.
     *
     * - a: [tagAlterPreservation]
     * - b: [fileAlterPreservation]
     * - c: [readOnly]
     * - h: [groupingIdentity]
     * - k: [compression]
     * - m: [encryption]
     * - n: [unsynchronization]
     * - p: [dataLengthIndicator]
     */
    val flags = source.readByteString(2)

    val frameType: FrameType

    private fun getCharset(textEncoding: Byte): Charset {
        return when (textEncoding.toInt()) {
            // Terminated with 0x00.
            0x00 -> id3v2Charset
            // Unicode strings must begin with the Unicode BOM ($FF FE or $FE FF) to identify the
            // byte order. Terminated with 0x00 0x00.
            0x01 -> Charsets.UTF_16
            // Id3v2.4.0. Terminated with 0x00 0x00.
            0x02 -> Charsets.UTF_16BE
            // Id3v2.4.0. Terminated with 0x00.
            0x03 -> Charsets.UTF_8
            else -> error("Invalid text encoding: $textEncoding.")
        }
    }

    /**
     * The [byteString] does not contain header data such as text encoding, but contains a
     * terminated string.
     */
    private fun geActualText(byteString: ByteString, charset: Charset): String {
        val synchronizedByteString = if (unsynchronization()) {
            synchronizeByteString(byteString)
        } else {
            byteString
        }

        // In some files, the fields in tags do not end with 0x00 or 0x00 0x00, which differs from
        // the documentation. I suspect this might be due to issues with certain tag writing
        // programs.
        //
        // To address this, OpenMrw has added additional checks.
        val textByteString = when (charset) {
            id3v2Charset, Charsets.UTF_8 -> {
                if (synchronizedByteString.last().toInt() == 0x00) {
                    synchronizedByteString.substring(0, synchronizedByteString.size - 1)
                } else {
                    synchronizedByteString
                }
            }

            Charsets.UTF_16 -> {
                // Ignore Unicode NULL [0xFF FE 00 00] [0xFE FF 00 00].
                val startIndex = when {
                    synchronizedByteString.size <= 4 -> 0
                    synchronizedByteString[0].toInt() and 0xFF == 0xFF &&
                            synchronizedByteString[1].toInt() and 0xFF == 0xFE &&
                            synchronizedByteString[2].toInt() and 0xFF == 0x00 &&
                            synchronizedByteString[3].toInt() and 0xFF == 0x00 -> 4

                    synchronizedByteString[0].toInt() and 0xFF == 0xFE &&
                            synchronizedByteString[1].toInt() and 0xFF == 0xFF &&
                            synchronizedByteString[2].toInt() and 0xFF == 0x00 &&
                            synchronizedByteString[3].toInt() and 0xFF == 0x00 -> 4

                    else -> 0
                }

                if (synchronizedByteString.last().toInt() and 0xFF == 0x00 &&
                    synchronizedByteString[synchronizedByteString.lastIndex - 1].toInt() and 0xFF == 0x00
                ) {
                    synchronizedByteString.substring(startIndex, synchronizedByteString.size - 2)
                } else {
                    synchronizedByteString.substring(startIndex)
                }
            }

            Charsets.UTF_16BE -> {
                if (synchronizedByteString.last().toInt() and 0xFF == 0x00 &&
                    synchronizedByteString[synchronizedByteString.lastIndex - 1].toInt() and 0xFF == 0x00
                ) {
                    synchronizedByteString.substring(0, synchronizedByteString.size - 2)
                } else {
                    synchronizedByteString
                }
            }

            else -> error("Invalid charset: $charset.")
        }

        return textByteString.decodeToString(charset)
    }

    /**
     * This flag tells the software what to do with this frame if it is unknown and the tag is
     * altered in any way. This applies to all kinds of alterations, including adding more padding
     * and reordering the frames.
     *
     * - true: Frame should be discarded.
     * - false: Frame should be preserved.
     */
    fun tagAlterPreservation() = flags[0].toInt() and 0x80 != 0

    /**
     * This flag tells the software what to do with this frame if it is unknown and the file,
     * excluding the tag, is altered. This does not apply when the audio is completely replaced with
     * other audio data.
     *
     * - true: Frame should be discarded.
     * - false: Frame should be preserved.
     */
    fun fileAlterPreservation() = flags[0].toInt() and 0x40 != 0

    /**
     * This flag, if set, tells the software that the contents of this frame is intended to be read
     * only. Changing the contents might break something, e.g. a signature. If the contents are
     * changed, without knowledge in why the frame was flagged read only and without taking the
     * proper means to compensate, e.g. recalculating the signature, the bit should be cleared.
     */
    fun readOnly() = flags[0].toInt() and 0x20 != 0

    /**
     * This flag indicates whether or not the frame is compressed.
     *
     * - true: Frame is compressed using ZLib with 4 bytes for 'decompressed size' appended to the
     *   frame header.
     * - false: Frame is not compressed.
     */
    fun compression() = when (id3v2Version) {
        3 -> flags[1].toInt() and 0x80 != 0
        4 -> flags[1].toInt() and 0x08 != 0
        else -> error("Invalid ID3v2 version: $id3v2Version.")
    }

    /**
     * This flag indicates whether or not the frame is encrypted. If set one byte indicating with
     * which method it was encrypted will be appended to the frame header.
     *
     * TODO https://id3.org/id3v2.3.0#sec4.26
     */
    fun encryption() = when (id3v2Version) {
        3 -> flags[1].toInt() and 0x40 != 0
        4 -> flags[1].toInt() and 0x04 != 0
        else -> error("Invalid ID3v2 version: $id3v2Version.")
    }

    /**
     * This flag indicates whether or not this frame belongs in a group with other frames. If set a
     * group identifier byte is added to the frame header. Every frame with the same group
     * identifier belongs to the same group.
     *
     * - true: Frame contains group information.
     * - false: Frame does not contain group information.
     */
    fun groupingIdentity() = when (id3v2Version) {
        3 -> flags[1].toInt() and 0x20 != 0
        4 -> flags[1].toInt() and 0x40 != 0
        else -> error("Invalid ID3v2 version: $id3v2Version.")
    }

    /**
     * - true: Frame has been unsyrchronized.
     * - false: Frame has not been unsynchronized.
     */
    fun unsynchronization() = id3v2Version == 4 && flags[1].toInt() and 0x02 != 0

    fun dataLengthIndicator() = id3v2Version == 4 && flags[1].toInt() and 0x01 != 0

    fun isPaddingFrame() = frameId == ByteString(0x00, 0x00, 0x00, 0x00)

    /**
     * Get the text information from the frame if [frameType] is [FrameType.TextInformation].
     */
    fun getTextInformation(source: Source): String {
        require(frameType == FrameType.TextInformation)
        // TODO: Use data length indicator.
        val byteCount = if (dataLengthIndicator()) {
            source.skip(4L)
            frameSize - 5
        } else {
            frameSize - 1
        }
        val textEncoding = source.readByte()
        val charset = getCharset(textEncoding)

        val byteString = source.readByteString(byteCount)
        return geActualText(byteString, charset)
    }

    fun getComment(source: Source): String {
        require(frameType == FrameType.Comment)
        val textEncoding = source.readByte()
        val charset = getCharset(textEncoding)
        // Language. 'XXX'.
        source.readByteString(3)

        var byteString = source.readByteString(frameSize - 4)
        // Content descriptor.
        if (byteString.first().toInt() and 0xFF == 0x00) {
            byteString = byteString.substring(1)
        }

        return geActualText(byteString, charset)
    }

    fun getUnsynchronizedLyrics(source: Source): String {
        require(frameType == FrameType.UnsynchronizedLyrics)
        val textEncoding = source.readByte()
        val charset = getCharset(textEncoding)
        // Language. 'XXX'.
        source.readByteString(3)

        var byteString = source.readByteString(frameSize - 4)
        // Content descriptor.
        if (byteString.first().toInt() and 0xFF == 0x00) {
            byteString = byteString.substring(1)
        }

        return geActualText(byteString, charset)
    }

    init {
        val frameIdString = frameId.decodeToString()
        frameType = when {
            frameId[0] == 'T'.code.toByte() -> FrameType.TextInformation
            frameIdString == Id3v2DeclaredFrames.USLT -> FrameType.UnsynchronizedLyrics
            frameIdString == Id3v2DeclaredFrames.COMM -> FrameType.Comment
            else -> FrameType.Unknown
        }
    }

    enum class FrameType {
        TextInformation,

        /**
         * ID3v2.3.0 title 4.9.
         *
         * @see Id3v2DeclaredFrames.USLT
         */
        UnsynchronizedLyrics,

        /**
         * ID3v2.3.0 title 4.11.
         *
         * @see Id3v2DeclaredFrames.COMM
         */
        Comment,

        /**
         * TODO: OpenMrw does not support this frame type yet.
         */
        Unknown
    }

    companion object {
        /**
         * 0xFF 00 -> 0xFF.
         */
        fun synchronizeByteString(byteString: ByteString): ByteString {
            val byteArray = byteString.toByteArray()
            val newByteArray = mutableListOf<Byte>()

            var i = 0
            while (i < byteArray.size) {
                val b1 = byteArray[i]
                val b2 = byteArray.getOrNull(i + 1)

                newByteArray.add(b1)

                if (b1.toInt() and 0xFF == 0xFF && b2 != null && b2.toInt() and 0xFF == 0x00) {
                    i++
                }
                i++
            }

            return ByteString(newByteArray.toByteArray())
        }
    }
}