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

package io.github.obimp

import io.github.obimp.data.structure.STLD
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.*
import io.github.obimp.data.type.Byte
import io.github.obimp.ud.UserInfo
import io.github.obimp.packet.Packet
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_ADD_ITEM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_DEL_ITEM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_UPD_ITEM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM_CLI_MESSAGE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_CLI_SET_STATUS
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UA
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UA_CLI_AVATAR_REQ
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD_CLI_DETAILS_REQ
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD_CLI_DETAILS_UPD
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD_CLI_SEARCH
import java.time.OffsetDateTime

/**
 * OBIMP API
 * @author Alexander Krysin
 */
class OBIMP {
    companion object {
        private var messageId = 1
            get() = field++

        /**
         * Set status
         * @param connection Connection
         * @param status Status
         * @param statusMessage Status message
         * @param xStatus X-Status
         * @param xStatusMessage X-Status message
         */
        fun setStatus(
            connection: OBIMPConnection, status: Int, statusMessage: String, xStatus: Int, xStatusMessage: String
        ) {
            val setStatus = Packet(OBIMP_BEX_PRES, OBIMP_BEX_PRES_CLI_SET_STATUS)
            setStatus.addWTLD(WTLD(0x0001, LongWord(status)))
            setStatus.addWTLD(WTLD(0x0002, UTF8(statusMessage)))
            setStatus.addWTLD(WTLD(0x0003, LongWord(xStatus)))
            setStatus.addWTLD(WTLD(0x0004, UTF8(xStatusMessage)))
            connection.sendPacket(setStatus)
        }

        /**
         * Send message
         * @param connection Connection
         * @param accountName Contact ID
         * @param text Message text
         */
        fun sendMessage(connection: OBIMPConnection, accountName: String, text: String) {
            val message = Packet(OBIMP_BEX_IM, OBIMP_BEX_IM_CLI_MESSAGE)
            message.addWTLD(WTLD(0x0001, UTF8(accountName)))
            message.addWTLD(WTLD(0x0002, LongWord(messageId)))
            message.addWTLD(WTLD(0x0003, LongWord(1)))
            message.addWTLD(WTLD(0x0004, BLK(text.encodeToByteArray())))
            message.addWTLD(WTLD(0x0005))
            connection.sendPacket(message)
        }

        fun getUserInfo(connection: OBIMPConnection, accountName: String) {
            val inf = Packet(OBIMP_BEX_UD, OBIMP_BEX_UD_CLI_DETAILS_REQ)
            inf.addWTLD(WTLD(0x0001, UTF8(accountName)))
            connection.sendPacket(inf)
        }

        fun updateUserInfo(connection: OBIMPConnection, userInfo: UserInfo) {
            val updInf = Packet(OBIMP_BEX_UD, OBIMP_BEX_UD_CLI_DETAILS_UPD)
            updInf.addWTLD(WTLD(0x0001, UTF8(userInfo.accountName)))
            updInf.addWTLD(WTLD(0x0002, UTF8(userInfo.nickname)))
            updInf.addWTLD(WTLD(0x0003, UTF8(userInfo.firstName)))
            updInf.addWTLD(WTLD(0x0004, UTF8(userInfo.lastName)))
            if (userInfo.countryCode != -1) updInf.addWTLD(WTLD(0x0005, Word(userInfo.countryCode)))
            updInf.addWTLD(WTLD(0x0006, UTF8(userInfo.regionState)))
            updInf.addWTLD(WTLD(0x0007, UTF8(userInfo.city)))
            updInf.addWTLD(WTLD(0x0008, UTF8(userInfo.zipCode)))
            updInf.addWTLD(WTLD(0x0009, UTF8(userInfo.address)))
            if (userInfo.languageCode != -1) updInf.addWTLD(WTLD(0x000A, Word(userInfo.languageCode)))
            if (userInfo.additionalLanguageCode != -1) updInf.addWTLD(
                WTLD(
                    0x000B, Word(userInfo.additionalLanguageCode)
                )
            )
            if (userInfo.gender.toInt() != -1) {
                updInf.addWTLD(
                    WTLD(
                        0x000C,
                        Byte(if (userInfo.gender.toInt() != 0x01 && userInfo.gender.toInt() != 0x02) 0x00 else userInfo.gender)
                    )
                )
            }
            updInf.addWTLD(WTLD(0x000D, DateTime(userInfo.birthday.toEpochSecond(OffsetDateTime.now().offset))));
            updInf.addWTLD(WTLD(0x000E, UTF8(userInfo.homepage)))
            updInf.addWTLD(WTLD(0x000F, UTF8(userInfo.about)))
            updInf.addWTLD(WTLD(0x0010, UTF8(userInfo.interests)))
            updInf.addWTLD(WTLD(0x0011, UTF8(userInfo.email)))
            updInf.addWTLD(WTLD(0x0012, UTF8(userInfo.additionalEmail)))
            updInf.addWTLD(WTLD(0x0013, UTF8(userInfo.homePhone)))
            updInf.addWTLD(WTLD(0x0014, UTF8(userInfo.workPhone)))
            updInf.addWTLD(WTLD(0x0015, UTF8(userInfo.cellularPhone)))
            updInf.addWTLD(WTLD(0x0016, UTF8(userInfo.faxNumber)))
            updInf.addWTLD(WTLD(0x0017, Bool(userInfo.onlineStatus)))
            updInf.addWTLD(WTLD(0x0018, UTF8(userInfo.company)))
            updInf.addWTLD(WTLD(0x0019, UTF8(userInfo.divisionDepartment)))
            updInf.addWTLD(WTLD(0x001A, UTF8(userInfo.position)))
            connection.sendPacket(updInf)
        }

        fun search(connection: OBIMPConnection, obj: Any, typeSearch: Int) {
            val search = Packet(OBIMP_BEX_UD, OBIMP_BEX_UD_CLI_SEARCH)
            when (typeSearch) {
                1 -> search.addWTLD(WTLD(0x0001, UTF8(obj.toString())))
                2 -> search.addWTLD(WTLD(0x0003, UTF8(obj.toString())))
                3 -> search.addWTLD(WTLD(0x0004, UTF8(obj.toString())))
                4 -> search.addWTLD(WTLD(0x0005, UTF8(obj.toString())))
                5 -> search.addWTLD(WTLD(0x0009, Byte(obj.toString().toByte())))
                6 -> search.addWTLD(WTLD(0x0007, UTF8(obj.toString())))
                7 -> search.addWTLD(WTLD(0x0006, Word(obj.toString().toInt())))
                8 -> search.addWTLD(WTLD(0x000C, Byte(obj.toString().toByte())))
                9 -> {
                    search.addWTLD(WTLD(0x000A,
                        Byte(obj.toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[0].toByte())))
                    search.addWTLD(WTLD(0x000B,
                        Byte(obj.toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[1].toByte())))
                }
                10 -> search.addWTLD(WTLD(0x0008, Word(obj.toString().toInt())))
                11 -> search.addWTLD(WTLD(0x000D, UTF8(obj.toString())))
            }
            connection.sendPacket(search)
        }

        fun addGroup(connection: OBIMPConnection, groupName: String, parentGroupId: Int) {
            val addGroup = Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_ADD_ITEM)
            addGroup.addWTLD(WTLD(0x0001, Word(0x0001)))
            addGroup.addWTLD(WTLD(0x0002, LongWord(parentGroupId)))
            addGroup.addWTLD(WTLD(0x0003, STLD(0x0001, UTF8(groupName))))
            connection.sendPacket(addGroup)
        }

