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

@file:Suppress("unused", "SpellCheckingInspection")

package com.xuncorp.openmrw.core.rw.id3v2

/**
 * ID3v2.3.0 frames.
 */
internal object Id3v2DeclaredFrames {
    /**
     * Audio encryption.
     */
    const val APNC = "APNC"

    /**
     * Attached picture.
     */
    const val APIC = "APIC"

    /**
     * Comments.
     */
    const val COMM = "COMM"

    /**
     * Commercial frame.
     */
    const val COMR = "COMR"

    /**
     * Encryption method registration.
     */
    const val ENCR = "ENCR"

    /**
     * Equalization.
     */
    const val EQUA = "EQUA"

    /**
     * Event timing codes.
     */
    const val ETCO = "ETCO"

    /**
     * General encapsulated object.
     */
    const val GEOB = "GEOB"

    /**
     * Group identification registration.
     */
    const val GRID = "GRID"

    /**
     * Involved people list.
     */
    const val IPLS = "IPLS"

    /**
     * Linked information.
     */
    const val LINK = "LINK"

    /**
     * Music CD identifier.
     */
    const val MCDI = "MCDI"

    /**
     * MPEG location lookup table.
     */
    const val MLLT = "MLLT"

    /**
     * Ownership frame.
     */
    const val OWNE = "OWNE"

    /**
     * Private frame.
     */
    const val PRIV = "PRIV"

    /**
     * Play counter.
     */
    const val PCNT = "PCNT"

    /**
     * Popularimeter.
     */
    const val POPM = "POPM"

    /**
     * Position synchronisation frame.
     */
    const val POSS = "POSS"

    /**
     * Recommended buffer size.
     */
    const val RBUF = "RBUF"

    /**
     * Relative volume adjustment.
     */
    const val RVAD = "RVAD"

    /**
     * Reverb.
     */
    const val RVRB = "RVRB"

    /**
     * Synchronized lyric/text.
     */
    const val SYLT = "SYLT"

    /**
     * Synchronized tempo codes.
     */
    const val SYTC = "SYTC"

    /**
     * Album/Movie/Show title.
     */
    const val TALB = "TALB"

    /**
     * BPM (beats per minute).
     */
    const val TBPM = "TBPM"

    /**
     * Composer.
     */
    const val TCOM = "TCOM"

    /**
     * Content type.
     */
    const val TCON = "TCON"

    /**
     * Copyright message.
     */
    const val TCOP = "TCOP"

    /**
     * Date.
     */
    const val TDAT = "TDAT"

    /**
     * Playlist delay.
     */
    const val TDLY = "TDLY"

    /**
     * Encoded by.
     */
    const val TENC = "TENC"

    /**
     * Lyricist/Text writer.
     */
    const val TEXT = "TEXT"

    /**
     * File type.
     */
    const val TFLT = "TFLT"

    /**
     * Time.
     */
    const val TIME = "TIME"

    /**
     * Content group description.
     */
    const val TIT1 = "TIT1"

    /**
     * Title/songname/content description.
     */
    const val TIT2 = "TIT2"

    /**
     * Subtitle/Description refinement.
     */
    const val TIT3 = "TIT3"

    /**
     * Initial key.
     */
    const val TKEY = "TKEY"

    /**
     * Language(s).
     */
    const val TLAN = "TLAN"

    /**
     * Length.
     */
    const val TLEN = "TLEN"

    /**
     * Media type.
     */
    const val TMED = "TMED"

    /**
     * Original album/movie/show title.
     */
    const val TOAL = "TOAL"

    /**
     * Original filename.
     */
    const val TOFN = "TOFN"

    /**
     * Original lyricist(s)/text writer(s).
     */
    const val TOLY = "TOLY"

    /**
     * artist(s)/performer(s).
     */
    const val TOPE = "TOPE"

    /**
     * Original release year.
     */
    const val TORY = "TORY"

    /**
     * File owner/licensee.
     */
    const val TOWN = "TOWN"

    /**
     * Artist. Lead performer(s)/Soloist(s).
     */
    const val TPE1 = "TPE1"

    /**
     * Album artist. Band/orchestra/accompaniment.
     */
    const val TPE2 = "TPE2"

    /**
     * Conductor/performer refinement.
     */
    const val TPE3 = "TPE3"

    /**
     * Interpreted, remixed, or otherwise modified by.
     */
    const val TPE4 = "TPE4"

    /**
     * Part of a set.
     */
    const val TPOS = "TPOS"

    /**
     * Publisher.
     */
    const val TPUB = "TPUB"

    /**
     * Track number/Position in set.
     */
    const val TRCK = "TRCK"

    /**
     * Recording dates.
     */
    const val TRDA = "TRDA"

    /**
     * Internet radio station name.
     */
    const val TRSN = "TRSN"

    /**
     * Internet radio station owner.
     */
    const val TRSO = "TRSO"

    /**
     * Size.
     */
    const val TSIZ = "TSIZ"

    /**
     * ISRC (international standard recording code).
     */
    const val TSRC = "TSRC"

    /**
     * Software/Hardware and settings used for encoding.
     */
    const val TSSE = "TSSE"

    /**
     * Year.
     */
    const val TYER = "TYER"

    /**
     * User defined text information frame.
     */
    const val TXXX = "TXXX"

    /**
     * Unique file identifier.
     */
    const val UFID = "UFID"

    /**
     * Terms of use.
     */
    const val USER = "USER"

    /**
     * Unsychronized lyric/text transcription.
     */
    const val USLT = "USLT"

    /**
     * Commercial information.
     */
    const val WCOM = "WCOM"

    /**
     * Copyright/Legal information.
     */
    const val WCOP = "WCOP"

    /**
     * Official audio file webpage.
     */
    const val WOAF = "WOAF"

    /**
     * Official artist/performer webpage.
     */
    const val WOAR = "WOAR"

    /**
     * Official audio source webpage.
     */
    const val WOAS = "WOAS"

    /**
     * Official internet radio station homepage.
     */
    const val WORS = "WORS"

    /**
     * Payment.
     */
    const val WPAY = "WPAY"

    /**
     * Publishers official webpage.
     */
    const val WPUB = "WPUB"

    /**
     * User defined URL link frame.
     */
    const val WXXX = "WXXX"
}