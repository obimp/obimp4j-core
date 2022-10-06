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
 * Online contact presence info
 * @author Alexander Krysin
 */
class OnlineContactInfo(
    /** Account name */
    val accountName: String,
    /** Presence status value */
    val status: Status,
    /** Status name (optional) */
    var statusName: String? = null,
    /** Additional status picture number (optional) */
    var xStatusNumber: Int? = null,
    /** Additional status picture description (optional) */
    var xStatusDescription: String? = null,
    /** Client capabilities (optional) */
    var clientCapabilities: List<ClientCapability>? = null,
    /** Client type (optional) */
    var clientType: ClientType? = null,
    /** Client name (optional) */
    var clientName: String? = null,
    /** Client version (optional) */
    var clientVersion: Version? = null,
    /** Client connected time (optional) */
    var clientConnectedTime: LocalDateTime? = null,
    /** Registration date (optional) */
    var registrationDate: LocalDateTime? = null,
    /** Avatar MD5 hash (optional) */
    var avatarMD5Hash: ByteArray? = null,
    /** Client IP address (optional) */
    var clientIPAddress: String? = null,
    /** Additional status picture ID (optional) */
    var xStatus: XStatus? = null,
    /** Client operating system name (optional) */
    var clientOperatingSystemName: String? = null,
    /** Client description (optional) */
    var clientDescription: String? = null,
    /** Custom transport status picture ID (optional) */
    var customTransportStatusPictureID: Byte? = null,
    /** Client identification sTLDs defined by transport (optional) */
    var transportClientIdentificationSTLDs: ByteBuffer? = null,
    /** Client host name (optional) */
    var clientHostName: String? = null,
    /** Transport Item ID (optional) */
    var transportItemID: Int? = null
)