        fun updateGroup(connection: OBIMPConnection, groupName: String, groupID: Int) {
            val updGroup = Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_UPD_ITEM)
            updGroup.addWTLD(WTLD(0x0001, LongWord(groupID)))
            updGroup.addWTLD(WTLD(0x0002, LongWord(0)))
            updGroup.addWTLD(WTLD(0x0003, STLD(0x0001, UTF8(groupName))))
            connection.sendPacket(updGroup)
        }

        fun deleteGroup(connection: OBIMPConnection, groupID: Int) {
            val delGroup = Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_DEL_ITEM)
            delGroup.addWTLD(WTLD(0x0001, LongWord(groupID)))
            connection.sendPacket(delGroup)
        }

        fun authReply(connection: OBIMPConnection, accountName: String, auth: Boolean) {
            val authReply = Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY)
            authReply.addWTLD(WTLD(0x0001, UTF8(accountName)))
            authReply.addWTLD(WTLD(0x0002, Word(if (auth) 0x0001 else 0x0002)))
            connection.sendPacket(authReply)
        }

        fun authRequest(
            connection: OBIMPConnection,
            accountName: String,
            reason: String = "Разрешите добавить Вас в мой список контактов."
        ) {
            val authRequest = Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST)
            authRequest.addWTLD(WTLD(0x0001, UTF8(accountName)))
            authRequest.addWTLD(WTLD(0x0002, UTF8(reason)))
            connection.sendPacket(authRequest)
        }

        fun authRevoke(connection: OBIMPConnection, accountName: String, reason: String = "Авторизация отозвана.") {
            val authRevoke = Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE)
            authRevoke.addWTLD(WTLD(0x0001, UTF8(accountName)))
            authRevoke.addWTLD(WTLD(0x0002, UTF8(reason)))
            connection.sendPacket(authRevoke)
        }

        fun requestAvatar(connection: OBIMPConnection, avatarMd5Hash: String) {
            val requestAvatar = Packet(OBIMP_BEX_UA, OBIMP_BEX_UA_CLI_AVATAR_REQ)
            requestAvatar.addWTLD(WTLD(0x0001, OctaWord(avatarMd5Hash.encodeToByteArray())))
            connection.sendPacket(requestAvatar)
        }
    }
}