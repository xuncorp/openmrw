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
import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString

internal class FlacMrwReader : MrwReader(MrwFormatType.Flac) {
    override fun match(source: Source): Boolean {
        val peek = source.peek()
        val magicHeader = peek.readByteString(4)
        peek.close()
        return magicHeader == MAGIC_HEADER
    }

    override fun fetch(source: Source): MrwFormat {
        val flacMrwFormat = FlacMrwFormat()

        flacMrwFormat.sampleRate = 0

        return FlacMrwFormat()
    }

    companion object {
        private val MAGIC_HEADER = ByteString(0x66, 0x4C, 0x61, 0x43)
    }
}