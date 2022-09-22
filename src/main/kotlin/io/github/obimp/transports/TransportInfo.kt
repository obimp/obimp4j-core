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

package io.github.obimp.transports

import io.github.obimp.presence.Status

class TransportInfo(
    val uniqueTransportID: String,
    val transportFullName: String,
    val transportShortName: String,
    val nameOfAccountsIDs: String,
    val defaultServerHostOrIP: String,
    val defaultServerPortNumber: Int,
    val supportedPresenceStatuses: List<Status>,
    val supportedXStatus: Boolean,
    val countXStatuses: Int,
    val supportedAddContacts: Boolean,
    val supportedUpdateContacts: Boolean,
    val supportedDeleteContacts: Boolean,
    val supportedVisibleList: Boolean,
    val supportedInvisibleList: Boolean,
    val supportedIgnoreList: Boolean,
    val supportedMoveToIgnoreList: Boolean,
    val supportedAuthorizationRequests: Boolean,
    val supportedRevokeAuthorizationMessages: Boolean,
    val supportedMessageAcknowledges: Boolean,
    val supportedNotificationMessages: Boolean,
    val supportedDetailsRequests: Boolean,
    val supportedUpdateOwnDetails: Boolean,
    val supportedUsersDirectorySearch: Boolean,
    val supportedAvatars: Boolean,
    val supportedUpdateOwnAvatar: Boolean,
    val supportedOfflineMessages: Boolean,
    val supportedPresenceInformationRequests: Boolean,
    val supportedAuthorizationRequestReasonText: Boolean,
    val supportedRevokeAuthorizationReasonText: Boolean,
    val supportedOwnMailboxURLRequest: Boolean,
    val currentResourcesVersion: Int?,
    val statusesPNGFilesURLs: String?,
    val xStatusPicturesPNGFilesURLs: String?,
    val customStatusPicturesPNGFilesURLs: String?,
    val nickname: Boolean?,
    val firstName: Boolean?,
    val lastName: Boolean?,
    val countryCode: Boolean?,
    val regionState: Boolean?,
    val city: Boolean?,
    val zipCode: Boolean?,
    val address: Boolean?,
    val languageCode: Boolean?,
    val additionalLanguageCode: Boolean?,
    val gender: Boolean?,
    val birthday: Boolean?,
    val homepage: Boolean?,
    val about: Boolean?,
    val interests: Boolean?,
    val email: Boolean?,
    val additionEmail: Boolean?,
    val homePhone: Boolean?,
    val workPhone: Boolean?,
    val cellularPhone: Boolean?,
    val faxNumber: Boolean?,
    val company: Boolean?,
    val divisionDepartment: Boolean?,
    val position: Boolean?,
    val searchByAccount: Boolean?,
    val searchByEmail: Boolean?,
    val searchByNickname: Boolean?,
    val searchByFirstName: Boolean?,
    val searchByLastName: Boolean?,
    val searchByGender: Boolean?,
    val searchByAge: Boolean?,
    val searchByCountryCode: Boolean?,
    val searchByCity: Boolean?,
    val searchByLanguageCode: Boolean?,
    val searchByInterests: Boolean?,
    val searchByZodiacSign: Boolean?,
    val searchByOnlineOnly: Boolean?
)
