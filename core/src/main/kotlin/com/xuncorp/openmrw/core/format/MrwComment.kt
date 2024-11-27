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

package com.xuncorp.openmrw.core.format

import com.xuncorp.openmrw.core.UnstableOpenMrwApi
import com.xuncorp.openmrw.core.rw.id3v2.Id3v2DeclaredFrames

class MrwComment {
    /**
     * The comment fields.
     *
     * The reason for using a [List] instead of a [Map] is to allow multiple identical tags, such as
     * multiple ARTIST representing multiple artists, which is commonly used in FLAC Comments, but
     * OpenMrw does not recommend this writing style and recommends using separator.
     */
    private val comments = mutableListOf<Pair<String, String>>()

    internal fun add(field: String, value: String) {
        comments.add(field to value)
    }

    fun get(field: String): List<String> {
        return comments.filter { it.first == field }.map { it.second }
    }

    /**
     * Returns the comment value of the specified [MrwCommentField], multiple original fields will
     * be merged, separated by [MrwCommentField.SEPARATOR].
     */
    @UnstableOpenMrwApi
    fun get(mrwCommentField: MrwCommentField): String {
        return comments
            .filter { it.first in mrwCommentField.field }
            // TODO: Is it only the artist and genre fields that should be concatenated with
            //   delimiters, while other fields are allowed only one value?
            .joinToString(MrwCommentField.SEPARATOR) { it.second }
    }

    fun getAll(): List<Pair<String, String>> {
        return comments
    }

    override fun toString(): String {
        return "MrwComment(comments=$comments)"
    }
}

/**
 * @see Id3v2DeclaredFrames
 */
enum class MrwCommentField(vararg val field: String) {
    Title(MrwCommentCommonFields.TITLE, Id3v2DeclaredFrames.TIT2),
    Artist(MrwCommentCommonFields.ARTIST, Id3v2DeclaredFrames.TPE1),
    Album(MrwCommentCommonFields.ALBUM, Id3v2DeclaredFrames.TALB),
    AlbumArtist(MrwCommentCommonFields.ALBUMARTIST, Id3v2DeclaredFrames.TPE2),
    Genre(MrwCommentCommonFields.GENRE, Id3v2DeclaredFrames.TCON),
    Lyrics(MrwCommentCommonFields.LYRICS, Id3v2DeclaredFrames.SYLT, Id3v2DeclaredFrames.USLT),
    TrackNumber(MrwCommentCommonFields.TRACKNUMBER, Id3v2DeclaredFrames.TRCK);

    companion object {
        const val SEPARATOR = "/"
    }
}

/**
 * Common fields used in most formats.
 */
object MrwCommentCommonFields {
    const val TITLE = "TITLE"
    const val ARTIST = "ARTIST"
    const val ALBUM = "ALBUM"
    const val ALBUMARTIST = "ALBUMARTIST"
    const val GENRE = "GENRE"
    const val LYRICS = "LYRICS"
    const val TRACKNUMBER = "TRACKNUMBER"
}