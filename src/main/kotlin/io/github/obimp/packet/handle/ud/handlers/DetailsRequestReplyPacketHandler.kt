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

package io.github.obimp.packet.handle.ud.handlers

import io.github.obimp.connection.Connection
import io.github.obimp.connection.getListeners
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.structure.readDataType
import io.github.obimp.data.type.Byte
import io.github.obimp.data.type.DateTime
import io.github.obimp.data.type.UTF8
import io.github.obimp.data.type.Word
import io.github.obimp.listener.UsersDirectoryListener
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.presence.Language
import io.github.obimp.ud.*
import java.time.ZonedDateTime

/**
 * @author Alexander Krysin
 */
class DetailsRequestReplyPacketHandler : PacketHandler<WTLD> {
    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        val requestDetailsResult = RequestDetailsResult.byCode(packet.nextItem().readDataType<Word>().value)
        val accountName = packet.nextItem().readDataType<UTF8>().value
        var userDetails: UserDetails? = null

        if (requestDetailsResult == RequestDetailsResult.SUCCESS) {
            userDetails = UserDetails(requestDetailsResult, accountName)

            while (packet.hasItems()) {
                val wtld = packet.nextItem()
                when (wtld.getType()) {
                    0x0004 -> userDetails.nickname = wtld.readDataType<UTF8>().value
                    0x0005 -> userDetails.firstName = wtld.readDataType<UTF8>().value
                    0x0006 -> userDetails.lastName = wtld.readDataType<UTF8>().value
                    0x0007 -> userDetails.country = Country.byCode(wtld.readDataType<Word>().value)
                    0x0008 -> userDetails.regionState = wtld.readDataType<UTF8>().value
                    0x0009 -> userDetails.city = wtld.readDataType<UTF8>().value
                    0x000A -> userDetails.zipCode = wtld.readDataType<UTF8>().value
                    0x000B -> userDetails.address = wtld.readDataType<UTF8>().value
                    0x000C -> userDetails.language = Language.byCode(wtld.readDataType<Word>().value)
                    0x000D -> userDetails.additionalLanguage = Language.byCode(wtld.readDataType<Word>().value)
                    0x000E -> userDetails.gender = Gender.values().find { it.value == wtld.readDataType<Byte>().value }
                    0x000F -> userDetails.birthday =
                        wtld.readDataType<DateTime>().value.atOffset(ZonedDateTime.now().offset).toLocalDateTime()
                    0x0010 -> userDetails.homepage = wtld.readDataType<UTF8>().value
                    0x0011 -> userDetails.about = wtld.readDataType<UTF8>().value
                    0x0012 -> userDetails.interests = wtld.readDataType<UTF8>().value
                    0x0013 -> userDetails.email = wtld.readDataType<UTF8>().value
                    0x0014 -> userDetails.additionalEmail = wtld.readDataType<UTF8>().value
                    0x0015 -> userDetails.homePhone = wtld.readDataType<UTF8>().value
                    0x0016 -> userDetails.workPhone = wtld.readDataType<UTF8>().value
                    0x0017 -> userDetails.cellularPhone = wtld.readDataType<UTF8>().value
                    0x0018 -> userDetails.faxNumber = wtld.readDataType<UTF8>().value
                    0x001A -> userDetails.company = wtld.readDataType<UTF8>().value
                    0x001B -> userDetails.divisionDepartment = wtld.readDataType<UTF8>().value
                    0x001C -> userDetails.position = wtld.readDataType<UTF8>().value
                }
            }
        }

        for (ml in connection.getListeners<UsersDirectoryListener>()) {
            ml.onDetails(UserDetailsReply(requestDetailsResult, userDetails))
        }
    }
}