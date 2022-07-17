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
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_REQUEST
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_CONTACT_ONLINE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_MAIL_NOTIF
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_OWN_MAIL_URL
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_PARAMS_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_SRV_PRES_INFO

/**
 * @author Alexander Krysin
 */
class PresencePacketHandler {
    fun parsePacket(packet: Packet, connection: OBIMPConnection) {
        when (packet.subtype) {
            OBIMP_BEX_PRES_SRV_PARAMS_REPLY -> {}
            OBIMP_BEX_PRES_SRV_CONTACT_ONLINE -> {
                val accountName = packet.getWTLD().getDataType<UTF8>().value
                val status = packet.getWTLD().getDataType<LongWord>().value
                for (usl in connection.userStatusListeners) {
                    usl.onUserOnline(accountName, status)
                }
            }
            OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE -> {
                val accountName = packet.getWTLD().getDataType<UTF8>().value
                for (usl in connection.userStatusListeners) {
                    usl.onUserOffline(accountName)
                }
            }
            OBIMP_BEX_PRES_SRV_PRES_INFO -> {
                connection.send(Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_REQUEST))
            }
            OBIMP_BEX_PRES_SRV_MAIL_NOTIF -> {}
            OBIMP_BEX_PRES_SRV_OWN_MAIL_URL -> {}
        }
    }
}