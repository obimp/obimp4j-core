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
import io.github.obimp.data.type.BLK
import io.github.obimp.data.type.OctaWord
import io.github.obimp.data.type.Word
import io.github.obimp.listener.UserAvatarsListener
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UA_SRV_AVATAR_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UA_SRV_PARAMS_REPLY

/**
 * @author Alexander Krysin
 */
class UserAvatarsPacketHandler {
    fun parsePacket(packet: Packet, connection: OBIMPConnection) {
        when (packet.subtype) {
            OBIMP_BEX_UA_SRV_PARAMS_REPLY -> {}
            OBIMP_BEX_UA_SRV_AVATAR_REPLY -> {
                val requestResult = packet.getWTLD().getDataType<Word>().value

                if (requestResult == AVATAR_REQ_SUCCESS) {
                    val avatarMD5 = packet.getWTLD().getDataType<OctaWord>().data.decodeToString()
                    val avatarFile = packet.getWTLD().getDataType<BLK>().data

                    for (ual in connection.userAvatarsListeners) {
                        ual.onAvatarLoaded(avatarMD5, avatarFile)
                    }
                }
            }
            OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY -> {
                val result = packet.getWTLD().getDataType<Word>().value

                if (result == AVATAR_SET_SUCCESS) {
                    connection.userAvatarsListeners.forEach(UserAvatarsListener::onAvatarUpdated)
                }
            }
        }
    }

    companion object {
        const val AVATAR_REQ_SUCCESS = 0x0000
        const val AVATAR_REQ_NOT_FOUND = 0x0001
        const val AVATAR_REQ_NOT_ALLOWED = 0x0002

        const val AVATAR_SET_SUCCESS = 0x0000
        const val AVATAR_SET_BAD_MD5 = 0x0001
        const val AVATAR_SET_NOT_ALLOWED = 0x0002
        const val AVATAR_SET_TEMP_UNAVAILABLE = 0x0003
        const val AVATAR_SET_TOO_BIG = 0x0004
        const val AVATAR_SET_TOO_SMALL = 0x0005
        const val AVATAR_SET_BANNED = 0x0006
        const val AVATAR_SET_INVALID_TYPE = 0x0007
        const val AVATAR_SET_OTHER_ERROR = 0x0008
    }
}