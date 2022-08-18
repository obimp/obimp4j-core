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

package io.github.obimp.listener

import io.github.obimp.cl.ContactListItem

/**
 * Contact list listener
 * @author Alexander Krysin
 */
interface ContactListListener {
    /**
     * Auth request callback
     * @param accountName Account name
     * @param reason Reason
     */
    fun onAuthRequest(accountName: String, reason: String)

    /**
     * Auth reply callback
     * @param accountName Account name
     * @param replyCode Reply code
     */
    fun onAuthReply(accountName: String, replyCode: Int)

    /**
     * Auth revoke callback
     * @param accountName Account name
     * @param reason Reason
     */
    fun onAuthRevoke(accountName: String, reason: String)

    /**
     * Contact list loaded callback
     * @param contactList Contact list
     */
    fun onContactListLoad(contactList: List<ContactListItem>)
}