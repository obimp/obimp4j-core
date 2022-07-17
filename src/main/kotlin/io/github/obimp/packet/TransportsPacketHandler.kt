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
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_TP_SRV_ITEM_READY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_TP_SRV_PARAMS_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_TP_SRV_SETTINGS_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_TP_SRV_SHOW_NOTIF
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_TP_SRV_TRANSPORT_INFO

/**
 * @author Alexander Krysin
 */
class TransportsPacketHandler {
    fun parsePacket(packet: Packet, connection: OBIMPConnection) {
        when (packet.subtype) {
            OBIMP_BEX_TP_SRV_PARAMS_REPLY -> {}
            OBIMP_BEX_TP_SRV_ITEM_READY -> {}
            OBIMP_BEX_TP_SRV_SETTINGS_REPLY -> {}
            OBIMP_BEX_TP_SRV_TRANSPORT_INFO -> {}
            OBIMP_BEX_TP_SRV_SHOW_NOTIF -> {}
            OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH -> {}
        }
    }
}