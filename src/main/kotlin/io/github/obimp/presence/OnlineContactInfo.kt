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

import io.github.obimp.util.Version
import java.nio.ByteBuffer
import java.time.LocalDateTime

/**
 * @author Alexander Krysin
 */
class OnlineContactInfo(
    val accountName: String,
    val status: Status,
    var statusName: String? = null,
    var xStatusNumber: Int? = null,
    var xStatusDescription: String? = null,
    var clientCapabilities: List<ClientCapability>? = null,
    var clientType: ClientType? = null,
    var clientName: String? = null,
    var clientVersion: Version? = null,
    var clientConnectedTime: LocalDateTime? = null,
    var registrationDate: LocalDateTime? = null,
    var avatarMD5Hash: ByteArray? = null,
    var clientIPAddress: String? = null,
    var xStatus: XStatus? = null,
    var clientOperatingSystemName: String? = null,
    var clientDescription: String? = null,
    var customTransportStatusPictureID: Byte? = null,
    var transportClientIdentificationSTLDs: ByteBuffer? = null,
    var clientHostName: String? = null,
    var transportItemID: Int? = null
)