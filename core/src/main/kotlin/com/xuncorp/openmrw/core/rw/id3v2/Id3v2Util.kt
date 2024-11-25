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

@file:Suppress("unused")

package com.xuncorp.openmrw.core.rw.id3v2

import kotlinx.io.bytestring.ByteString

internal object Id3v2Util {
    /**
     * 0xFF 00 -> 0xFF
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