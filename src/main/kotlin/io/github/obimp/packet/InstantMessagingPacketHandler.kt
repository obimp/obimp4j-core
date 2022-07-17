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
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.BLK
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM_CLI_DEL_OFFLINE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REQ
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM_CLI_SRV_MSG_REPORT
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM_CLI_SRV_NOTIFY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM_SRV_DONE_OFFLINE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM_SRV_MESSAGE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM_SRV_PARAMS_REPLY

/**
 * @author Alexander Krysin
 */
class InstantMessagingPacketHandler {
    fun parsePacket(packet: Packet, connection: OBIMPConnection, username: String) {
        when (packet.subtype) {
            OBIMP_BEX_IM_SRV_PARAMS_REPLY -> {}
            OBIMP_BEX_IM_SRV_DONE_OFFLINE -> {
                connection.send(Packet(OBIMP_BEX_IM, OBIMP_BEX_IM_CLI_DEL_OFFLINE))
            }
            OBIMP_BEX_IM_SRV_MESSAGE -> {
                val accountName = packet.getWTLD().getDataType<UTF8>().value
                val messageId = packet.getWTLD().getDataType<LongWord>().value
                val messageType = packet.getWTLD().getDataType<LongWord>().value
                val messageData = packet.getWTLD().getDataType<BLK>().data.decodeToString()

                for (ml in connection.messageListeners) {
                    ml.onIncomingMessage(accountName, messageId, messageType, messageData)
                }

                while (packet.isNotEmpty()) {
                    val wtld = packet.getWTLD()
                    when (wtld.type) {
                        0x0005 -> {
                            val msgReport = Packet(OBIMP_BEX_IM, OBIMP_BEX_IM_CLI_SRV_MSG_REPORT)
                            msgReport.addWTLD(WTLD(0x0001, UTF8(username)))
                            msgReport.addWTLD(WTLD(0x0002, LongWord(messageId)))
                            connection.send(msgReport)
                        }
                    }
                }
            }
            OBIMP_BEX_IM_CLI_SRV_MSG_REPORT -> {
                val accountName = packet.getWTLD().getDataType<UTF8>().value
                val messageId = packet.getWTLD().getDataType<LongWord>().value

                for (ml in connection.messageListeners) {
                    ml.onMessageDelivered(accountName, messageId)
                }
            }
            OBIMP_BEX_IM_CLI_SRV_NOTIFY -> {
                val accountName = packet.getWTLD().getDataType<UTF8>().value
                val notificationType = packet.getWTLD().getDataType<LongWord>().value
                val notificationValue = packet.getWTLD().getDataType<LongWord>().value

                for (ml in connection.messageListeners) {
                    ml.onNotify(accountName, notificationType, notificationValue)
                }
            }
            OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REQ -> {}
            OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY -> {}
        }
    }
}