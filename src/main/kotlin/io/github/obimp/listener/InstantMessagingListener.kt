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

import io.github.obimp.im.EncryptionKeyReply
import io.github.obimp.im.IncomingMessage
import io.github.obimp.im.InstantMessagingParameters
import io.github.obimp.im.Notification
import java.util.*

/**
 * Instant messaging listener
 * @author Alexander Krysin
 */
interface InstantMessagingListener : EventListener {
    /**
     * Instant messaging parameters callback
     * @param instantMessagingParameters Instant messaging parameters
     */
    fun onInstantMessagingParameters(instantMessagingParameters: InstantMessagingParameters)

    /**
     * Offline messages done callback
     */
    fun onOfflineDone()

    /**
     * Incoming message callback
     * @param message Message
     */
    fun onIncomingMessage(incomingMessage: IncomingMessage)

    /**
     * Message report callback
     * @param accountName Account name
     * @param messageId Message ID
     */
    fun onMessageReport(accountName: String, messageId: Int)

    /**
     * Notify callback
     * @param notification Notification
     */
    fun onNotify(notification: Notification)

    /**
     * Encryption key request callback
     * @param accountName Account name
     */
    fun onEncryptionKeyRequest(accountName: String)

    /**
     * Encryption key reply callback
     * @param accountName Account name
     * @param encryptionKeyReply Encryption key reply
     */
    fun onEncryptionKeyReply(accountName: String, encryptionKeyReply: EncryptionKeyReply)
}