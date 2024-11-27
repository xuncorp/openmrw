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

import com.xuncorp.openmrw.core.format.MrwFile
import com.xuncorp.openmrw.core.format.ape.ApeMrwReader
import com.xuncorp.openmrw.core.format.flac.FlacMrwReader
import com.xuncorp.openmrw.core.format.mp3.Mp3MrwReader
import com.xuncorp.openmrw.core.format.ncm.NcmMrwReader
import com.xuncorp.openmrw.core.rw.ReaderProperties
import kotlinx.io.Source

/**
 * OpenMrw (Open Metadata Reader Writer), a tool library for the JVM platform.
 */
object OpenMrw {
    /**
     * TODO: Currently, sorting by the prevalence of possible formats, and will try other later.
     */
    private val readers by lazy {
        listOf(
            Mp3MrwReader(),
            FlacMrwReader(),
            ApeMrwReader(),
            NcmMrwReader()
        )
    }

    /**
     * Returns the [Result] of [MrwFile] by [Source].
     *
     * @param properties [ReaderProperties].
     */
    @UnstableOpenMrwApi
    fun read(
        source: Source,
        properties: ReaderProperties = ReaderProperties()
    ): Result<MrwFile> = runCatching {
        for (reader in readers) {
            val matched = source.peek().use { matchSource ->
                try {
                    reader.match(matchSource)
                    true
                } catch (_: Exception) {
                    false
                }
            }

            if (matched) {
                source.peek().use { fetchSource ->
                    return@runCatching reader.fetch(fetchSource, properties)
                }
            }
        }

        throw IllegalArgumentException("Unsupported source.")
    }
}