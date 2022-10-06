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

/**
 * Presence info
 * @author Alexander Krysin
 */
class PresenceInfo(
    /** Client capabilities */
    val clientCapabilities: List<ClientCapability>,
    /** Client type */
    val clientType: ClientType,
    /** Client name */
    val clientName: String,
    /** Client version */
    val clientVersion: Version,
    /** Client language */
    val clientLanguage: Language,
    /** Client operating system name (optional) (maximal UTF08 encoded length is 128) */
    val clientOperatingSystemName: String?,
    /** Client description (optional) (maximal UTF08 encoded length is 512) */
    val clientDescription: String?,
    /** Client flags for presence information (optional) */
    val clientFlag: ClientFlag?,
    /** Client host name (optional) (maximal UTF-8 encoded length is 255) */
    val clientHostName: String?
)