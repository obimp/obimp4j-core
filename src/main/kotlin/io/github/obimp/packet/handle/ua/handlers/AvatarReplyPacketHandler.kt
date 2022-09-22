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

package io.github.obimp.packet.handle.ua.handlers

import io.github.obimp.connection.Connection
import io.github.obimp.connection.getListeners
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.structure.readDataType
import io.github.obimp.data.type.BLK
import io.github.obimp.data.type.OctaWord
import io.github.obimp.data.type.Word
import io.github.obimp.listener.UserAvatarsListener
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.ua.AvatarRequestResult
import io.github.obimp.ua.AvatarResult
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */
class AvatarReplyPacketHandler : PacketHandler<WTLD> {
    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        val avatarRequestResult = AvatarRequestResult.byValue(packet.nextItem().readDataType<Word>().value)
        val avatarMD5Hash = packet.nextItem().readDataType<OctaWord>().value.array()
        var avatarFile: ByteBuffer? = null

        if (avatarRequestResult == AvatarRequestResult.SUCCESS) {
            avatarFile = packet.nextItem().readDataType<BLK>().value
        }

        for (ual in connection.getListeners<UserAvatarsListener>()) {
            ual.onAvatar(AvatarResult(avatarRequestResult, avatarMD5Hash, avatarFile))
        }
    }
}