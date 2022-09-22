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

import io.github.obimp.cl.*
import java.util.*

/**
 * Contact list listener
 * @author Alexander Krysin
 */
interface ContactListListener : EventListener {
    /**
     * Contact list parameters callback
     * @param contactListParameters Contact list parameters
     */
    fun onContactListParameters(contactListParameters: ContactListParameters)

    /**
     * Contact list loaded callback
     * @param items Contact list items
     */
    fun onContactListLoaded(items: List<ContactListItem>)

    /**
     * Contact list verify callback
     * @param contactListMD5Hash Contact list MD5 hash
     */
    fun onContactListVerify(contactListMD5Hash: ByteArray)

    /**
     * Add contact list item callback
     * @param contactListItemAddingResult Contact list item adding result
     */
    fun onAddContactListItem(contactListItemAddingResult: AddingResult)

    /**
     * Delete contact list item callback
     * @param contactListItemDeletionResult Contact list item deletion result
     */
    fun onDeleteContactListItem(contactListItemDeletionResult: DeletionResult)

    /**
     * Update contact list item callback
     * @param contactListItemUpdateOperationResult Contact list item update operation result
     */
    fun onUpdateContactListItem(contactListItemUpdateOperationResult: UpdateOperationResult)

    /**
     * Authorization request callback
     * @param accountName Account name
     * @param reason Reason
     */
    fun onAuthorizationRequest(accountName: String, reason: String)

    /**
     * Authorization reply callback
     * @param accountName Account name
     * @param authorizationReply Authorization reply
     */
    fun onAuthorizationReply(accountName: String, authorizationReply: AuthorizationReply)

    /**
     * Authorization revoke callback
     * @param accountName Account name
     * @param reason Reason
     */
    fun onAuthorizationRevoke(accountName: String, reason: String)

    /**
     * Offline authorization requests/replies/revokes done callback
     */
    fun onOffauthDone()

    /**
     * Contact list item operation callback
     * @param contactListItem Contact list item
     * @param operation Contact list item operation
     */
    fun onContactListItemOperation(contactListItem: ContactListItem, operation: Operation)

    /**
     * Begin server-side contact list item operations callback
     */
    fun onBeginUpdate()

    /**
     * End of server-side contact list item operations callback
     */
    fun onEndUpdate()
}