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

/**
 * Transport item
 * @author Alexander Krysin
 */
class TransportItem(
    /** Unique transport ID */
    val uniqueTransportID: String,
    /** Transport full name */
    val transportFullName: String,
    /** Transport short name */
    val transportShortName: String,
    /** Name of accounts IDs */
    val nameOfAccountsIDs: String,
    /** Default server host / IP */
    val defaultServerHostOrIP: String,
    /** Default server port number */
    val defaultServerPortNumber: Int,
    /** Supported OBIMP presence statuses array (excluding "online" status, supported by default) */
    val supportedPresenceStatuses: List<Status>,
    /** Additional status pictures supported */
    val supportedXStatus: Boolean,
    /** Additional status pictures count */
    val countXStatuses: Int,
    /** Add contacts supported */
    val supportedAddContacts: Boolean,
    /** Update contacts supported */
    val supportedUpdateContacts: Boolean,
    /** Delete contacts supported */
    val supportedDeleteContacts: Boolean,
    /** Visible list supported */
    val supportedVisibleList: Boolean,
    /** Invisible list supported */
    val supportedInvisibleList: Boolean,
    /** Ignore list supported */
    val supportedIgnoreList: Boolean,
    /** Move to ignore list supported */
    val supportedMoveToIgnoreList: Boolean,
    /** Authorization requests supported */
    val supportedAuthorizationRequests: Boolean,
    /** Revoke authorization messages supported */
    val supportedRevokeAuthorizationMessages: Boolean,
    /** Message acknowledges supported */
    val supportedMessageAcknowledges: Boolean,
    /** Notification messages supported */
    val supportedNotificationMessages: Boolean,
    /** Details requests supported */
    val supportedDetailsRequests: Boolean,
    /** Update own details supported */
    val supportedUpdateOwnDetails: Boolean,
    /** Users directory search supported */
    val supportedUsersDirectorySearch: Boolean,
    /** Avatars supported */
    val supportedAvatars: Boolean,
    /** Update own avatar supported */
    val supportedUpdateOwnAvatar: Boolean,
    /** Offline messages supported */
    val supportedOfflineMessages: Boolean,
    /** Presence information requests supported */
    val supportedPresenceInformationRequests: Boolean,
    /** Authorization request reason text supported */
    val supportedAuthorizationRequestReasonText: Boolean,
    /** Revoke authorization reason text supported */
    val supportedRevokeAuthorizationReasonText: Boolean,
    /** Own mailbox URL request supported */
    val supportedOwnMailboxURLRequest: Boolean,
    /** Current resources version (if changed, then resources should be re-downloaded) */
    val currentResourcesVersion: Int? = null,
    /** Statuses PNG files URLs */
    val statusesPNGFilesURLs: String? = null,
    /** Additional status pictures PNG files URLs */
    val xStatusPicturesPNGFilesURLs: String? = null,
    /** Custom status pictures PNG files uRLs */
    val customStatusPicturesPNGFilesURLs: String? = null,
    /** Nickname update supported */
    val nickname: Boolean? = null,
    /** First name update supported */
    val firstName: Boolean? = null,
    /** Last name update supported */
    val lastName: Boolean? = null,
    /** Country update supported */
    val countryCode: Boolean? = null,
    /** Region / state update supported */
    val regionState: Boolean? = null,
    /** City update supported */
    val city: Boolean? = null,
    /** ZIP Code update supported */
    val zipCode: Boolean? = null,
    /** Address update supported */
    val address: Boolean? = null,
    /** Language update supported */
    val languageCode: Boolean? = null,
    /** Additional language update supported */
    val additionalLanguageCode: Boolean? = null,
    /** Gender update supported */
    val gender: Boolean? = null,
    /** Birthday update supported */
    val birthday: Boolean? = null,
    /** Homepage update supported */
    val homepage: Boolean? = null,
    /** About update supported */
    val about: Boolean? = null,
    /** Interests update supported */
    val interests: Boolean? = null,
    /** Email update supported */
    val email: Boolean? = null,
    /** Additional email update supported */
    val additionalEmail: Boolean? = null,
    /** Home phone update supported */
    val homePhone: Boolean? = null,
    /** Work phone update supported */
    val workPhone: Boolean? = null,
    /** Cellular phone update supported */
    val cellularPhone: Boolean? = null,
    /** Fax number update supported */
    val faxNumber: Boolean? = null,
    /** Company update supported */
    val company: Boolean? = null,
    /** Division / department update supported */
    val divisionDepartment: Boolean? = null,
    /** Position update supported */
    val position: Boolean? = null,
    /** Search by account supported */
    val searchByAccount: Boolean? = null,
    /** Search by email supported */
    val searchByEmail: Boolean? = null,
    /** Search by nickname supported */
    val searchByNickname: Boolean? = null,
    /** Search by first name supported */
    val searchByFirstName: Boolean? = null,
    /** Search by last name supported */
    val searchByLastName: Boolean? = null,
    /** Search by gender supported */
    val searchByGender: Boolean? = null,
    /** Search by age supported */
    val searchByAge: Boolean? = null,
    /** Search by country supported */
    val searchByCountryCode: Boolean? = null,
    /** Search by city supported */
    val searchByCity: Boolean? = null,
    /** Search by language supported */
    val searchByLanguageCode: Boolean? = null,
    /** Search by interests supported */
    val searchByInterests: Boolean? = null,
    /** Search by zodiac sign supported */
    val searchByZodiacSign: Boolean? = null,
    /** Search by online only supported */
    val searchByOnlineOnly: Boolean? = null
)
