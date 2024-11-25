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

import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.readUInt

/**
 * # ID3v2 Header
 *
 * Should be the first information in the file. 10 bytes.
 *
 * - [ID3 tag version 2.3.0](https://id3.org/id3v2.3.0), informal standard.
 * - [ID3 tag version 2.4.0](https://id3.org/id3v2.4.0-structure), informal standard.
 */
internal class Id3v2Header(source: Source) {
    /**
     * 'I', 'D', '3', 3 bytes.
     */
    val identifier = source.readByteString(3)

    /**
     * 1 byte.
     *
     * - 3: ID3v2.3.0
     * - 4: ID3v2.4.0
     */
    val version = source.readByte().toInt() and 0xFF

    /**
     * 1 byte.
     */
    val revision = source.readByte().toInt() and 0xFF

    /**
     * 1 byte.
     *
     * %abcd0000
     * - a: [unsynchronization]
     * - b: [extendedHeader]
     * - c: [experimentalIndicator]
     * - d: [footerPresent]
     */
    val flags = source.readByte()

    /**
     * 4 bytes.
     *
     * The ID3v2tag size (bytes) is the size of the complete tag after un synchronization, including
     * padding, excluding the header but not excluding the extended header
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
     * Bit 7 in the 'ID3v2 flags' indicates whether or not unsynchronization is used
     * (see section 5 for details); a set bit indicates usage.
     */
    fun unsynchronization() = flags.toInt() and 0x80 != 0

    /**
     * The second bit (bit 6) indicates whether or not the header is followed by an extended header.
     */
    fun extendedHeader() = flags.toInt() and 0x40 != 0

    /**
     * The third bit (bit 5) should be used as an 'experimental indicator'. This flag should always
     * be set when the tag is in an experimental stage.
     */
    fun experimentalIndicator() = flags.toInt() and 0x20 != 0

    /**
     * ID3v2.4.0 only.
     */
    fun footerPresent() = version == 4 && flags.toInt() and 0x10 != 0

    init {
        require(identifier == ByteString(0x49, 0x44, 0x33))
    }

    override fun toString(): String {
        return "Id3v2Header(identifier=$identifier, majorVersion=$version, " +
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