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

package io.github.obimp.packet.handle.im.handlers

import io.github.obimp.connection.Connection
import io.github.obimp.connection.getListeners
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.structure.readDataType
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.im.Notification
import io.github.obimp.im.NotificationType
import io.github.obimp.im.NotificationValue
import io.github.obimp.listener.InstantMessagingListener
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler

/**
 * @author Alexander Krysin
 */
class NotifyPacketHandler : PacketHandler<WTLD> {
    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        val accountName = packet.nextItem().readDataType<UTF8>().value
        val notificationType = packet.nextItem().readDataType<LongWord>().value
        val notificationValue = packet.nextItem().readDataType<LongWord>().value
        val transportID = if (packet.hasItems()) packet.nextItem().readDataType<LongWord>().value else null

        for (ml in connection.getListeners<InstantMessagingListener>()) {
            ml.onNotify(
                Notification(
                    accountName,
                    NotificationType.byType(notificationType),
                    NotificationValue.byValue(notificationValue),
                    transportID
                )
            )
        }
    }
}