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

package io.github.obimp.presence

import java.time.LocalDateTime

/**
 * Current presence information of client's account
 * @author Alexander Krysin
 */
class CurrentPresenceInfo(
    /** Account name */
    val accountName: String,
    /** Registration date */
    val registrationDate: LocalDateTime,
    /** Current session client connected time */
    val currentSessionClientConnectedTime: LocalDateTime,
    /** Previous session client connected time */
    val previousSessionClientConnectedTime: LocalDateTime,
    /** Current session client IP address (as it seen by server) */
    val currentSessionClientIP: String,
    /** Previous session client IP address (as it seen by server) */
    val previousSessionClientIP: String,
    /** Currently signed on instances count */
    val currentlySignedOnInstancesCount: Short,
    /** Additional server added description */
    val additionServerAddedDescription: String,
    /** Presence information (will be added only for OBIMP client presence info) */
    val presenceInfo: PresenceInfo?,
    /** Transport Item ID (optional) */
    val transportItemID: Int?
)