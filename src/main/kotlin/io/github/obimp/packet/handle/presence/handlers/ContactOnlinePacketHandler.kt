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

package io.github.obimp.packet.handle.presence.handlers

import io.github.obimp.connection.Connection
import io.github.obimp.connection.getListeners
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.structure.readDataType
import io.github.obimp.data.structure.readDataTypeList
import io.github.obimp.data.type.*
import io.github.obimp.data.type.Byte
import io.github.obimp.listener.PresenceInfoListener
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.presence.*
import io.github.obimp.util.Version
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */
class ContactOnlinePacketHandler : PacketHandler<WTLD> {
    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        val accountName = packet.nextItem().readDataType<UTF8>().value
        val statusValue = packet.nextItem().readDataType<LongWord>().value
        val onlineContact = OnlineContactInfo(accountName, Status.byValue(statusValue))
        while (packet.hasItems()) {
            val wtld = packet.nextItem()
            when (wtld.getType()) {
                0x0003 -> onlineContact.statusName = wtld.readDataType<UTF8>().value
                0x0004 -> onlineContact.xStatusNumber = wtld.readDataType<LongWord>().value
                0x0005 -> onlineContact.xStatusDescription = wtld.readDataType<UTF8>().value
                0x0006 -> {
                    val clientCapabilities = mutableListOf<ClientCapability>()
                    val caps = wtld.readDataTypeList<Word>()
                    caps.forEach {
                        when (it.value.toInt()) {
                            0x0001 -> clientCapabilities.add(ClientCapability.MSGS_UTF8)
                            0x0002 -> clientCapabilities.add(ClientCapability.MSGS_RTF)
                            0x0003 -> clientCapabilities.add(ClientCapability.MSGS_HTML)
                            0x0004 -> clientCapabilities.add(ClientCapability.MSGS_ENCRYPT)
                            0x0005 -> clientCapabilities.add(ClientCapability.NOTIFS_TYPING)
                            0x0006 -> clientCapabilities.add(ClientCapability.AVATARS)
                            0x0007 -> clientCapabilities.add(ClientCapability.FILE_TRANSFER)
                            0x0008 -> clientCapabilities.add(ClientCapability.TRANSPORTS)
                            0x0009 -> clientCapabilities.add(ClientCapability.NOTIFS_ALARM)
                            0x000A -> clientCapabilities.add(ClientCapability.NOTIFS_MAIL)
                        }
                    }
                    onlineContact.clientCapabilities = clientCapabilities
                }
                0x0007 -> onlineContact.clientType = ClientType.byValue(wtld.readDataType<Word>().value)
                0x0008 -> onlineContact.clientName = wtld.readDataType<UTF8>().value
                0x0009 -> {
                    val version = ByteBuffer.allocate(8)
                    version.putLong(wtld.readDataType<QuadWord>().value)
                    version.rewind()
                    val major = version.short
                    val minor = version.short
                    val release = version.short
                    val build = version.short
                    onlineContact.clientVersion = Version(major.toInt(), minor.toInt(), release.toInt(), build.toInt())
                }
                0x000A -> onlineContact.clientConnectedTime = wtld.readDataType<DateTime>().value
                0x000B -> onlineContact.registrationDate = wtld.readDataType<DateTime>().value
                0x000C -> onlineContact.avatarMD5Hash = wtld.readDataType<OctaWord>().value.array()
                0x000D -> onlineContact.clientIPAddress = wtld.readDataType<UTF8>().value
                0x000E -> onlineContact.xStatus = XStatus.byUUID(wtld.readDataType<UUID>().value)
                0x000F -> onlineContact.clientOperatingSystemName = wtld.readDataType<UTF8>().value
                0x0010 -> onlineContact.clientDescription = wtld.readDataType<UTF8>().value
                0x0011 -> onlineContact.customTransportStatusPictureID = wtld.readDataType<Byte>().value
                0x0012 -> onlineContact.transportClientIdentificationSTLDs = wtld.readDataType<BLK>().value
                0x0013 -> onlineContact.clientHostName = wtld.readDataType<UTF8>().value
                0x1001 -> onlineContact.transportItemID = wtld.readDataType<LongWord>().value
            }
        }
        for (usl in connection.getListeners<PresenceInfoListener>()) {
            usl.onContactOnline(onlineContact)
        }
    }
}