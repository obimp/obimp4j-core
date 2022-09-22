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

package io.github.obimp.packet.handle.common.handlers

import io.github.obimp.common.LoginError
import io.github.obimp.connection.Connection
import io.github.obimp.connection.getListeners
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.structure.readDataType
import io.github.obimp.data.structure.readDataTypeList
import io.github.obimp.data.type.Word
import io.github.obimp.listener.CommonListener
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_CL
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_FT
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_IM
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_PRES
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_TP
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_UA
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_UD
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.cl.ContactListPacketHandler.Companion.OBIMP_BEX_CL_CLI_PARAMS
import io.github.obimp.packet.handle.createEmptyPacket
import io.github.obimp.packet.handle.ft.FileTransferPacketHandler.Companion.OBIMP_BEX_FT_CLI_PARAMS
import io.github.obimp.packet.handle.im.InstantMessagingPacketHandler.Companion.OBIMP_BEX_IM_CLI_PARAMS
import io.github.obimp.packet.handle.presence.PresencePacketHandler.Companion.OBIMP_BEX_PRES_CLI_PARAMS
import io.github.obimp.packet.handle.presence.PresencePacketHandler.Companion.OBIMP_BEX_PRES_CLI_REQ_PRES_INFO
import io.github.obimp.packet.handle.transports.TransportsPacketHandler.Companion.OBIMP_BEX_TP_CLI_PARAMS
import io.github.obimp.packet.handle.ua.UserAvatarsPacketHandler.Companion.OBIMP_BEX_UA_CLI_PARAMS
import io.github.obimp.packet.handle.ud.UsersDirectoryPacketHandler.Companion.OBIMP_BEX_UD_CLI_PARAMS

/**
 * @author Alexander Krysin
 */
class LoginReplyPacketHandler : PacketHandler<WTLD> {
    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        val wtld = packet.nextItem()
        if (wtld.getType() == 0x0001) {
            val errorCode = wtld.readDataType<Word>().value
            for (cl in connection.getListeners<CommonListener>()) {
                cl.onLoginError(LoginError.byCode(errorCode))
            }
        }
        if (wtld.getType() == 0x0002) {
            for (cl in connection.getListeners<CommonListener>()) {
                cl.onConnect()
            }
            val data = wtld.readDataTypeList<Word>()
            val serverSupportedBexs = mutableMapOf<Short, Short>()
            var i = 0
            while (i < data.size) {
                val bexType = data[i++].value
                val maximalBexSubtype = data[i++].value
                serverSupportedBexs[bexType] = maximalBexSubtype
            }
            for (bexType in serverSupportedBexs.keys) {
                when (bexType) {
                    OBIMP_BEX_CL -> connection.connectionListener.sendPacket(
                        createEmptyPacket(
                            OBIMP_BEX_CL,
                            OBIMP_BEX_CL_CLI_PARAMS
                        )
                    )

                    OBIMP_BEX_PRES -> connection.connectionListener.sendPacket(
                        createEmptyPacket(
                            OBIMP_BEX_PRES,
                            OBIMP_BEX_PRES_CLI_PARAMS
                        )
                    )

                    OBIMP_BEX_IM -> connection.connectionListener.sendPacket(
                        createEmptyPacket(
                            OBIMP_BEX_IM,
                            OBIMP_BEX_IM_CLI_PARAMS
                        )
                    )

                    OBIMP_BEX_UD -> connection.connectionListener.sendPacket(
                        createEmptyPacket(
                            OBIMP_BEX_UD,
                            OBIMP_BEX_UD_CLI_PARAMS
                        )
                    )

                    OBIMP_BEX_UA -> connection.connectionListener.sendPacket(
                        createEmptyPacket(
                            OBIMP_BEX_UA,
                            OBIMP_BEX_UA_CLI_PARAMS
                        )
                    )

                    OBIMP_BEX_FT -> connection.connectionListener.sendPacket(
                        createEmptyPacket(
                            OBIMP_BEX_FT,
                            OBIMP_BEX_FT_CLI_PARAMS
                        )
                    )

                    OBIMP_BEX_TP -> connection.connectionListener.sendPacket(
                        createEmptyPacket(
                            OBIMP_BEX_TP,
                            OBIMP_BEX_TP_CLI_PARAMS
                        )
                    )
                }
            }
            connection.connectionListener.sendPacket(
                createEmptyPacket(
                    OBIMP_BEX_PRES,
                    OBIMP_BEX_PRES_CLI_REQ_PRES_INFO
                )
            )
        }
    }
}