/*
 * OBIMP4J - Java OBIMP Lib
 * Copyright (C) 2013—2022 Alexander Krysin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.obimp.cl

/**
 * Contact list parameters and limits
 * @author Alexander Krysin
 */
class ContactListParameters(
    /** Maximal groups count */
    val maxGroupsCount: Int,
    /** Maximal UTF-8 encoded group name length */
    val maxGroupNameLength: Int,
    /** Maximal contacts count all over contact list */
    val maxContactsCount: Int,
    /** Maximal UTF-8 encoded account name length */
    val maxAccountNameLength: Int,
    /** Maximal UTF-8 encoded contact name / transport friendly name length */
    val maxContactOrTransportNameLength: Int,
    /** Maximal UTF-8 encoded authorization reason / revoke length */
    val maxAuthReasonOrRevokeLength: Int,
    /** Maximal user / developer sTLDs count in one item */
    val maxUserDevSTLDsCount: Int,
    /** Maximal user / developer sTLD length */
    val maxUserDevSTLDLength: Int,
    /** Offline authorization messages (requests, replies, revokes) count waiting for client request */
    val waitingOfflineAuthMessagesCount: Int,
    /** Automatically remove authorization flag after adding contact */
    val autoRemoveAuthFlagAfterAddingContact: Boolean,
    /** Maximal notes count */
    val maxNotesCount: Int,
    /** Maximal UTF-8 encoded note name length */
    val maxNoteNameLength: Int,
    /** Maximal UTF-8 encoded note text length */
    val maxNoteTextLength: Int
)