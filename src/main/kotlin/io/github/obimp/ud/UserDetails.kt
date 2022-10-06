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

package io.github.obimp.ud

import io.github.obimp.presence.Language
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author Alexander Krysin
 */
data class UserDetails(
    /** Account name */
    val accountName: String,
    /** Secure email (only for client's account) */
    val secureEmail: String? = null,
    /** Online status will not be shown in users directory (only for client's account) */
    val hideOnlineStatus: Boolean? = null,
    /** Nickname (optional) */
    var nickname: String? = null,
    /** First name (optional) */
    var firstName: String? = null,
    /** Last name (optional) */
    var lastName: String? = null,
    /** Country (optional) */
    var country: Country? = null,
    /** Region / state (optional) */
    var regionState: String? = null,
    /** City (optional) */
    var city: String? = null,
    /** ZIP Code (optional) */
    var zipCode: String? = null,
    /** Address (optional) */
    var address: String? = null,
    /** Language (optional) */
    var language: Language? = null,
    /** Additional language (optional) */
    var additionalLanguage: Language? = null,
    /** Gender (optional) */
    var gender: Gender? = null,
    /** Birthday (optional) */
    var birthday: LocalDateTime? = null,
    /** Homepage (optional) */
    var homepage: String? = null,
    /** About (optional) */
    var about: String? = null,
    /** Interests (optional) */
    var interests: String? = null,
    /** Email (optional) */
    var email: String? = null,
    /** Additional email (optional) */
    var additionalEmail: String? = null,
    /** Home phone (optional) */
    var homePhone: String? = null,
    /** Work phone (optional) */
    var workPhone: String? = null,
    /** Cellular phone (optional) */
    var cellularPhone: String? = null,
    /** Fax number (optional) */
    var faxNumber: String? = null,
    /** Company (optional) */
    var company: String? = null,
    /** Division / department (optional) */
    var divisionDepartment: String? = null,
    /** Position (optional) */
    var position: String? = null,
    /** Transport Item ID (optional) */
    val transportItemID: Int? = null
)
