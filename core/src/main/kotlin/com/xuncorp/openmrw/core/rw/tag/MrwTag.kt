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

package com.xuncorp.openmrw.core.rw.tag

import com.xuncorp.openmrw.core.UnstableOpenMrwApi
import com.xuncorp.openmrw.core.rw.tag.id3v2.Id3v2DeclaredFrames

class MrwTag {
    /**
     * The field with value.
     *
     * The reason for using a [List] instead of a [Map] is to allow multiple identical tags, such as
     * multiple ARTIST representing multiple artists, which is commonly used in FLAC Comments, but
     * OpenMrw does not recommend this writing style and recommends using separator.
     */
    private val values = mutableListOf<Pair<String, String>>()

    internal fun add(field: String, value: String) {
        values.add(field to value)
    }

    /**
     * @see Id3v2DeclaredFrames
     * @see OggVorbisCommentFields
     */
    fun get(field: String): List<String> {
        return values.filter { it.first == field }.map { it.second }
    }

    /**
     * Returns the comment value of the specified [MrwTagField], multiple original fields will
     * be merged, separated by [MrwTagField.SEPARATOR].
     */
    @UnstableOpenMrwApi
    fun get(field: MrwTagField): String {
        return values
            .filter { it.first in field.field }
            .run {
                if (field == MrwTagField.Artist || field == MrwTagField.Genre) {
                    joinToString(MrwTagField.SEPARATOR) { it.second }
                } else {
                    // First one.
                    firstOrNull()?.second ?: ""
                }
            }
    }

    fun getAll(): List<Pair<String, String>> {
        return values
    }

    override fun toString(): String {
        return "MrwTag(values=$values)"
    }
}

/**
 * @see Id3v2DeclaredFrames
 */
enum class MrwTagField(vararg val field: String) {
    Title(OggVorbisCommentFields.TITLE, Id3v2DeclaredFrames.TIT2),
    Artist(OggVorbisCommentFields.ARTIST, Id3v2DeclaredFrames.TPE1),
    Album(OggVorbisCommentFields.ALBUM, Id3v2DeclaredFrames.TALB),
    AlbumArtist(OggVorbisCommentFields.ALBUMARTIST, Id3v2DeclaredFrames.TPE2),
    Genre(OggVorbisCommentFields.GENRE, Id3v2DeclaredFrames.TCON),
    Lyrics(OggVorbisCommentFields.LYRICS, Id3v2DeclaredFrames.SYLT, Id3v2DeclaredFrames.USLT),
    TrackNumber(OggVorbisCommentFields.TRACKNUMBER, Id3v2DeclaredFrames.TRCK),
    Year(OggVorbisCommentFields.YEAR);

    companion object {
        const val SEPARATOR = "/"
    }
}