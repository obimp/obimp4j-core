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

package io.github.obimp.packet.handle.ud

import io.github.obimp.connection.Connection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.ud.handlers.*

/**
 * @author Alexander Krysin
 */
class UsersDirectoryPacketHandler : PacketHandler<WTLD> {
    private val bexSubtypeToPacketHandler = mapOf(
        Pair(OBIMP_BEX_UD_SRV_PARAMS_REPLY, UsersDirectoryParametersReplyPacketHandler()),
        Pair(OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY, DetailsRequestReplyPacketHandler()),
        Pair(OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY, DetailsUpdateReplyPacketHandler()),
        Pair(OBIMP_BEX_UD_SRV_SEARCH_REPLY, SearchReplyPacketHandler()),
        Pair(OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY, SecureUpdateReplyPacketHandler())
    )

    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        bexSubtypeToPacketHandler[packet.getSubtype()]?.handlePacket(connection, packet)
    }

    companion object {
        //BEX 0x0005, Users directory (OBIMP_BEX_UD)
        const val OBIMP_BEX_UD_CLI_PARAMS: Short = 0x0001
        const val OBIMP_BEX_UD_SRV_PARAMS_REPLY: Short = 0x0002
        const val OBIMP_BEX_UD_CLI_DETAILS_REQ: Short = 0x0003
        const val OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY: Short = 0x0004
        const val OBIMP_BEX_UD_CLI_DETAILS_UPD: Short = 0x0005
        const val OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY: Short = 0x0006
        const val OBIMP_BEX_UD_CLI_SEARCH: Short = 0x0007
        const val OBIMP_BEX_UD_SRV_SEARCH_REPLY: Short = 0x0008
        const val OBIMP_BEX_UD_CLI_SECURE_UPD: Short = 0x0009
        const val OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY: Short = 0x000A
    }
}