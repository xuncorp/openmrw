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
import kotlinx.io.Source

internal class ApeMrwReader : MrwReader() {
    override fun match(source: Source): Boolean {
        val apeCommonHeader = ApeCommonHeader(source)
        return apeCommonHeader.id == ApeCommonHeader.ID_MAC ||
                apeCommonHeader.id == ApeCommonHeader.ID_MACF
    }

    override fun fetch(source: Source): MrwFormat {
        val apeMrwFormat = ApeMrwFormat()

        source.peek().use { peek ->
            val apeCommonHeader = ApeCommonHeader(peek)
            val version = apeCommonHeader.version

            if (version > 3970u) {
                ApeDescriptor(peek)
                // new header
                val apeHeader = ApeHeader(peek)
                apeMrwFormat.mrwStreamInfo.apply {
                    sampleRate = apeHeader.sampleRate.toInt()
                    channelCount = apeHeader.channels.toInt()
                    bits = apeHeader.bitsPerSample.toInt()
                    sampleCount = if (apeHeader.totalFrames == 0u) {
                        0L
                    } else {
                        (apeHeader.totalFrames.toLong() - 1) *
                                apeHeader.blocksPerFrame.toLong() +
                                apeHeader.finalFrameBlocks.toLong()
                    }
                }
                println(apeHeader)
            } else {
                // old header
                val apeHeaderOld = ApeHeaderOld(peek)
                apeMrwFormat.mrwStreamInfo.apply {
                    sampleRate = apeHeaderOld.sampleRate.toInt()
                    channelCount = apeHeaderOld.channels.toInt()
                }
            }
        }

        return apeMrwFormat
    }
}