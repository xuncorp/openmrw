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

package com.xuncorp.openmrw.core.format.ncm

import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString

internal class NcmHeader(source: Source) {
    private val header = source.readByteString(8)

    init {
        // 'C', 'T', 'E', 'N', 'F', 'D', 'A'.
        require(header ==  ByteString(0x43, 0x54, 0x45, 0x4E, 0x46, 0x44, 0x41, 0x4D))
    }
}