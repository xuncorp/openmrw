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

package com.xuncorp.openmrw.core.format.ape

import com.xuncorp.openmrw.core.format.MrwFormat
import com.xuncorp.openmrw.core.rw.MrwReader
import com.xuncorp.openmrw.core.rw.ReaderProperties
import kotlinx.io.Source

internal class ApeMrwReader : MrwReader() {
    override fun match(source: Source) {
        val apeCommonHeader = ApeCommonHeader(source)
        require(
            apeCommonHeader.id == ApeCommonHeader.ID_MAC ||
                    apeCommonHeader.id == ApeCommonHeader.ID_MACF
        )
    }

    override fun fetch(source: Source, properties: ReaderProperties): MrwFormat {
        val apeMrwFormat = ApeMrwFormat()

        val apeCommonHeader = ApeCommonHeader(source.peek())
        val version = apeCommonHeader.version

        if (version > 3970u) {
            ApeDescriptor(source)
            // new header
            val apeHeader = ApeHeader(source)
            apeMrwFormat.mrwStreamInfo.apply {
                sampleRate = apeHeader.sampleRate.toInt()
                channelCount = apeHeader.channels.toInt()
                bits = apeHeader.bitsPerSample.toInt()
                sampleCount = apeHeader.sampleCount
            }
        } else {
            // old header
            val apeHeaderOld = ApeHeaderOld(source)
            apeMrwFormat.mrwStreamInfo.apply {
                sampleRate = apeHeaderOld.sampleRate.toInt()
                channelCount = apeHeaderOld.channels.toInt()
                bits = apeHeaderOld.bits
                sampleCount = apeHeaderOld.sampleCount
            }
        }

        return apeMrwFormat
    }
}