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

package com.xuncorp.openmrw.core.format.flac

import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.decodeToString
import kotlinx.io.readString
import kotlinx.io.readUIntLe

/**
 * @param byteString the first 4 bytes of the metadata block header.
 */
internal class FlacHeader(byteString: ByteString) {
    val isLastMetadataBlock = (byteString[0].toInt() and 0b10000000 shr 7) == 1

    val blockType = byteString[0].toInt() and 0b01111111

    val length = (byteString[1].toInt() and 0xFF shl 16) or
            (byteString[2].toInt() and 0xFF shl 8) or
            (byteString[3].toInt() and 0xFF)

    companion object {
        const val BLOCK_TYPE_STREAMINFO = 0
        const val BLOCK_TYPE_PADDING = 1
        const val BLOCK_TYPE_APPLICATION = 2
        const val BLOCK_TYPE_SEEKTABLE = 3
        const val BLOCK_TYPE_VORBIS_COMMENT = 4
        const val BLOCK_TYPE_CUESHEET = 5
        const val BLOCK_TYPE_PICTURE = 6
        const val BLOCK_TYPE_INVALID = 127
    }

    override fun toString(): String {
        return "FlacHeader(isLastMetadataBlock=$isLastMetadataBlock, " +
                "blockType=$blockType, " +
                "length=$length)"
    }
}

/**
 * @param byteString 34 bytes.
 *
 * @see FlacHeader.BLOCK_TYPE_STREAMINFO
 */
internal class FlacStreamInfo(byteString: ByteString) {
    /**
     * Samples.
     */
    val minBlockSize = (byteString[0].toInt() and 0xFF shl 8) or
            (byteString[1].toInt() and 0xFF)

    /**
     * Samples.
     */
    val maxBlockSize = (byteString[2].toInt() and 0xFF shl 8) or
            (byteString[3].toInt() and 0xFF)

    /**
     * Bytes.
     */
    val minFrameSize = (byteString[4].toInt() and 0xFF shl 16) or
            (byteString[5].toInt() and 0xFF shl 8) or
            (byteString[6].toInt() and 0xFF)

    /**
     * Bytes.
     */
    val maxFrameSize = (byteString[7].toInt() and 0xFF shl 16) or
            (byteString[8].toInt() and 0xFF shl 8) or
            (byteString[9].toInt() and 0xFF)

    /**
     * Max 655350 Hz.
     */
    val sampleRate = (byteString[10].toInt() and 0xFF shl 12) or
            (byteString[11].toInt() and 0xFF shl 4) or
            (byteString[12].toInt() and 0xFF shr 4)

    /**
     * 2 to 8.
     */
    val channelCount = (byteString[12].toInt() and 0x0F shr 1) + 1

    /**
     * Bits per sample, 4 to 32.
     */
    val bits = ((byteString[12].toInt() and 0x1 shl 4) or
                    (byteString[13].toInt() and 0xF0 shr 4)) + 1

    val sampleCount = (byteString[13].toLong() and 0x0F shl 32) or
            (byteString[14].toLong() and 0xFF shl 24) or
            (byteString[15].toLong() and 0xFF shl 16) or
            (byteString[16].toLong() and 0xFF shl 8) or
            (byteString[17].toLong() and 0xFF)

    val unencodedAudioDataMd5 = byteString.substring(18, 34).decodeToString()
}

/**
 * FLAC tags, without the framing bit.
 *
 * [Ogg Vorbis](https://www.xiph.org/vorbis/doc/v-comment.html).
 *
 * @see FlacHeader.BLOCK_TYPE_VORBIS_COMMENT
 */
internal class FlacVorbisComment(source: Source) {
    val vendorString: String

    val userComments: List<String>

    init {
        val vendorLength = source.readUIntLe().toLong()
        vendorString = source.readString(vendorLength)

        val userCommentListLength = source.readUIntLe().toLong()
        userComments = ArrayList(userCommentListLength.toInt())

        for (i in 0 until userCommentListLength) {
            val userCommentLength = source.readUIntLe().toLong()
            val userComment = source.readString(userCommentLength)
            userComments.add(userComment)
        }
    }

    override fun toString(): String {
        return "FlacVorbisComment(vendorString='$vendorString', userComments=$userComments)"
    }
}