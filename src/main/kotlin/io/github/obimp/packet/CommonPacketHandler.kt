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
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.BLK
import io.github.obimp.data.type.OctaWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.data.type.Word
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_PARAMS
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM_CLI_LOGIN
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM_SRV_BYE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM_SRV_HELLO
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM_SRV_LOGIN_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM_SRV_REGISTER_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_FT_CLI_PARAMS
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_IM_CLI_PARAMS
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_CLI_PARAMS
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_CLI_REQ_PRES_INFO
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_TP
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_TP_CLI_PARAMS
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UA
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UA_CLI_PARAMS
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_UD_CLI_PARAMS
import java.security.MessageDigest
import java.util.*

/**
 * @author Alexander Krysin
 */
class CommonPacketHandler {
    fun parsePacket(packet: Packet, connection: OBIMPConnection, username: String, password: String) {
        when (packet.subtype) {
            OBIMP_BEX_COM_SRV_HELLO -> {
                val wtld = packet.getWTLD()
                when (wtld.type) {
                    0x0001 -> {
                        val errorCode = wtld.getDataType<Word>()
                        for (cl in connection.connectionListeners) {
                            cl.onLoginFailed(errorCode.value)
                        }
                    }
                    0x0002 -> {
                        val serverKey = wtld.getDataType<BLK>().data
                        val hash = md5(md5("${username.lowercase()}$SALT$password") + serverKey)
                        val login = Packet(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_LOGIN)
                        login.addWTLD(WTLD(0x0001, UTF8(username)))
                        login.addWTLD(WTLD(0x0002, OctaWord(hash)))
                        connection.send(login)
                    }
                    0x0007 -> {
                        val login = Packet(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_LOGIN)
                        login.addWTLD(WTLD(0x0001, UTF8(username)))
                        login.addWTLD(WTLD(0x0003, BLK(base64(password))))
                        connection.send(login)
                    }
                }
            }
            OBIMP_BEX_COM_SRV_LOGIN_REPLY -> {
                val wtld = packet.getWTLD()
                if (wtld.type == 0x0001) {
                    val errorCode = wtld.getDataType<Word>()
                    for (cl in connection.connectionListeners) {
                        cl.onLoginFailed(errorCode.value)
                    }
                }
                if (wtld.type == 0x0002) {
                    for (cl in connection.connectionListeners) {
                        cl.onLoginSuccess()
                    }
                    val data = wtld.getDataTypeList<Word>()
                    val serverSupportedBexs = mutableMapOf<Short, Short>()
                    var i = 0
                    while (i < data.size) {
                        val bexType = data[i++].value.toShort()
                        val maximalBexSubtype = data[i++].value.toShort()
                        serverSupportedBexs[bexType] = maximalBexSubtype
                    }
                    for (bexType in serverSupportedBexs.keys) {
                        when (bexType) {
                            OBIMP_BEX_CL -> connection.send(Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_PARAMS))
                            OBIMP_BEX_PRES -> connection.send(Packet(OBIMP_BEX_PRES, OBIMP_BEX_PRES_CLI_PARAMS))
                            OBIMP_BEX_IM -> connection.send(Packet(OBIMP_BEX_IM, OBIMP_BEX_IM_CLI_PARAMS))
                            OBIMP_BEX_UD -> connection.send(Packet(OBIMP_BEX_UD, OBIMP_BEX_UD_CLI_PARAMS))
                            OBIMP_BEX_UA -> connection.send(Packet(OBIMP_BEX_UA, OBIMP_BEX_UA_CLI_PARAMS))
                            OBIMP_BEX_FT -> connection.send(Packet(OBIMP_BEX_FT, OBIMP_BEX_FT_CLI_PARAMS))
                            OBIMP_BEX_TP -> connection.send(Packet(OBIMP_BEX_TP, OBIMP_BEX_TP_CLI_PARAMS))
                        }
                    }
                    connection.send(Packet(OBIMP_BEX_PRES, OBIMP_BEX_PRES_CLI_REQ_PRES_INFO))
                }
            }
            OBIMP_BEX_COM_SRV_BYE -> {
                for (cl in connection.connectionListeners) {
                    val byeReasonCode = packet.getWTLD().getDataType<Word>()
                    cl.onLogout(byeReasonCode.value)
                }
            }
            OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING -> {
                connection.send(Packet(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG))
            }
            OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG -> {}
            OBIMP_BEX_COM_SRV_REGISTER_REPLY -> {}
        }
    }

    private fun md5(data: String) = md5(data.encodeToByteArray())

    private fun md5(data: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("MD5")
        md.update(data)
        return md.digest()
    }

    private fun base64(data: String) = Base64.getEncoder().encode(data.encodeToByteArray())

    companion object {
        const val SALT = "OBIMPSALT"

        const val HELLO_ERROR_ACCOUNT_INVALID: Short = 0x0001
        const val HELLO_ERROR_SERVICE_TEMP_UNAVAILABLE: Short = 0x0002
        const val HELLO_ERROR_ACCOUNT_BANNED: Short = 0x0003
        const val HELLO_ERROR_WRONG_COOKIE: Short = 0x0004
        const val HELLO_ERROR_TOO_MANY_CLIENTS: Short = 0x0005
        const val HELLO_ERROR_INVALID_LOGIN: Short = 0x0006

        const val LOGIN_ERROR_ACCOUNT_INVALID: Short = 0x0001
        const val LOGIN_ERROR_SERVICE_TEMP_UNAVAILABLE: Short = 0x0002
        const val LOGIN_ERROR_ACCOUNT_BANNED: Short = 0x0003
        const val LOGIN_ERROR_WRONG_PASSWORD: Short = 0x0004
        const val LOGIN_ERROR_INVALID_LOGIN: Short = 0x0005

        const val BYE_REASON_SRV_SHUTDOWN: Short = 0x0001
        const val BYE_REASON_CLI_NEW_LOGIN: Short = 0x0002
        const val BYE_REASON_ACCOUNT_KICKED: Short = 0x0003
        const val BYE_REASON_INCORRECT_SEQ: Short = 0x0004
        const val BYE_REASON_INCORRECT_BEX_TYPE: Short = 0x0005
        const val BYE_REASON_INCORRECT_BEX_SUB: Short = 0x0006
        const val BYE_REASON_INCORRECT_BEX_STEP: Short = 0x0007
        const val BYE_REASON_TIMEOUT: Short = 0x0008
        const val BYE_REASON_INCORRECT_WTLD: Short = 0x0009
        const val BYE_REASON_NOT_ALLOWED: Short = 0x000A
        const val BYE_REASON_FLOODING: Short = 0x000B

        const val REG_RES_SUCCESS: Short = 0x0000
        const val REG_RES_DISABLED: Short = 0x0001
        const val REG_RES_ACCOUNT_EXISTS: Short = 0x0002
        const val REG_RES_BAD_ACCOUNT_NAME: Short = 0x0003
        const val REG_RES_BAD_REQUEST: Short = 0x0004
        const val REG_RES_BAD_SERVER_KEY: Short = 0x0005
        const val REG_RES_SERVICE_TEMP_UNAVAILABLE: Short = 0x0006
        const val REG_RES_EMAIL_REQUIRED: Short = 0x0007
    }
}