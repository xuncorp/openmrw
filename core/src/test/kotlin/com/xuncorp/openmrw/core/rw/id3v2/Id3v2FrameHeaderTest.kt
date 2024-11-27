package com.xuncorp.openmrw.core.rw.id3v2

import com.xuncorp.openmrw.core.rw.tag.id3v2.Id3v2FrameHeader
import kotlinx.io.bytestring.ByteString
import org.junit.Test

class Id3v2FrameHeaderTest {
    @Test
    fun synchronizeByteString_empty() {
        val byteString = ByteString()
        val newByteString = Id3v2FrameHeader.synchronizeByteString(byteString)
        assert(newByteString == ByteString())
    }

    @Test
    fun synchronizeByteString_1() {
        val byteString = ByteString(
            0xFF.toByte(), 0x00.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte()
        )
        val newByteString = Id3v2FrameHeader.synchronizeByteString(byteString)
        assert(newByteString == ByteString(0xFF.toByte(), 0xFF.toByte(), 0x00.toByte()))
    }
}