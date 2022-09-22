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

package io.github.obimp.packet.handle.presence

import io.github.obimp.connection.Connection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.presence.handlers.*

/**
 * @author Alexander Krysin
 */
class PresencePacketHandler : PacketHandler<WTLD> {
    private val bexSubtypeToPacketHandler = mapOf(
        Pair(OBIMP_BEX_PRES_SRV_PARAMS_REPLY, PresenceParametersReplyPacketHandler()),
        Pair(OBIMP_BEX_PRES_SRV_CONTACT_ONLINE, ContactOnlinePacketHandler()),
        Pair(OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE, ContactOfflinePacketHandler()),
        Pair(OBIMP_BEX_PRES_SRV_PRES_INFO, PresenceInfoPacketHandler()),
        Pair(OBIMP_BEX_PRES_SRV_MAIL_NOTIF, MailNotificationPacketHandler()),
        Pair(OBIMP_BEX_PRES_SRV_OWN_MAIL_URL, OwnMailUrlPacketHandler()),
    )

    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        bexSubtypeToPacketHandler[packet.getSubtype()]?.handlePacket(connection, packet)
    }

    companion object {
        //BEX 0x0003, Presence (OBIMP_BEX_PRES)
        const val OBIMP_BEX_PRES_CLI_PARAMS: Short = 0x0001
        const val OBIMP_BEX_PRES_SRV_PARAMS_REPLY: Short = 0x0002
        const val OBIMP_BEX_PRES_CLI_SET_PRES_INFO: Short = 0x0003
        const val OBIMP_BEX_PRES_CLI_SET_STATUS: Short = 0x0004
        const val OBIMP_BEX_PRES_CLI_ACTIVATE: Short = 0x0005
        const val OBIMP_BEX_PRES_SRV_CONTACT_ONLINE: Short = 0x0006
        const val OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE: Short = 0x0007
        const val OBIMP_BEX_PRES_CLI_REQ_PRES_INFO: Short = 0x0008
        const val OBIMP_BEX_PRES_SRV_PRES_INFO: Short = 0x0009
        const val OBIMP_BEX_PRES_SRV_MAIL_NOTIF: Short = 0x000A
        const val OBIMP_BEX_PRES_CLI_REQ_OWN_MAIL_URL: Short = 0x000B
        const val OBIMP_BEX_PRES_SRV_OWN_MAIL_URL: Short = 0x000C
    }
}