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

package com.xuncorp.openmrw.core.format

enum class MrwFormatType {
    /**
     * # Free Lossless Audio Codec
     *
     * @since 0.1.0-dev01 2024/11/20
     */
    Flac,

    /**
     * # Monkey's Audio
     *
     * [Official Site](https://monkeysaudio.com/)
     *
     * @since 0.1.0-dev01 2024/11/20
     */
    Ape,

    /**
     * # MPEG-1 Audio Layer 3
     *
     * @since 0.1.0-dev01 2024/11/21
     */
    Mp3,

    /**
     * # NetEase Cloud Music
     *
     * @since 0.1.0-dev04 2024/11/26
     */
    Ncm
}