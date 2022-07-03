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

package io.github.obimp.meta

/**
 * @author Alexander Krysin
 */
data class UserInfo(
    val accountName: String = "",
    val nickname: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val countryCode: Int = -1,
    val regionState: String = "",
    val city: String = "",
    val zipCode: String = "",
    val address: String = "",
    val languageCode: Int = -1,
    val additionalLanguageCode: Int = -1,
    val gender: Byte = -1,
    val birthday: Long = 0,
    val homepage: String = "",
    val about: String = "",
    val interests: String = "",
    val email: String = "",
    val additionalEmail: String = "",
    val homePhone: String = "",
    val workPhone: String = "",
    val cellularPhone: String = "",
    val faxNumber: String = "",
    val onlineStatus: Boolean = true,
    val company: String = "",
    val divisionDepartment: String = "",
    val position: String = ""
)
