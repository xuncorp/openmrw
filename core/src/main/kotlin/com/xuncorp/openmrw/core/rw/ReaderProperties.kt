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

package com.xuncorp.openmrw.core.rw

import com.xuncorp.openmrw.core.rw.id3v2.Id3v2FrameHeader
import java.nio.charset.Charset

/**
 * Reader properties.
 *
 * @property id3v2Charset in ID3v2 tags, the 0x00 flag indicates that the encoding is
 *   [Charsets.ISO_8859_1]. However, many editors use the default encoding of the user's system when
 *   writing, such as [com.xuncorp.openmrw.core.util.GBK] or [com.xuncorp.openmrw.core.util.GB18030]
 *   in a Chinese environment. See also [Id3v2FrameHeader].
 */
data class ReaderProperties(
    val id3v2Charset: Charset = Charsets.ISO_8859_1
)