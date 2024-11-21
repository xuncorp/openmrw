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

package com.xuncorp.openmrw.core.format.ape

import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.readUIntLe
import kotlinx.io.readUShortLe

private const val APE_COMPRESSION_LEVEL_FAST: UShort = 1000u
private const val APE_COMPRESSION_LEVEL_NORMAL: UShort = 2000u
private const val APE_COMPRESSION_LEVEL_HIGH: UShort = 3000u
private const val APE_COMPRESSION_LEVEL_EXTRA_HIGH: UShort = 4000u
private const val APE_COMPRESSION_LEVEL_INSANE: UShort = 5000u

private const val APE_FORMAT_FLAG_8_BIT = 1 shl 0
private const val APE_FORMAT_FLAG_CRC = 1 shl 1
private const val APE_FORMAT_FLAG_HAS_PEAK_LEVEL = 1 shl 2
private const val APE_FORMAT_FLAG_24_BIT = 1 shl 3
private const val APE_FORMAT_FLAG_HAS_SEEK_ELEMENTS = 1 shl 4
private const val APE_FORMAT_FLAG_CREATE_WAV_HEADER = 1 shl 5
private const val APE_FORMAT_FLAG_AIFF = 1 shl 6
private const val APE_FORMAT_FLAG_W64 = 1 shl 7
private const val APE_FORMAT_FLAG_SND = 1 shl 8

/**
 * Peek new source.
 */
internal class ApeCommonHeader(source: Source) {
    val id: ByteString
    val version: UShort

    init {
        source.peek().use { peek ->
            id = peek.readByteString(4)
            version = peek.readUShortLe()
        }
    }

    companion object {
        /**
         * Common magic header
         */
        val ID_MAC = ByteString(0x4D, 0x41, 0x43, 0x20)

        /**
         * new magic header, only ape version > 3970 (3.97).
         */
        val ID_MACF = ByteString(0x4D, 0x41, 0x43, 0x46)
    }

    override fun toString(): String {
        return "ApeCommonHeader(id=$id, version=$version)"
    }
}

/**
 * Old header, ape version <= 3970 (3.97).
 */
internal class ApeHeaderOld(source: Source) {
    val id = source.readByteString(4)
    val version = source.readUShortLe()
    val compressionLevel = source.readUShortLe()
    val formatFlags = source.readUShortLe()
    val channels = source.readUShortLe()
    val sampleRate = source.readUIntLe()
    val headerBytes = source.readUIntLe()
    val terminatingBytes = source.readUIntLe()
    val totalFrames = source.readUIntLe()
    val finalFrameBlocks = source.readUIntLe()

    /**
     * The number of audio blocks in one frame
     */
    val blocksPerFrame =
        if ((version >= 3900u) ||
            (version >= 3800u && compressionLevel == APE_COMPRESSION_LEVEL_EXTRA_HIGH)
        ) {
            73728
        } else {
            9216
        }

    val bits =
        if (formatFlags.toInt() and APE_FORMAT_FLAG_8_BIT != 0) {
            8
        } else if (formatFlags.toInt() and APE_FORMAT_FLAG_24_BIT != 0) {
            24
        } else {
            16
        }

    val sampleCount =
        if (totalFrames == 0u) {
            0L
        } else {
            (totalFrames.toLong() - 1L) * blocksPerFrame.toLong() + finalFrameBlocks.toLong()
        }

    override fun toString(): String {
        return "ApeHeaderOld(id=$id, version=$version, compressionLevel=$compressionLevel, " +
                "formatFlags=$formatFlags, channels=$channels, sampleRate=$sampleRate, " +
                "headerBytes=$headerBytes, terminatingBytes=$terminatingBytes, " +
                "totalFrames=$totalFrames, finalFrameBlocks=$finalFrameBlocks)"
    }
}

internal class ApeDescriptor(source: Source) {
    val id = source.readByteString(4)
    val version = source.readUShortLe()
    val padding = source.readUShortLe()
    val descriptorBytes = source.readUIntLe()
    val headerBytes = source.readUIntLe()
    val seekTableBytes = source.readUIntLe()
    val headerDataBytes = source.readUIntLe()
    val apeFrameDataBytes = source.readUIntLe()
    val apeFrameDataBytesHigh = source.readUIntLe()
    val terminatingDataBytes = source.readUIntLe()
    val fileMd5 = source.readByteString(16)

    override fun toString(): String {
        return "ApeDescriptor(id=$id, version=$version, padding=$padding, " +
                "descriptorBytes=$descriptorBytes, headerBytes=$headerBytes, " +
                "seekTableBytes=$seekTableBytes, headerDataBytes=$headerDataBytes, " +
                "apeFrameDataBytes=$apeFrameDataBytes, " +
                "apeFrameDataBytesHigh=$apeFrameDataBytesHigh, " +
                "terminatingDataBytes=$terminatingDataBytes, fileMd5=$fileMd5)"
    }
}

internal class ApeHeader(source: Source) {
    val compressionLevel = source.readUShortLe()
    val formatFlags = source.readUShortLe()

    /**
     * The number of audio blocks in one frame
     */
    val blocksPerFrame = source.readUIntLe()

    val finalFrameBlocks = source.readUIntLe()
    val totalFrames = source.readUIntLe()
    val bitsPerSample = source.readUShortLe()
    val channels = source.readUShortLe()
    val sampleRate = source.readUIntLe()

    val sampleCount =
        if (totalFrames == 0u) {
            0L
        } else {
            (totalFrames.toLong() - 1L) * blocksPerFrame.toLong() + finalFrameBlocks.toLong()
        }

    override fun toString(): String {
        return "ApeHeader(compressionLevel=$compressionLevel, formatFlags=$formatFlags, " +
                "blocksPerFrame=$blocksPerFrame, finalFrameBlocks=$finalFrameBlocks, " +
                "totalFrames=$totalFrames, bitsPerSample=$bitsPerSample, channels=$channels, " +
                "sampleRate=$sampleRate)"
    }
}