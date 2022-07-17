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

import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author Alexander Krysin
 */
data class UserInfo(
    var accountName: String = "",
    var nickname: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var countryCode: Int = -1,
    var regionState: String = "",
    var city: String = "",
    var zipCode: String = "",
    var address: String = "",
    var languageCode: Int = -1,
    var additionalLanguageCode: Int = -1,
    var gender: Byte = -1,
    var birthday: LocalDateTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC),
    var homepage: String = "",
    var about: String = "",
    var interests: String = "",
    var email: String = "",
    var additionalEmail: String = "",
    var homePhone: String = "",
    var workPhone: String = "",
    var cellularPhone: String = "",
    var faxNumber: String = "",
    var onlineStatus: Boolean = false,
    var company: String = "",
    var divisionDepartment: String = "",
    var position: String = ""
)
