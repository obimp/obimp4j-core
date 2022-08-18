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

package io.github.obimp

import io.github.obimp.listener.*

/**
 * @author Alexander Krysin
 */
sealed class ListenerManager {
    val connectionListeners = mutableListOf<ConnectionListener>()
    val messageListeners = mutableListOf<MessageListener>()
    val userStatusListeners = mutableListOf<UserStatusListener>()
    val metaInfoListeners = mutableListOf<MetaInfoListener>()
    val contactListListeners = mutableListOf<ContactListListener>()
    val userAvatarsListeners = mutableListOf<UserAvatarsListener>()

    fun addConnectionListener(connectionListener: ConnectionListener) = connectionListeners.add(connectionListener)

    fun addMessageListener(messageListener: MessageListener) = messageListeners.add(messageListener)

    fun addUserStatusListener(userStatusListener: UserStatusListener) = userStatusListeners.add(userStatusListener)

    fun addMetaInfoListener(metaInfoListener: MetaInfoListener) = metaInfoListeners.add(metaInfoListener)

    fun addContactListListener(contactListListener: ContactListListener) = contactListListeners.add(contactListListener)

    fun addUserAvatarListener(userAvatarsListener: UserAvatarsListener) = userAvatarsListeners.add(userAvatarsListener)

    fun removeConnectionListener(connectionListener: ConnectionListener) =
        connectionListeners.remove(connectionListener)

    fun removeMessageListener(messageListener: MessageListener) = messageListeners.remove(messageListener)

    fun removeUserStatusListener(userStatusListener: UserStatusListener) =
        userStatusListeners.remove(userStatusListener)

    fun removeMetaInfoListener(metaInfoListener: MetaInfoListener) = metaInfoListeners.remove(metaInfoListener)

    fun removeContactListListener(contactListListener: ContactListListener) =
        contactListListeners.remove(contactListListener)

    fun removeUserAvatarsListener(userAvatarsListener: UserAvatarsListener) =
        userAvatarsListeners.remove(userAvatarsListener)
}