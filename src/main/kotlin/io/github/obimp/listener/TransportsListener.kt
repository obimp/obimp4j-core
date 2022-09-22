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

import io.github.obimp.transports.*
import java.util.*

/**
 * Transports listener
 * @author Alexander Krysin
 */
interface TransportsListener : EventListener {
    /**
     * Transports parameters callback
     * @param transportsParameters Transports parameters
     */
    fun onTransportsParameters(transportsParameters: TransportsParameters)

    /**
     * Transport ready callback
     * @param transportID Transport ID
     * @param transportOptions Transport options
     */
    fun onTransportReady(transportID: Int, transportOptions: TransportOptions)

    /**
     * Update transport setting callback
     * @param transportID Transport ID
     * @param transportSettingsUpdateResult Transport setting update result
     */
    fun onUpdateTransportSetting(transportID: Int, transportSettingsUpdateResult: TransportSettingsUpdateResult)

    /**
     * Transport info callback
     * @param transportID Transport ID
     * @param transportState Transport state
     */
    fun onTransportInfo(transportID: Int, transportState: TransportState)

    /**
     * Show notification callback
     * @param transportNotification Transport notification
     */
    fun onShowNotification(transportNotification: TransportNotification)

    fun onOwnAvatarHash(transportID: Int, avatarMD5Hash: ByteArray)
}