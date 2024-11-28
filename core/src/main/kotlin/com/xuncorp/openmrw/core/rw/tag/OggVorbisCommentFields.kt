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

@file:Suppress("unused", "UnusedReceiverParameter")

package com.xuncorp.openmrw.core.rw.tag

/**
 * # Ogg Vorbis Comment
 *
 * Most code comments are sourced from the content on website
 * [Ogg Vorbis Comment](https://www.xiph.org/vorbis/doc/v-comment.html)
 * (© 1994 - 2005 Xiph.Org. All rights reserved.), with some possibly having been modified.
 */
object OggVorbisCommentFields {
    /**
     * Track/Work name.
     */
    const val TITLE = "TITLE"

    /**
     * The version field may be used to differentiate multiple versions of the same track title in a
     * single collection. (e.g. remix info).
     */
    const val VERSION = "VERSION"

    /**
     * The collection name to which this track belongs.
     */
    const val ALBUM = "ALBUM"

    /**
     * The track number of this piece if part of a specific larger collection or album.
     */
    const val TRACKNUMBER = "TRACKNUMBER"

    /**
     * The artist generally considered responsible for the work. In popular music this is usually
     * the performing band or singer. For classical music it would be the composer. For an audio
     * book it would be the author of the original text.
     */
    const val ARTIST = "ARTIST"

    /**
     * The artist(s) who performed the work. In classical music this would be the conductor,
     * orchestra, soloists. In an audio book it would be the actor who did the reading. In popular
     * music this is typically the same as the ARTIST and is omitted.
     */
    const val PERFORMER = "PERFORMER"

    /**
     * Copyright attribution, e.g., '2001 Nobody's Band' or '1999 Jack Moffitt'.
     */
    const val COPYRIGHT = "COPYRIGHT"

    /**
     * License information, for example, 'All Rights Reserved', 'Any Use Permitted', a URL to a
     * license such as a Creative Commons license (e.g. "creativecommons.org/licenses/by/4.0/"), or
     * similar.
     */
    const val LICENSE = "LICENSE"

    /**
     * Name of the organization producing the track (i.e. the 'record label').
     */
    const val ORGANIZATION = "ORGANIZATION"

    /**
     * A short text description of the contents.
     */
    const val DESCRIPTION = "DESCRIPTION"

    /**
     * A short text indication of music genre.
     */
    const val GENRE = "GENRE"

    /**
     * Date the track was recorded.
     */
    const val DATE = "DATE"

    /**
     * Location where track was recorded.
     */
    const val LOCATION = "LOCATION"

    /**
     * Contact information for the creators or distributors of the track. This could be a URL, an
     * email address, the physical address of the producing label.
     */
    const val CONTACT = "CONTACT"

    /**
     * ISRC number for the track; see [the ISRC intro page](https://isrc.ifpi.org) for more
     * information on ISRC numbers.
     */
    const val ISRC = "ISRC"

    /**
     * The major artist of the album.
     */
    @OpenMrwExtendTagField
    const val ALBUMARTIST = "ALBUMARTIST"

    /**
     * The lyrics to the track.
     */
    @OpenMrwExtendTagField
    const val LYRICS = "LYRICS"

    /**
     * The year the track was recorded.
     */
    @OpenMrwExtendTagField
    const val YEAR = "YEAR"
}