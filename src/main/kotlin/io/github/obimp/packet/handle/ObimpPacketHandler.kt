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

package io.github.obimp.packet.handle

import io.github.obimp.connection.Connection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.cl.ContactListPacketHandler
import io.github.obimp.packet.handle.common.CommonPacketHandler
import io.github.obimp.packet.handle.ft.FileTransferPacketHandler
import io.github.obimp.packet.handle.im.InstantMessagingPacketHandler
import io.github.obimp.packet.handle.presence.PresencePacketHandler
import io.github.obimp.packet.handle.transports.TransportsPacketHandler
import io.github.obimp.packet.handle.ua.UserAvatarsPacketHandler
import io.github.obimp.packet.handle.ud.UsersDirectoryPacketHandler

/**
 * @author Alexander Krysin
 */
class ObimpPacketHandler : PacketHandler<WTLD> {
    private val bexTypeToPacketHandler = mapOf(
        Pair(OBIMP_BEX_COM, CommonPacketHandler()),
        Pair(OBIMP_BEX_CL, ContactListPacketHandler()),
        Pair(OBIMP_BEX_PRES, PresencePacketHandler()),
        Pair(OBIMP_BEX_IM, InstantMessagingPacketHandler()),
        Pair(OBIMP_BEX_UD, UsersDirectoryPacketHandler()),
        Pair(OBIMP_BEX_UA, UserAvatarsPacketHandler()),
        Pair(OBIMP_BEX_FT, FileTransferPacketHandler()),
        Pair(OBIMP_BEX_TP, TransportsPacketHandler())
    )

    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        bexTypeToPacketHandler[packet.getType()]?.handlePacket(connection, packet)
    }



    companion object {
        //General BEX types
        const val OBIMP_BEX_COM: Short = 0x0001 // Common
        const val OBIMP_BEX_CL: Short = 0x0002 // Contact list
        const val OBIMP_BEX_PRES: Short = 0x0003 // Presence
        const val OBIMP_BEX_IM: Short = 0x0004 // Instant messaging
        const val OBIMP_BEX_UD: Short = 0x0005 // Users directory
        const val OBIMP_BEX_UA: Short = 0x0006 // User avatars
        const val OBIMP_BEX_FT: Short = 0x0007 // File transfer
        const val OBIMP_BEX_TP: Short = 0x0008 // Transports
    }
}