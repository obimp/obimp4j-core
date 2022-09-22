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

import io.github.obimp.presence.MailNotification
import io.github.obimp.presence.OnlineContactInfo
import io.github.obimp.presence.PresenceInfo
import io.github.obimp.presence.PresenceInfoParameters
import java.util.*

/**
 * Presence info listener
 * @author Alexander Krysin
 */
interface PresenceInfoListener : EventListener {
    /**
     * Presence info parameters callback
     * @param presenceInfoParameters Presence info parameters
     */
    fun onPresenceInfoParameters(presenceInfoParameters: PresenceInfoParameters)
    /**
     * Contact online callback
     * @param onlineContactInfo Online contact info
     */
    fun onContactOnline(onlineContactInfo: OnlineContactInfo)

    /**
     * Contact offline callback
     * @param accountName Account name
     */
    fun onContactOffline(accountName: String)

    /**
     * Presence info callback
     * @param presenceInfo Presence info
     */
    fun onPresenceInfo(presenceInfo: PresenceInfo)

    /**
     * Mail notification callback
     * @param mailNotification Mail notification
     */
    fun onMailNotification(mailNotification: MailNotification)

    /**
     * Own mail URL callback
     * @param ownMailURL Own mail URL
     */
    fun onOwnMailURL(ownMailURL: String)
}