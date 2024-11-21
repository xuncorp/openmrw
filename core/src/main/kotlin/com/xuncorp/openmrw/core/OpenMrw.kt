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

package com.xuncorp.openmrw.core

import com.xuncorp.openmrw.core.format.MrwFormat
import com.xuncorp.openmrw.core.format.ape.ApeMrwReader
import com.xuncorp.openmrw.core.format.flac.FlacMrwReader
import kotlinx.io.Source

object OpenMrw {

    private val readers by lazy {
        listOf(
            FlacMrwReader(),
            ApeMrwReader()
        )
    }

    fun read(source: Source): Result<MrwFormat> = runCatching {
        for (reader in readers) {
            if (reader.match(source).getOrNull() != null) {
                return@runCatching reader.fetch(source)
            }
        }

        throw IllegalArgumentException("Unsupported source.")
    }

}