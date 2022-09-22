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

package io.github.obimp.packet.handle.common

import io.github.obimp.connection.Connection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.common.handlers.*

/**
 * @author Alexander Krysin
 */
class CommonPacketHandler : PacketHandler<WTLD> {
    private val bexSubtypeToPacketHandler = mapOf(
        Pair(OBIMP_BEX_COM_SRV_HELLO, HelloPacketHandler()),
        Pair(OBIMP_BEX_COM_SRV_LOGIN_REPLY, LoginReplyPacketHandler()),
        Pair(OBIMP_BEX_COM_SRV_BYE, ByePacketHandler()),
        Pair(OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING, KeepalivePingPacketHandler()),
        Pair(OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG, KeepalivePongPacketHandler()),
        Pair(OBIMP_BEX_COM_SRV_REGISTER_REPLY, RegisterReplyPacketHandler())
    )

    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        bexSubtypeToPacketHandler[packet.getSubtype()]?.handlePacket(connection, packet)
    }

    companion object {
        //BEX 0x0001, Common. (OBIMP_BEX_COM)
        const val OBIMP_BEX_COM_CLI_HELLO: Short = 0x0001
        const val OBIMP_BEX_COM_SRV_HELLO: Short = 0x0002
        const val OBIMP_BEX_COM_CLI_LOGIN: Short = 0x0003
        const val OBIMP_BEX_COM_SRV_LOGIN_REPLY: Short = 0x0004
        const val OBIMP_BEX_COM_SRV_BYE: Short = 0x0005
        const val OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING: Short = 0x0006
        const val OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG: Short = 0x0007
        const val OBIMP_BEX_COM_CLI_REGISTER: Short = 0x0008
        const val OBIMP_BEX_COM_SRV_REGISTER_REPLY: Short = 0x0009
    }
}