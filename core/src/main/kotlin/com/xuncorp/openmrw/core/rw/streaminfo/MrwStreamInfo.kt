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

package com.xuncorp.openmrw.core.rw.streaminfo

class MrwStreamInfo {
    /**
     * Sample rate.
     */
    var sampleRate = 0
        internal set(value) {
            field = value
            durationMillis = calcDurationMillis()
        }

    /**
     * Number of channels.
     */
    var channelCount = 0
        internal set

    /**
     * Bits per sample.
     */
    var bits = 0
        internal set

    /**
     * Total number of samples, irrespective of the number of channels.
     */
    var sampleCount = 0L
        internal set(value) {
            field = value
            durationMillis = calcDurationMillis()
        }

    /**
     * Duration in milliseconds.
     */
    var durationMillis: Long = 0L
        private set

    private fun calcDurationMillis(): Long {
        return if (sampleRate == 0) {
            0L
        } else {
            sampleCount * 1000 / sampleRate
        }
    }

    override fun toString(): String {
        return "MrwStreamInfo(sampleRate=$sampleRate, channelCount=$channelCount, bits=$bits, " +
                "sampleCount=$sampleCount, durationMillis=$durationMillis)"
    }
}