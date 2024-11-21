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
import kotlinx.io.readByteString
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

    val flags = source.readByte()

    /**
     * The ID3v2 tag size is the size of the complete tag after unsychronisation, including padding,
     * excluding the header but not excluding the extended header (total tag size - 10). Only 28
     * bits (representing up to 256MB) are used in the size description to avoid the introduction
     * of 'false syncsignals'.
     */
    val size = source.readUInt()

    init {
        require(identifier == ByteString(0x49, 0x44, 0x33))
    }
}