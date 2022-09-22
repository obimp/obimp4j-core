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
    val requestDetailsResult: RequestDetailsResult,
    val accountName: String,
    val secureEmail: String? = null,
    val hideOnlineStatus: Boolean? = null,
    var nickname: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var country: Country? = null,
    var regionState: String? = null,
    var city: String? = null,
    var zipCode: String? = null,
    var address: String? = null,
    var language: Language? = null,
    var additionalLanguage: Language? = null,
    var gender: Gender? = null,
    var birthday: LocalDateTime? = null,
    var homepage: String? = null,
    var about: String? = null,
    var interests: String? = null,
    var email: String? = null,
    var additionalEmail: String? = null,
    var homePhone: String? = null,
    var workPhone: String? = null,
    var cellularPhone: String? = null,
    var faxNumber: String? = null,
    var company: String? = null,
    var divisionDepartment: String? = null,
    var position: String? = null,
    val transportItemID: Int? = null
)
