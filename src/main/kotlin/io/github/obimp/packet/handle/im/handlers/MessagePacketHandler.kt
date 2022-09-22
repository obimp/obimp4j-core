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

package io.github.obimp.packet.handle.im.handlers

import io.github.obimp.connection.Connection
import io.github.obimp.connection.getListeners
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.structure.readDataType
import io.github.obimp.data.type.*
import io.github.obimp.data.type.Byte
import io.github.obimp.im.EncryptionType
import io.github.obimp.im.IncomingMessage
import io.github.obimp.im.MessageType
import io.github.obimp.im.SystemMessagePopupPosition
import io.github.obimp.listener.InstantMessagingListener
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_IM
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.createEmptyPacket
import io.github.obimp.packet.handle.im.InstantMessagingPacketHandler.Companion.OBIMP_BEX_IM_CLI_SRV_MSG_REPORT
import java.time.LocalDateTime

/**
 * @author Alexander Krysin
 */
class MessagePacketHandler : PacketHandler<WTLD> {
    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        val accountName = packet.nextItem().readDataType<UTF8>().value
        val messageId = packet.nextItem().readDataType<LongWord>().value
        val messageType = packet.nextItem().readDataType<LongWord>().value
        val messageData = packet.nextItem().readDataType<BLK>().value
        var requestDeliveryReport = false
        var encryptionType = EncryptionType.DISABLED
        var offlineMessage = false
        var offlineMessageTime: LocalDateTime? = null
        var systemMessage = false
        var systemMessagePopupPosition: SystemMessagePopupPosition? = null
        var multipleMessage = false
        var transportID: Int? = null
        while (packet.hasItems()) {
            val wtld = packet.nextItem()
            when (wtld.getType()) {
                0x0005 -> requestDeliveryReport = true
                0x0006 -> encryptionType = EncryptionType.byType(wtld.readDataType<LongWord>().value)
                0x0007 -> offlineMessage = true
                0x0008 -> offlineMessageTime = wtld.readDataType<DateTime>().value
                0x0009 -> systemMessage = true
                0x000A -> systemMessagePopupPosition =
                    SystemMessagePopupPosition.byValue(wtld.readDataType<Byte>().value)

                0x000B -> multipleMessage = true
                0x1001 -> transportID = wtld.readDataType<LongWord>().value
            }
        }

        for (ml in connection.getListeners<InstantMessagingListener>()) {
            ml.onIncomingMessage(
                IncomingMessage(
                    accountName,
                    messageId,
                    MessageType.byType(messageType),
                    messageData,
                    requestDeliveryReport,
                    encryptionType,
                    offlineMessage,
                    offlineMessageTime,
                    systemMessage,
                    systemMessagePopupPosition,
                    multipleMessage,
                    transportID
                )
            )
        }

        while (packet.hasItems()) {
            val wtld = packet.nextItem()
            when (wtld.getType()) {
                0x0005 -> {
                    val msgReport = createEmptyPacket(OBIMP_BEX_IM, OBIMP_BEX_IM_CLI_SRV_MSG_REPORT)
                    msgReport.body.content.add(WTLD(LongWord(0x0001)).apply { data.add(UTF8(connection.username)) })
                    msgReport.body.content.add(WTLD(LongWord(0x0002)).apply { data.add(LongWord(messageId)) })
                    connection.connectionListener.sendPacket(msgReport)
                }
            }
        }
    }
}