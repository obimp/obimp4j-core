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

import java.time.LocalDateTime

/**
 * Online contact
 * @author Alexander Krysin
 */
data class OnlineContact(
    val accountName: String,
    val statusValue: Int,
    var statusText: String? = null,
    var xStatusValue: Int? = null,
    var xStatusText: String? = null,
    var clientCapabilities: ClientCapabilities? = null,
    var clientType: ClientType? = null,
    var clientName: String? = null,
    var clientVersion: ClientVersion? = null,
    var clientConnectedTime: LocalDateTime? = null,
    var registrationDate: LocalDateTime? = null,
    var avatarMD5Hash: String? = null,
    var clientIpAddress: String? = null,
    var xStatusPictureId: String? = null,
    var operatingSystemName: String? = null,
    var clientDescription: String? = null,
    var customTransportStatusPictureId: Byte? = null,
    var clientIdentification: ClientIdentification? = null,
    var clientHostName: String? = null,
    var transportItemId: Int? = null
)
