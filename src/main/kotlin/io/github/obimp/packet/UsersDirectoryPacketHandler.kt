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

package io.github.obimp.packet

import io.github.obimp.OBIMPConnection
import io.github.obimp.data.type.Byte
import io.github.obimp.data.type.DateTime
import io.github.obimp.data.type.UTF8
import io.github.obimp.data.type.Word
import io.github.obimp.listener.MetaInfoListener
import io.github.obimp.ud.SearchResult
import io.github.obimp.ud.UserInfo
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD_SRV_PARAMS_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD_SRV_SEARCH_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY
import java.time.OffsetDateTime

/**
 * @author Alexander Krysin
 */
class UsersDirectoryPacketHandler {
    fun parsePacket(packet: Packet, connection: OBIMPConnection) {
        when (packet.subtype) {
            OBIMP_BEX_UD_SRV_PARAMS_REPLY -> {}
            OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY -> {
                val resultCode = packet.getWTLD().getDataType<Word>().value
                val accountName = packet.getWTLD().getDataType<UTF8>().value

                if (resultCode == DETAILS_RES_SUCCESS) {
                    val userInfo = UserInfo(accountName = accountName)

                    while (packet.isNotEmpty()) {
                        val wtld = packet.getWTLD()
                        when (wtld.type) {
                            0x0004 -> userInfo.nickname = wtld.getDataType<UTF8>().value
                            0x0005 -> userInfo.firstName = wtld.getDataType<UTF8>().value
                            0x0006 -> userInfo.lastName = wtld.getDataType<UTF8>().value
                            0x0007 -> userInfo.countryCode = wtld.getDataType<Word>().value
                            0x0008 -> userInfo.regionState = wtld.getDataType<UTF8>().value
                            0x0009 -> userInfo.city = wtld.getDataType<UTF8>().value
                            0x000A -> userInfo.zipCode = wtld.getDataType<UTF8>().value
                            0x000B -> userInfo.address = wtld.getDataType<UTF8>().value
                            0x000C -> userInfo.languageCode = wtld.getDataType<Word>().value
                            0x000D -> userInfo.additionalLanguageCode = wtld.getDataType<Word>().value
                            0x000E -> userInfo.gender = wtld.getDataType<Byte>().value
                            0x000F -> userInfo.birthday =
                                wtld.getDataType<DateTime>().getDateTime(OffsetDateTime.now().offset)
                            0x0010 -> userInfo.homepage = wtld.getDataType<UTF8>().value
                            0x0011 -> userInfo.about = wtld.getDataType<UTF8>().value
                            0x0012 -> userInfo.interests = wtld.getDataType<UTF8>().value
                            0x0013 -> userInfo.email = wtld.getDataType<UTF8>().value
                            0x0014 -> userInfo.additionalEmail = wtld.getDataType<UTF8>().value
                            0x0015 -> userInfo.homePhone = wtld.getDataType<UTF8>().value
                            0x0016 -> userInfo.workPhone = wtld.getDataType<UTF8>().value
                            0x0017 -> userInfo.cellularPhone = wtld.getDataType<UTF8>().value
                            0x0018 -> userInfo.faxNumber = wtld.getDataType<UTF8>().value
                            0x001A -> userInfo.company = wtld.getDataType<UTF8>().value
                            0x001B -> userInfo.divisionDepartment = wtld.getDataType<UTF8>().value
                            0x001C -> userInfo.position = wtld.getDataType<UTF8>().value
                        }
                    }

                    for (ml in connection.metaInfoListeners) {
                        ml.onUserInfo(userInfo)
                    }
                }
            }
            OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY -> {
                val resultCode = packet.getWTLD().getDataType<Word>().value

                for (ml in connection.metaInfoListeners) {
                    ml.onDetailsUpdated(resultCode)
                }
            }
            OBIMP_BEX_UD_SRV_SEARCH_REPLY -> {
                val resultCode = packet.getWTLD().getDataType<Word>().value

                if (resultCode == SEARCH_RES_SUCCESS) {
                    val searchResult = SearchResult()

                    while (packet.isNotEmpty()) {
                        val wtld = packet.getWTLD()

                        when (wtld.type) {
                            0x0002 -> searchResult.accountName = wtld.getDataType<UTF8>().value
                            0x0003 -> searchResult.nickname = wtld.getDataType<UTF8>().value
                            0x0004 -> searchResult.firstName = wtld.getDataType<UTF8>().value
                            0x0005 -> searchResult.lastName = wtld.getDataType<UTF8>().value
                            0x0006 -> searchResult.gender = wtld.getDataType<Byte>().value
                            0x0007 -> searchResult.age = wtld.getDataType<Byte>().value
                            0x0008 -> searchResult.isOnline = true
                        }
                    }

                    for (ml in connection.metaInfoListeners) {
                        ml.onSearchResult(searchResult)
                    }
                }
            }
            OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY -> {
                val resultCode = packet.getWTLD().getDataType<Word>().value

                if (resultCode == UPD_SECURE_RES_SUCCESS) {
                    connection.metaInfoListeners.forEach(MetaInfoListener::onSecureUpdated)
                }
            }
        }
    }

    companion object {
        const val DETAILS_RES_SUCCESS = 0x0000
        const val DETAILS_RES_NOT_FOUND = 0x0001
        const val DETAILS_RES_TOO_MANY_REQUESTS = 0x0002
        const val DETAILS_RES_SERVICE_TEMP_UNAVAILABLE = 0x0003

        const val SEARCH_RES_SUCCESS = 0x0000
        const val SEARCH_RES_NOT_FOUND = 0x0001
        const val SEARCH_RES_BAD_REQUEST = 0x0002
        const val SEARCH_RES_TOO_MANY_REQUESTS = 0x0003
        const val SEARCH_RES_SERVICE_TEMP_UNAVAILABLE = 0x0004

        const val UPD_SECURE_RES_SUCCESS = 0x0000
        const val UPD_SECURE_RES_BAD_REQUEST = 0x0001
        const val UPD_SECURE_RES_SERVICE_TEMP_UNAVAILABLE = 0x0002
        const val UPD_SECURE_RES_NOT_ALLOWED = 0x0003
    }
}