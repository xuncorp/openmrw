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
    val blocksPerFrame = source.readUIntLe()
    val finalFrameBlocks = source.readUIntLe()
    val totalFrames = source.readUIntLe()
    val bitsPerSample = source.readUShortLe()
    val channels = source.readUShortLe()
    val sampleRate = source.readUIntLe()

    override fun toString(): String {
        return "ApeHeader(compressionLevel=$compressionLevel, formatFlags=$formatFlags, " +
                "blocksPerFrame=$blocksPerFrame, finalFrameBlocks=$finalFrameBlocks, " +
                "totalFrames=$totalFrames, bitsPerSample=$bitsPerSample, channels=$channels, " +
                "sampleRate=$sampleRate)"
    }
}