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
import io.github.obimp.cl.*
import io.github.obimp.data.type.*
import io.github.obimp.data.type.Byte
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_REQUEST
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_CONTACT_ONLINE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_MAIL_NOTIF
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_OWN_MAIL_URL
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_PARAMS_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_PRES_INFO
import io.github.obimp.toInt

/**
 * @author Alexander Krysin
 */
class PresencePacketHandler {
    fun parsePacket(packet: Packet, connection: OBIMPConnection) {
        when (packet.subtype) {
            OBIMP_BEX_PRES_SRV_PARAMS_REPLY -> {}
            OBIMP_BEX_PRES_SRV_CONTACT_ONLINE -> {
                val accountName = packet.getWTLD().getDataType<UTF8>().value
                val statusValue = packet.getWTLD().getDataType<LongWord>().value
                val onlineContact = OnlineContact(accountName, statusValue)
                while (packet.isNotEmpty()) {
                    val wtld = packet.getWTLD()
                    when (wtld.type) {
                        0x0003 -> onlineContact.statusText = wtld.getDataType<UTF8>().value
                        0x0004 -> onlineContact.xStatusValue = wtld.getDataType<LongWord>().value
                        0x0005 -> onlineContact.xStatusText = wtld.getDataType<UTF8>().value
                        0x0006 -> {
                            val clientCapabilities = ClientCapabilities()
                            val caps = wtld.getDataTypeList<Word>()
                            caps.forEach {
                                when (it.value) {
                                    0x0001 -> clientCapabilities.msgUtf8Support = true
                                    0x0002 -> clientCapabilities.msgRtfSupport = true
                                    0x0003 -> clientCapabilities.msgHtmlSupport = true
                                    0x0004 -> clientCapabilities.msgEncryptSupport = true
                                    0x0005 -> clientCapabilities.notifTypingSupport = true
                                    0x0006 -> clientCapabilities.avatarsSupport = true
                                    0x0007 -> clientCapabilities.fileTransferSupport = true
                                    0x0008 -> clientCapabilities.transportsSupport = true
                                    0x0009 -> clientCapabilities.notifAlarmSupport = true
                                    0x000A -> clientCapabilities.notifMailSupport = true
                                }
                            }
                            onlineContact.clientCapabilities = clientCapabilities
                        }
                        0x0007 -> onlineContact.clientType = when (wtld.getDataType<Word>().value) {
                            0x0001 -> ClientType.USER
                            0x0002 -> ClientType.BOT
                            else -> ClientType.SERVICE
                        }
                        0x0008 -> onlineContact.clientName = wtld.getDataType<UTF8>().value
                        0x0009 -> {
                            val version = wtld.getDataType<QuadWord>().data
                            val major = version.take(2).toInt()
                            version.drop(2)
                            val minor = version.take(2).toInt()
                            version.drop(2)
                            val release = version.take(2).toInt()
                            val build = version.takeLast(2).toInt()
                            onlineContact.clientVersion = ClientVersion(major, minor, release, build)
                        }
                        0x000A -> onlineContact.clientConnectedTime = wtld.getDataType<DateTime>().getDateTime()
                        0x000B -> onlineContact.registrationDate = wtld.getDataType<DateTime>().getDateTime()
                        0x000C -> onlineContact.avatarMD5Hash = wtld.getDataType<OctaWord>().data.decodeToString()
                        0x000D -> onlineContact.clientIpAddress = wtld.getDataType<UTF8>().value
                        0x000E -> onlineContact.xStatusPictureId = wtld.getDataType<UUID>().value
                        0x000F -> onlineContact.operatingSystemName = wtld.getDataType<UTF8>().value
                        0x0010 -> onlineContact.clientDescription = wtld.getDataType<UTF8>().value
                        0x0011 -> onlineContact.customTransportStatusPictureId = wtld.getDataType<Byte>().value
                        0x0012 -> onlineContact.clientIdentification =
                            ClientIdentification(wtld.getDataType<BLK>().data.asList())
                        0x0013 -> onlineContact.clientHostName = wtld.getDataType<UTF8>().value
                        0x1001 -> onlineContact.transportItemId = wtld.getDataType<LongWord>().value
                    }
                }
                for (usl in connection.userStatusListeners) {
                    usl.onUserOnline(onlineContact)
                }
            }

            OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE -> {
                val accountName = packet.getWTLD().getDataType<UTF8>().value
                for (usl in connection.userStatusListeners) {
                    usl.onUserOffline(accountName)
                }
            }

            OBIMP_BEX_PRES_SRV_PRES_INFO -> {
                connection.sendPacket(Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_REQUEST))
            }

            OBIMP_BEX_PRES_SRV_MAIL_NOTIF -> {}
            OBIMP_BEX_PRES_SRV_OWN_MAIL_URL -> {}
        }
    }
}