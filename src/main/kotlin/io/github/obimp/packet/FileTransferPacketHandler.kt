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
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT_CLI_SRV_CONTROL
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT_DIR_PROX_ERROR
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT_DIR_PROX_FILE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT_DIR_PROX_FILE_DATA
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT_DIR_PROX_FILE_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT_DIR_PROX_HELLO
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT_SRV_PARAMS_REPLY

/**
 * @author Alexander Krysin
 */
class FileTransferPacketHandler {
    fun parsePacket(packet: Packet, connection: OBIMPConnection) {
        when (packet.subtype) {
            OBIMP_BEX_FT_SRV_PARAMS_REPLY -> {}
            OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST -> {}
            OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY -> {}
            OBIMP_BEX_FT_CLI_SRV_CONTROL -> {}
            OBIMP_BEX_FT_DIR_PROX_ERROR -> {}
            OBIMP_BEX_FT_DIR_PROX_HELLO -> {}
            OBIMP_BEX_FT_DIR_PROX_FILE -> {}
            OBIMP_BEX_FT_DIR_PROX_FILE_REPLY -> {}
            OBIMP_BEX_FT_DIR_PROX_FILE_DATA -> {}
        }
    }
}