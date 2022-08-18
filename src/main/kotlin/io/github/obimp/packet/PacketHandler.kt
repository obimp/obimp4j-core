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
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_TP
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UA
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD

/**
 * @author Alexander Krysin
 */
class PacketHandler {
    private val commonPacketHandler = CommonPacketHandler()
    private val contactListPacketHandler = ContactListPacketHandler()
    private val presencePacketHandler = PresencePacketHandler()
    private val instantMessagingPacketHandler = InstantMessagingPacketHandler()
    private val usersDirectoryPacketHandler = UsersDirectoryPacketHandler()
    private val userAvatarsPacketHandler = UserAvatarsPacketHandler()
    private val fileTransferPacketHandler = FileTransferPacketHandler()
    private val transportsPacketHandler = TransportsPacketHandler()

    fun parsePacket(
        packet: Packet,
        connection: OBIMPConnection,
        username: String,
        password: String
    ) {
        when (packet.type) {
            OBIMP_BEX_COM -> commonPacketHandler.parsePacket(packet, connection, username, password)
            OBIMP_BEX_CL -> contactListPacketHandler.parsePacket(packet, connection)
            OBIMP_BEX_PRES -> presencePacketHandler.parsePacket(packet, connection)
            OBIMP_BEX_IM -> instantMessagingPacketHandler.parsePacket(packet, connection, username)
            OBIMP_BEX_UD -> usersDirectoryPacketHandler.parsePacket(packet, connection)
            OBIMP_BEX_UA -> userAvatarsPacketHandler.parsePacket(packet, connection)
            OBIMP_BEX_FT -> fileTransferPacketHandler.parsePacket(packet, connection)
            OBIMP_BEX_TP -> transportsPacketHandler.parsePacket(packet, connection)
        }
    }
}