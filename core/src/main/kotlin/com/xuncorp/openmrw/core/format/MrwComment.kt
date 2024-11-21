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

    fun get(mrwCommentField: MrwCommentField): List<String> {
        return get(mrwCommentField.field)
    }

    fun getAll(): List<Pair<String, String>> {
        return comments
    }

    override fun toString(): String {
        return "MrwComment(comments=$comments)"
    }
}

enum class MrwCommentField(val field: String) {
    Title("TITLE"),
    Artist("ARTIST"),
    Album("ALBUM"),
    AlbumArtist("ALBUMARTIST"),
    Genre("GENRE"),
    Lyrics("LYRICS")
}