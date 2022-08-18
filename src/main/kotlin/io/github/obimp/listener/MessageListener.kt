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

/**
 * Message listener
 * @author Alexander Krysin
 */
interface MessageListener {
    /**
     * Incoming message callback
     * @param accountName Account name
     * @param messageId Message ID
     * @param messageType Message type
     * @param messageData Message data
     */
    fun onIncomingMessage(accountName: String, messageId: Int, messageType: Int, messageData: String)

    /**
     * Message delivered callback
     * @param accountName Account name
     * @param messageId Message ID
     */
    fun onMessageDelivered(accountName: String, messageId: Int)

    /**
     * Notify callback
     * @param accountName Account name
     * @param notificationType Notification type
     * @param notificationValue Notification value
     */
    fun onNotify(accountName: String, notificationType: Int, notificationValue: Int)
}