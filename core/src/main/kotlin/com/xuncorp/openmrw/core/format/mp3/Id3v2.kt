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

package com.xuncorp.openmrw.core.format.mp3

import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.decodeToString
import kotlinx.io.readByteString
import kotlinx.io.readString
import kotlinx.io.readUInt

/**
 * # ID3v2 Header
 *
 * Should be the first information in the file. 10 bytes.
 *
 * [ID3 tag version 2.3.0](https://id3.org/id3v2.3.0), informal standard.
 */
internal class Id3v2Header(source: Source) {
    /**
     * 'I', 'D', '3'
     */
    val identifier = source.readByteString(3)

    /**
     * 3
     */
    val majorVersion = source.readByte().toInt() and 0xFF

    /**
     * 0
     */
    val revision = source.readByte().toInt() and 0xFF

    /**
     * %abc00000
     * - a: [unSynchronisation]
     * - b: [extendedHeader]
     * - c: [experimentalIndicator]
     */
    val flags = source.readByte()

    /**
     * The ID3v2tag size (bytes) is the size of the complete tag after un synchronisation,
     * including padding, excluding the header but not excluding the extended header
     * (total tag size - 10).
     *
     * Only 28 bits (representing up to 256MB) are used in the size description to avoid the
     * introduction of 'false sync signals'.
     */
    val size = (source.readByte().toInt() and 0x7F shl 21) or
            (source.readByte().toInt() and 0x7F shl 14) or
            (source.readByte().toInt() and 0x7F shl 7) or
            (source.readByte().toInt() and 0x7F)

    /**
     * Bit 7 in the 'ID3v2 flags' indicates whether or not un synchronisation is used
     * (see section 5 for details); a set bit indicates usage.
     */
    val unSynchronisation = flags.toInt() and 0x80 != 0

    /**
     * The second bit (bit 6) indicates whether or not the header is followed by an extended header.
     */
    val extendedHeader = flags.toInt() and 0x40 != 0

    /**
     * The third bit (bit 5) should be used as an 'experimental indicator'. This flag should always
     * be set when the tag is in an experimental stage.
     */
    val experimentalIndicator = flags.toInt() and 0x20 != 0

    init {
        require(identifier == ByteString(0x49, 0x44, 0x33))
    }

    override fun toString(): String {
        return "Id3v2Header(identifier=$identifier, majorVersion=$majorVersion, " +
                "revision=$revision, flags=$flags, size=$size)"
    }
}

/**
 * - Extended Header Size: $xx xx xx xx
 * - Extended Flags: $xx xx
 * - Size of Padding: $xx xx xx xx
 * - Total Frame CRC: $xx xx xx xx (Optional)
 */
internal class Id3v2ExtendedHeader(source: Source) {
    /**
     * Where the 'Extended header size', currently 6 or 10 bytes, excludes itself.
     */
    val extendedHeaderSize = source.readUInt()

    val extendedFlags = source.readByteString(2)

    val sizeOfPadding = source.readUInt()

    /**
     * If this flag is set four bytes of CRC-32 data is appended to the extended header.
     */
    val crcDataPresent = extendedFlags[0].toInt() and 0x80 != 0

    val totalFrameCrc: ByteString =
        if (crcDataPresent) {
            source.readByteString(4)
        } else {
            ByteString()
        }
}

/**
 * As the tag consists of a tag header and a tag body with one or more frames, all the frames
 * consists of a frame header followed by one or more fields containing the actual information.
 */
internal class Id3v2FrameHeader(source: Source) {
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
    val frameSize = source.readUInt()

    /**
     * %abc00000 %ijk00000
     * - a: [tagAlterPreservation]
     * - b: [fileAlterPreservation]
     */
    val flags = source.readByteString(2)

    /**
     * This flag tells the software what to do with this frame if it is unknown and the tag is
     * altered in any way. This applies to all kinds of alterations, including adding more padding
     * and reordering the frames.
     *
     * - true: Frame should be discarded.
     * - false: Frame should be preserved.
     */
    val tagAlterPreservation = flags[0].toInt() and 0x80 != 0

    /**
     * This flag tells the software what to do with this frame if it is unknown and the file,
     * excluding the tag, is altered. This does not apply when the audio is completely replaced with
     * other audio data.
     *
     * - true: Frame should be discarded.
     * - false: Frame should be preserved.
     */
    val fileAlterPreservation = flags[0].toInt() and 0x40 != 0

    /**
     * This flag, if set, tells the software that the contents of this frame is intended to be read
     * only. Changing the contents might break something, e.g. a signature. If the contents are
     * changed, without knowledge in why the frame was flagged read only and without taking the
     * proper means to compensate, e.g. recalculating the signature, the bit should be cleared.
     */
    val readOnly = flags[0].toInt() and 0x20 != 0

    /**
     * This flag indicates whether or not the frame is compressed.
     *
     * - true: Frame is compressed using ZLib with 4 bytes for 'decompressed size' appended to the
     *   frame header.
     * - false: Frame is not compressed.
     */
    val compression = flags[1].toInt() and 0x80 != 0

    /**
     * This flag indicates whether or not the frame is encrypted. If set one byte indicating with
     * which method it was encrypted will be appended to the frame header.
     *
     * TODO https://id3.org/id3v2.3.0#sec4.26
     */
    val encryption = flags[1].toInt() and 0x40 != 0

    /**
     * This flag indicates whether or not this frame belongs in a group with other frames. If set a
     * group identifier byte is added to the frame header. Every frame with the same group
     * identifier belongs to the same group.
     *
     * - true: Frame contains group information.
     * - false: Frame does not contain group information.
     */
    val groupingIdentity = flags[1].toInt() and 0x20 != 0

    val isPaddingFrame = frameId == ByteString(0x00, 0x00, 0x00, 0x00)

    val frameType: FrameType
        = when (frameId[0]) {
            'T'.code.toByte() -> FrameType.TextInformation
            else -> FrameType.Unknown
        }

    /**
     * Get the text information from the frame if [frameType] is [FrameType.TextInformation].
     */
    fun getTextInformation(source: Source): String {
        require(frameType == FrameType.TextInformation)
        val textEncoding = source.readByte()
        val charset = when (textEncoding.toInt()) {
            0x00 -> Charsets.ISO_8859_1
            0x01 -> Charsets.UTF_16
            else -> throw IllegalArgumentException("Invalid text encoding: $textEncoding")
        }
        return source.readString(frameSize.toLong() - 1L, charset)
    }

    enum class FrameType {
        TextInformation,

        /**
         * TODO OpenMrw does not support this frame type yet.
         */
        Unknown
    }
}