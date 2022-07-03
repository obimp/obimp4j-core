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
import io.github.obimp.XStatus.X_STATUS
import io.github.obimp.cl.*
import io.github.obimp.data.structure.STLD
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.BLK
import io.github.obimp.data.type.OctaWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.listener.*
import io.github.obimp.packet.PacketListener.Companion.getLength
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException
import java.net.Socket
import java.security.MessageDigest
import java.util.*

/**
 * @author Alexander Krysin
 */
class PacketHandler {
    companion object {
        var logged = false

        //General BEX types
        const val OBIMP_BEX_COM = 0x0001 // Common
        const val OBIMP_BEX_CL = 0x0002 // Contact list
        const val OBIMP_BEX_PRES = 0x0003 // Presence
        const val OBIMP_BEX_IM = 0x0004 // Instant messaging
        const val OBIMP_BEX_UD = 0x0005 // Users directory
        const val OBIMP_BEX_UA = 0x0006 // User avatars
        const val OBIMP_BEX_FT = 0x0007 // File transfer
        const val OBIMP_BEX_TP = 0x0008 // Transports

        //BEX 0x0001, Common. (OBIMP_BEX_COM)
        const val OBIMP_BEX_COM_CLI_HELLO = 0x0001 //Subtype: 0x0001. (OBIMP_BEX_COM_CLI_HELLO)
        private const val OBIMP_BEX_COM_SRV_HELLO = 0x0002 //Subtype: 0x0002. (OBIMP_BEX_COM_SRV_HELLO)
        const val OBIMP_BEX_COM_CLI_LOGIN = 0x0003 //Subtype: 0x0003. (OBIMP_BEX_COM_CLI_LOGIN)
        private const val OBIMP_BEX_COM_SRV_LOGIN_REPLY = 0x0004 //Subtype: 0x0004. (OBIMP_BEX_COM_SRV_LOGIN_REPLY)
        private const val OBIMP_BEX_COM_SRV_BYE = 0x0005 //Subtype: 0x0005. (OBIMP_BEX_COM_SRV_BYE)
        const val OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING =
            0x0006 //Subtype: 0x0006. (OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING)
        const val OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG =
            0x0007 //Subtype: 0x0007. (OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG)
        const val OBIMP_BEX_COM_CLI_REGISTER = 0x0008 //Subtype: 0x0008. (OBIMP_BEX_COM_CLI_REGISTER)
        private const val OBIMP_BEX_COM_SRV_REGISTER_REPLY =
            0x0009 //Subtype: 0x0009. (OBIMP_BEX_COM_SRV_REGISTER_REPLY)

        //BEX 0x0002, Contact list. (OBIMP_BEX_CL)
        const val OBIMP_BEX_CL_CLI_PARAMS = 0x0001 //Subtype: 0x0001. (OBIMP_BEX_CL_CLI_PARAMS)
        private const val OBIMP_BEX_CL_SRV_PARAMS_REPLY = 0x0002 //Subtype: 0x0002. (OBIMP_BEX_CL_SRV_PARAMS_REPLY)
        const val OBIMP_BEX_CL_CLI_REQUEST = 0x0003 //Subtype: 0x0003. (OBIMP_BEX_CL_CLI_REQUEST)
        private const val OBIMP_BEX_CL_SRV_REPLY = 0x0004 //Subtype: 0x0004. (OBIMP_BEX_CL_SRV_REPLY)
        const val OBIMP_BEX_CL_CLI_VERIFY = 0x0005 //Subtype: 0x0005. (OBIMP_BEX_CL_CLI_VERIFY)
        private const val OBIMP_BEX_CL_SRV_VERIFY_REPLY = 0x0006 //Subtype: 0x0006. (OBIMP_BEX_CL_SRV_VERIFY_REPLY)
        const val OBIMP_BEX_CL_CLI_ADD_ITEM = 0x0007 //Subtype: 0x0007. (OBIMP_BEX_CL_CLI_ADD_ITEM)
        private const val OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY = 0x0008 //Subtype: 0x0008. (OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY)
        const val OBIMP_BEX_CL_CLI_DEL_ITEM = 0x0009 //Subtype: 0x0009. (OBIMP_BEX_CL_CLI_DEL_ITEM)
        private const val OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY = 0x000A //Subtype: 0x000A. (OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY)
        const val OBIMP_BEX_CL_CLI_UPD_ITEM = 0x000B //Subtype: 0x000B. (OBIMP_BEX_CL_CLI_UPD_ITEM)
        private const val OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY = 0x000C //Subtype: 0x000C. (OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY)
        const val OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST = 0x000D //Subtype: 0x000D. (OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST)
        const val OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY = 0x000E //Subtype: 0x000E. (OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY)
        const val OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE = 0x000F //Subtype: 0x000F. (OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE)
        const val OBIMP_BEX_CL_CLI_REQ_OFFAUTH = 0x0010 //Subtype: 0x0010. (OBIMP_BEX_CL_CLI_REQ_OFFAUTH)
        private const val OBIMP_BEX_CL_SRV_DONE_OFFAUTH = 0x0011 //Subtype: 0x0011. (OBIMP_BEX_CL_SRV_DONE_OFFAUTH)
        const val OBIMP_BEX_CL_CLI_DEL_OFFAUTH = 0x0012 //Subtype: 0x0012. (OBIMP_BEX_CL_CLI_DEL_OFFAUTH)
        private const val OBIMP_BEX_CL_SRV_ITEM_OPER = 0x0013 //Subtype: 0x0013. (OBIMP_BEX_CL_SRV_ITEM_OPER)
        private const val OBIMP_BEX_CL_SRV_BEGIN_UPDATE = 0x0014 //Subtype: 0x0014. (OBIMP_BEX_CL_SRV_BEGIN_UPDATE)
        private const val OBIMP_BEX_CL_SRV_END_UPDATE = 0x0015 //Subtype: 0x0015. (OBIMP_BEX_CL_SRV_END_UPDATE)

        //BEX 0x0003, Presence (OBIMP_BEX_PRES)
        const val OBIMP_BEX_PRES_CLI_PARAMS = 0x0001 //Subtype: 0x0001. (OBIMP_BEX_PRES_CLI_PARAMS)
        private const val OBIMP_BEX_PRES_SRV_PARAMS_REPLY = 0x0002 //Subtype: 0x0002. (OBIMP_BEX_PRES_SRV_PARAMS_REPLY)
        const val OBIMP_BEX_PRES_CLI_SET_PRES_INFO = 0x0003 //Subtype: 0x0003. (OBIMP_BEX_PRES_CLI_SET_PRES_INFO)
        const val OBIMP_BEX_PRES_CLI_SET_STATUS = 0x0004 //Subtype: 0x0004. (OBIMP_BEX_PRES_CLI_SET_STATUS)
        const val OBIMP_BEX_PRES_CLI_ACTIVATE = 0x0005 //Subtype: 0x0005. (OBIMP_BEX_PRES_CLI_ACTIVATE)
        private const val OBIMP_BEX_PRES_SRV_CONTACT_ONLINE =
            0x0006 //Subtype: 0x0006. (OBIMP_BEX_PRES_SRV_CONTACT_ONLINE)
        private const val OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE =
            0x0007 //Subtype: 0x0007. (OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE)
        const val OBIMP_BEX_PRES_CLI_REQ_PRES_INFO = 0x0008 //Subtype: 0x0008. (OBIMP_BEX_PRES_CLI_REQ_PRES_INFO)
        private const val OBIMP_BEX_PRES_SRV_PRES_INFO = 0x0009 //Subtype: 0x0009. (OBIMP_BEX_PRES_SRV_PRES_INFO)
        private const val OBIMP_BEX_PRES_SRV_MAIL_NOTIF = 0x000A //Subtype: 0x000A. (OBIMP_BEX_PRES_SRV_MAIL_NOTIF)
        const val OBIMP_BEX_PRES_CLI_REQ_OWN_MAIL_URL = 0x000B //Subtype: 0x000B. (OBIMP_BEX_PRES_CLI_REQ_OWN_MAIL_URL)
        private const val OBIMP_BEX_PRES_SRV_OWN_MAIL_URL = 0x000C //Subtype: 0x000C. (OBIMP_BEX_PRES_SRV_OWN_MAIL_URL)

        //BEX 0x0004, Instant messaging (OBIMP_BEX_IM)
        const val OBIMP_BEX_IM_CLI_PARAMS = 0x0001 //Subtype: 0x0001. (OBIMP_BEX_IM_CLI_PARAMS)
        private const val OBIMP_BEX_IM_SRV_PARAMS_REPLY = 0x0002 //Subtype: 0x0002. (OBIMP_BEX_IM_SRV_PARAMS_REPLY)
        const val OBIMP_BEX_IM_CLI_REQ_OFFLINE = 0x0003 //Subtype: 0x0003. (OBIMP_BEX_IM_CLI_REQ_OFFLINE)
        private const val OBIMP_BEX_IM_SRV_DONE_OFFLINE = 0x0004 //Subtype: 0x0004. (OBIMP_BEX_IM_SRV_DONE_OFFLINE)
        const val OBIMP_BEX_IM_CLI_DEL_OFFLINE = 0x0005 //Subtype: 0x0005. (OBIMP_BEX_IM_CLI_DEL_OFFLINE)
        const val OBIMP_BEX_IM_CLI_MESSAGE = 0x0006 //Subtype: 0x0006. (OBIMP_BEX_IM_CLI_MESSAGE)
        private const val OBIMP_BEX_IM_SRV_MESSAGE = 0x0007 //Subtype: 0x0007. (OBIMP_BEX_IM_SRV_MESSAGE)
        const val OBIMP_BEX_IM_CLI_SRV_MSG_REPORT = 0x0008 //Subtype: 0x0008. (OBIMP_BEX_IM_CLI_SRV_MSG_REPORT)
        const val OBIMP_BEX_IM_CLI_SRV_NOTIFY = 0x0009 //Subtype: 0x0009. (OBIMP_BEX_IM_CLI_SRV_NOTIFY)
        const val OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REQ =
            0x000A //Subtype: 0x000A. (OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REQ)
        const val OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY =
            0x000B //Subtype: 0x000B. (OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY)
        const val OBIMP_BEX_IM_CLI_MULTIPLE_MSG = 0x000C //Subtype: 0x000C. (OBIMP_BEX_IM_CLI_MULTIPLE_MSG)

        //BEX 0x0005, Users directory (OBIMP_BEX_UD)
        const val OBIMP_BEX_UD_CLI_PARAMS = 0x0001 //Subtype: 0x0001. (OBIMP_BEX_UD_CLI_PARAMS)
        private const val OBIMP_BEX_UD_SRV_PARAMS_REPLY = 0x0002 //Subtype: 0x0002. (OBIMP_BEX_UD_SRV_PARAMS_REPLY)
        const val OBIMP_BEX_UD_CLI_DETAILS_REQ = 0x0003 //Subtype: 0x0003. (OBIMP_BEX_UD_CLI_DETAILS_REQ)
        private const val OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY =
            0x0004 //Subtype: 0x0004. (OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY)
        const val OBIMP_BEX_UD_CLI_DETAILS_UPD = 0x0005 //Subtype: 0x0005. (OBIMP_BEX_UD_CLI_DETAILS_UPD)
        private const val OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY =
            0x0006 //Subtype: 0x0006. (OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY)
        const val OBIMP_BEX_UD_CLI_SEARCH = 0x0007 //Subtype: 0x0007. (OBIMP_BEX_UD_CLI_SEARCH)
        private const val OBIMP_BEX_UD_SRV_SEARCH_REPLY = 0x0008 //Subtype: 0x0008. (OBIMP_BEX_UD_SRV_SEARCH_REPLY)
        const val OBIMP_BEX_UD_CLI_SECURE_UPD = 0x0009 //Subtype: 0x0009. (OBIMP_BEX_UD_CLI_SECURE_UPD)
        private const val OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY =
            0x000A //Subtype: 0x000A. (OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY)

        //BEX 0x0006, User avatars (OBIMP_BEX_UA)
        const val OBIMP_BEX_UA_CLI_PARAMS = 0x0001 //Subtype: 0x0001. (OBIMP_BEX_UA_CLI_PARAMS)
        private const val OBIMP_BEX_UA_SRV_PARAMS_REPLY = 0x0002 //Subtype: 0x0002. (OBIMP_BEX_UA_SRV_PARAMS_REPLY)
        const val OBIMP_BEX_UA_CLI_AVATAR_REQ = 0x0003 //Subtype: 0x0003. (OBIMP_BEX_UA_CLI_AVATAR_REQ)
        private const val OBIMP_BEX_UA_SRV_AVATAR_REPLY = 0x0004 //Subtype: 0x0004. (OBIMP_BEX_UA_SRV_AVATAR_REPLY)
        const val OBIMP_BEX_UA_CLI_AVATAR_SET = 0x0005 //Subtype: 0x0005. (OBIMP_BEX_UA_CLI_AVATAR_SET)
        private const val OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY =
            0x0006 //Subtype: 0x0006. (OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY)

        //BEX 0x0007, File transfer (OBIMP_BEX_FT)
        const val OBIMP_BEX_FT_CLI_PARAMS = 0x0001 //Subtype: 0x0001. (OBIMP_BEX_FT_CLI_PARAMS)
        private const val OBIMP_BEX_FT_SRV_PARAMS_REPLY = 0x0002 //Subtype: 0x0002. (OBIMP_BEX_FT_SRV_PARAMS_REPLY)
        const val OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST =
            0x0003 //Subtype: 0x0003. (OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST)
        const val OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY =
            0x0004 //Subtype: 0x0004. (OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY)
        const val OBIMP_BEX_FT_CLI_SRV_CONTROL = 0x0005 //Subtype: 0x0005. (OBIMP_BEX_FT_CLI_SRV_CONTROL)
        private const val OBIMP_BEX_FT_DIR_PROX_ERROR = 0x0101 //Subtype: 0x0101. (OBIMP_BEX_FT_DIR_PROX_ERROR)
        private const val OBIMP_BEX_FT_DIR_PROX_HELLO = 0x0102 //Subtype: 0x0102. (OBIMP_BEX_FT_DIR_PROX_HELLO)
        private const val OBIMP_BEX_FT_DIR_PROX_FILE = 0x0103 //Subtype: 0x0103. (OBIMP_BEX_FT_DIR_PROX_FILE)
        private const val OBIMP_BEX_FT_DIR_PROX_FILE_REPLY =
            0x0104 //Subtype: 0x0104. (OBIMP_BEX_FT_DIR_PROX_FILE_REPLY)
        private const val OBIMP_BEX_FT_DIR_PROX_FILE_DATA = 0x0105 //Subtype: 0x0105. (OBIMP_BEX_FT_DIR_PROX_FILE_DATA)

        //BEX 0x0008, Transports (OBIMP_BEX_TP)
        const val OBIMP_BEX_TP_CLI_PARAMS = 0x0001 //Subtype: 0x0001. (OBIMP_BEX_TP_CLI_PARAMS)
        private const val OBIMP_BEX_TP_SRV_PARAMS_REPLY = 0x0002 //Subtype: 0x0002. (OBIMP_BEX_TP_SRV_PARAMS_REPLY)
        private const val OBIMP_BEX_TP_SRV_ITEM_READY = 0x0003 //Subtype: 0x0003. (OBIMP_BEX_TP_SRV_ITEM_READY)
        const val OBIMP_BEX_TP_CLI_SETTINGS = 0x0004 //Subtype: 0x0004. (OBIMP_BEX_TP_CLI_SETTINGS)
        private const val OBIMP_BEX_TP_SRV_SETTINGS_REPLY = 0x0005 //Subtype: 0x0005. (OBIMP_BEX_TP_SRV_SETTINGS_REPLY)
        const val OBIMP_BEX_TP_CLI_MANAGE = 0x0006 //Subtype: 0x0006. (OBIMP_BEX_TP_CLI_MANAGE)
        private const val OBIMP_BEX_TP_SRV_TRANSPORT_INFO = 0x0007 //Subtype: 0x0007. (OBIMP_BEX_TP_SRV_TRANSPORT_INFO)
        private const val OBIMP_BEX_TP_SRV_SHOW_NOTIF = 0x0008 //Subtype: 0x0008. (OBIMP_BEX_TP_SRV_SHOW_NOTIF)
        private const val OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH =
            0x0009 //Subtype: 0x0009. (OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH)

        @Throws(IOException::class)
        fun parsePacket(
            pkt: Packet,
            tlds: HashMap<Int, BLK>,
            oc: OBIMPConnection,
            username: String,
            password: String,
            socket: Socket
        ) {
            var s = ""
            //println( "0x000"+pkt.getType()+", 0x000"+pkt.getSubtype() );
            when (pkt.getType()) {
                OBIMP_BEX_COM -> when (pkt.getSubtype()) {
                    OBIMP_BEX_COM_SRV_HELLO -> {
                        s = "Server say HELLO"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0001 -> s += " - HELLO_ERROR_ACCOUNT_INVALID"
                                0x0002 -> s += " - HELLO_ERROR_SERVICE_TEMP_UNAVAILABLE"
                                0x0003 -> s += " - HELLO_ERROR_ACCOUNT_BANNED"
                                0x0004 -> s += " - HELLO_ERROR_WRONG_COOKIE"
                                0x0005 -> s += " - HELLO_ERROR_TOO_MANY_CLIENTS"
                                0x0006 -> s += " - HELLO_ERROR_INVALID_LOGIN"
                            }
                            for (cl: ConnectionListener in oc.con_list) {
                                cl.onLoginFailed(s.split(" - ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1])
                            }
                        } else {
                            s += " - NO ERROR"
                            val login = Packet(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_LOGIN)
                            login.append(WTLD(0x0001, UTF8(username)))
                            if (tlds.containsKey(0x0007)) { // server requires plain-text password authentication
                                login.append(WTLD(0x0003, BLK(Base64.getEncoder().encode(password.toByteArray()))))
                            } else { // generate one-time MD5 password hash
                                val srv_key = tlds[0x0002]!!.data
                                val pre_hash: ByteArray =
                                    PacketHandler.MD5((username + "OBIMPSALT" + password).toByteArray())
                                val md5 = ByteArray(pre_hash.size + srv_key.size)
                                var j = 0
                                run {
                                    var i: Int = 0
                                    while (i < pre_hash.size) {
                                        md5[j] = pre_hash[i]
                                        j++
                                        i++
                                    }
                                }
                                var i = 0
                                while (i < srv_key.size) {
                                    md5[j] = srv_key[i]
                                    j++
                                    i++
                                }
                                val hash: ByteArray = PacketHandler.MD5(md5)
                                login.append(WTLD(0x0002, OctaWord(hash)))
                            }
                            socket.getOutputStream().write(login.asByteArray(oc.seq))
                            socket.getOutputStream().flush()
                        }
                    }
                    OBIMP_BEX_COM_SRV_LOGIN_REPLY -> {
                        s = "Server say LOGIN"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0001 -> s += " - LOGIN_ERROR_ACCOUNT_INVALID"
                                0x0002 -> s += " - LOGIN_ERROR_SERVICE_TEMP_UNAVAILABLE"
                                0x0003 -> s += " - LOGIN_ERROR_ACCOUNT_BANNED"
                                0x0004 -> s += " - LOGIN_ERROR_WRONG_PASSWORD"
                                0x0005 -> s += " - LOGIN_ERROR_INVALID_LOGIN"
                            }
                            for (cl: ConnectionListener in oc.con_list) {
                                cl.onLoginFailed(s.split(" - ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1])
                            }
                        } else {
                            s += " - NO ERROR"
                            logged = true
                            for (cl: ConnectionListener in oc.con_list) {
                                cl.onLoginSuccess()
                            }
                        }
                    }
                    OBIMP_BEX_COM_SRV_BYE -> {
                        s = "Server say BYE"
                        //                        for(byte b : packet.asByteArray(OBIMP_BEX_CL)) {
//                            s += b + " ";
//                        }
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0001 -> s += " - BYE_REASON_SRV_SHUTDOWN"
                                0x0002 -> s += " - BYE_REASON_CLI_NEW_LOGIN"
                                0x0003 -> s += " - BYE_REASON_ACCOUNT_KICKED"
                                0x0004 -> s += " - BYE_REASON_INCORRECT_SEQ"
                                0x0005 -> s += " - BYE_REASON_INCORRECT_BEX_TYPE"
                                0x0006 -> s += " - BYE_REASON_INCORRECT_BEX_SUB"
                                0x0007 -> s += " - BYE_REASON_INCORRECT_BEX_STEP"
                                0x0008 -> s += " - BYE_REASON_TIMEOUT"
                                0x0009 -> s += " - BYE_REASON_INCORRECT_WTLD"
                                0x000A -> s += " - BYE_REASON_NOT_ALLOWED"
                                0x000B -> s += " - BYE_REASON_FLOODING"
                            }
                            for (cl: ConnectionListener in oc.con_list) {
                                cl.onLogout(s.split(" - ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
                            }
                        } else s += " - NO ERROR"
                    }
                    OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING -> {
                        s = "Server say PING"
                        oc.sendPong()
                    }
                    OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG -> s = "Server say PONG"
                    OBIMP_BEX_COM_SRV_REGISTER_REPLY -> {
                        s = "Server say REGISTER"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0000 -> s += " - REG_RES_SUCCESS"
                                0x0001 -> s += " - REG_RES_DISABLED"
                                0x0002 -> s += " - REG_RES_ACCOUNT_EXISTS"
                                0x0003 -> s += " - REG_RES_BAD_ACCOUNT_NAME"
                                0x0004 -> s += " - REG_RES_BAD_REQUEST"
                                0x0005 -> s += " - REG_RES_BAD_SERVER_KEY"
                                0x0006 -> s += " - REG_RES_SERVICE_TEMP_UNAVAILABLE"
                                0x0007 -> s += " - REG_RES_EMAIL_REQUIRED"
                            }
                        } else s += " - NO ERROR"
                    }
                }
                OBIMP_BEX_CL -> when (pkt.getSubtype()) {
                    OBIMP_BEX_CL_SRV_PARAMS_REPLY -> {
                        s = "Server say CL PARAMS"
                        if (tlds[0x0001] != null) s += "\n LongWord, maximal groups count (" + tlds[0x0001]!!.data[3] + ")"
                        if (tlds[0x0002] != null) s += "\n LongWord, maximal UTF-8 encoded group name length (" + tlds[0x0002]!!.data[3] + ")"
                        if (tlds[0x0003] != null) s += "\n LongWord, maximal contacts count all over contact list (" + tlds[0x0003]!!.data[3] + ")"
                        if (tlds[0x0004] != null) s += "\n LongWord, maximal UTF-8 encoded account name length (" + tlds[0x0004]!!.data[3] + ")"
                        if (tlds[0x0005] != null) s += "\n LongWord, maximal UTF-8 encoded contact name/transport friendly name length (" + tlds[0x0005]!!.data[3] + ")"
                        if (tlds[0x0006] != null) s += "\n LongWord, maximal UTF-8 encoded authorization reason/revoke length (" + tlds[0x0006]!!.data[3] + ")"
                        if (tlds[0x0007] != null) s += "\n LongWord, maximal user/developer sTLDs count in one item (" + tlds[0x0007]!!.data[3] + ")"
                        if (tlds[0x0008] != null) s += "\n LongWord, maximal user/developer sTLD length (" + tlds[0x0008]!!.data[3] + ")"
                        if (tlds[0x0009] != null) s += "\n LongWord, offline authorization messages (requests, replies, revokes) count waiting for client request (" + tlds[0x0009]!!.data[3] + ")"
                        if (tlds[0x000A] != null) s += "\n Bool, if True then server will automatically remove authorization flag after adding contact (" + tlds[0x000A]!!.data[0] + ")"
                        if (tlds[0x000B] != null) s += "\n LongWord, maximal notes count (" + tlds[0x000B]!!.data[3] + ")"
                        if (tlds[0x000C] != null) s += "\n LongWord, maximal UTF-8 encoded note name length (" + tlds[0x000C]!!.data[3] + ")"
                        if (tlds[0x000D] != null) s += "\n LongWord, maximal UTF-8 encoded note text length (" + tlds[0x000D]!!.data[3] + ")"
                    }
                    OBIMP_BEX_CL_SRV_REPLY -> {
                        s = "Server say CL"
                        /** */
//                            Vector<Byte> bite = new Vector<Byte>();
                        val data = tlds[0x0001]!!.data
                        //                            for(int i =0; i<data.length; i++){
//                            bite.add(i, data[i]);
//                            }
//                            System.out.println(bite.toString());
                        /** */
//                            byte[] data = tlds.get(0x0001).getData();
                        val cl_length = getLength(data[0], data[1], data[2], data[3])
                        s += "\nYour CL ($cl_length items):"
                        val cl = ByteArray(data.size - 4)
                        var j = 4
                        run {
                            var i: Int = 0
                            while (i < cl.size) {
                                cl[i] = data[j]
                                j++
                                i++
                            }
                        }
                        val clist = arrayOfNulls<ContactListItem>(cl_length)
                        val items: HashMap<*, *> = PacketHandler.parseByteToCLItems(cl_length, cl)
                        var i = 0
                        while (i < items.size) {
                            val item = items.keys.toTypedArray()[i] as ContactListItem
                            val props: HashMap<*, *> =
                                PacketHandler.parseByteArrayTosTLDArray(items.values.toTypedArray()[i] as ByteArray)
                            if (item is Contact) { // Contact
                                item.accountName = String((props[0x0002] as STLD?)!!.data)
                                item.contactName = String((props[0x0003] as STLD?)!!.data)
                                item.privacyType = (props[0x0004] as STLD?)!!.data[0].toInt()
                                if (props[0x0005] as STLD? != null) item.authorization =
                                    if ((props[0x0005] as STLD?)!!.data.size == 0) "требуется авторизация" else "авторизован"
                            } else if (item is Group) { // Group
                                item.name = String((props[0x0001] as STLD?)!!.data)
                            } else if (item is Transport) { // Transport
                                //sTLD 0x1002: UUID, transport UUID
                                item.accountName = String((props[0x1003] as STLD?)!!.data)
                                item.friendlyName = String((props[0x1004] as STLD?)!!.data)
                            } else if (item is Note) { // Note
                                item.noteName = String((props[0x2001] as STLD?)!!.data)
                                item.noteType = (props[0x2002] as STLD?)!!.data[0].toInt()
                                if (props[0x2003] as STLD? != null) item.noteText =
                                    String((props[0x2003] as STLD?)!!.data)
                                //sTLD 0x2004: DateTime, note date (UTC)
                                //sTLD 0x2005: OctaWord, note picture MD5 hash
                            }
                            clist[i] = item
                            i++
                        }
                        for (cll: ContactListListener in oc.cl_list) {
                            cll.onLoadContactList(clist)
                        }
                    }
                    OBIMP_BEX_CL_SRV_VERIFY_REPLY -> s = "Server say CL VERIFY"
                    OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY -> {
                        s = "Server say CL ADD ITEM"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0000 -> s += " - ADD_RES_SUCCESS"
                                0x0001 -> s += " - ADD_RES_ERROR_WRONG_ITEM_TYPE"
                                0x0002 -> s += " - ADD_RES_ERROR_WRONG_PARENT_GROUP"
                                0x0003 -> s += " - ADD_RES_ERROR_NAME_LEN_LIMIT"
                                0x0004 -> s += " - ADD_RES_ERROR_WRONG_NAME"
                                0x0005 -> s += " - ADD_RES_ERROR_ITEM_ALREADY_EXISTS"
                                0x0006 -> s += " - ADD_RES_ERROR_ITEM_LIMIT_REACHED"
                                0x0007 -> s += " - ADD_RES_ERROR_BAD_REQUEST"
                                0x0008 -> s += " - ADD_RES_ERROR_BAD_ITEM_STLD"
                                0x0009 -> s += " - ADD_RES_ERROR_NOT_ALLOWED"
                            }
                        } else s += " - NO ERROR"
                        if (tlds[0x0002] != null) s += " newly added Item ID (" + tlds[0x0001]!!.data[3] + ")"
                    }
                    OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY -> {
                        s = "Server say CL DEL ITEM"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0000 -> s += " - DEL_RES_SUCCESS"
                                0x0001 -> s += " - DEL_RES_ERROR_NOT_FOUND"
                                0x0002 -> s += " - DEL_RES_ERROR_NOT_ALLOWED"
                                0x0003 -> s += " - DEL_RES_ERROR_GROUP_NOT_EMPTY"
                            }
                        } else s += " - NO ERROR"
                    }
                    OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY -> {
                        s = "Server say CL UPD ITEM"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0000 -> s += " - UPD_RES_SUCCESS"
                                0x0001 -> s += " - UPD_RES_ERROR_NOT_FOUND"
                                0x0002 -> s += " - UPD_RES_ERROR_WRONG_PARENT_GROUP"
                                0x0003 -> s += " - UPD_RES_ERROR_NAME_LEN_LIMIT"
                                0x0004 -> s += " - UPD_RES_ERROR_WRONG_NAME"
                                0x0005 -> s += " - UPD_RES_ERROR_ITEM_ALREADY_EXISTS"
                                0x0006 -> s += " - UPD_RES_ERROR_BAD_REQUEST"
                                0x0007 -> s += " - UPD_RES_ERROR_BAD_ITEM_STLD"
                                0x0008 -> s += " - UPD_RES_ERROR_NOT_ALLOWED"
                            }
                        } else s += " - NO ERROR"
                    }
                    OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST -> {
                        val userid = String(tlds[0x0001]!!.data)
                        val reason = String(tlds[0x0002]!!.data)
                        for (cll: ContactListListener in oc.cl_list) {
                            cll.onAuthRequest(userid, reason)
                        }
                    }
                    OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY -> {
                        val us_id = String(tlds[0x0001]!!.data)
                        val reply = if (tlds[0x0002]!!.data[1].toInt() == 0x01) true else false
                        for (cll: ContactListListener in oc.cl_list) {
                            cll.onAuthReply(us_id, reply)
                        }
                    }
                    OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE -> {
                        val uid = String(tlds[0x0001]!!.data)
                        val rsn = String(tlds[0x0002]!!.data)
                        for (cll: ContactListListener in oc.cl_list) {
                            cll.onAuthRevoke(uid, rsn)
                        }
                    }
                    OBIMP_BEX_CL_SRV_DONE_OFFAUTH -> {
                        s = "Server say CL DONE OFFAUTH"
                        oc.send(Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_DEL_OFFAUTH))
                    }
                    OBIMP_BEX_CL_SRV_ITEM_OPER -> {
                        s = "Server say CL ITEM OPER"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0001 -> s += " - OPER_ADD_ITEM"
                                0x0002 -> s += " - OPER_DEL_ITEM"
                                0x0003 -> s += " - OPER_UPD_ITEM"
                            }
                        } else s += " - NO ERROR"
                        if (tlds[0x0002] != null) s += " contact list Item Type (" + tlds[0x0002]!!.data[1] + ")"
                        if (tlds[0x0003] != null) s += " Item ID (" + tlds[0x0003]!!.data[3] + ")"
                        if (tlds[0x0004] != null) s += " Group ID (" + tlds[0x0004]!!.data[3] + ")"
                    }
                    OBIMP_BEX_CL_SRV_BEGIN_UPDATE -> s = "Server say CL BEGIN UPDATE"
                    OBIMP_BEX_CL_SRV_END_UPDATE -> s = "Server say CL END UPDATE"
                }
                OBIMP_BEX_PRES -> when (pkt.getSubtype()) {
                    OBIMP_BEX_PRES_SRV_PARAMS_REPLY -> {
                        s = "Server say PRES PARAMS"
                        if (tlds[0x0001] != null) s += "\n LongWord, maximal UTF-8 encoded status name length (" + tlds[0x0001]!!.data[3] + ")"
                        if (tlds[0x0002] != null) s += "\n LongWord, maximal UTF-8 encoded additional status picture length (" + tlds[0x0002]!!.data[3] + ")"
                        if (tlds[0x0003] != null) s += "\n LongWord, maximal UTF-8 encoded client name length (" + tlds[0x0003]!!.data[3] + ")"
                        if (tlds[0x0004] != null) s += "\n LongWord, maximal capabilities count (" + tlds[0x0004]!!.data[3] + ")"
                        if (tlds[0x0005] != null) s += "\n LongWord, required optional client information flags (see below) (" + tlds[0x0005]!!.data[3] + ")"
                    }
                    OBIMP_BEX_PRES_SRV_CONTACT_ONLINE -> {
                        val user = HashMap<String, String>()
                        val st = tlds[0x0002]!!.data
                        val status = st[0].toInt() + st[1].toInt() + st[2].toInt() + st[3].toInt()
                        user["StatusValue"] = status.toString()
                        val v = tlds[0x0009]!!.data
                        user["Version"] =
                            (v.get(0).toInt() + v.get(1).toInt()).toString() + "." + (v.get(2).toInt() + v.get(3)
                                .toInt()) + "." + (v.get(4).toInt() + v.get(5).toInt()) + "." + (v.get(6)
                                .toInt() + v.get(7).toInt())
                        var caps = "Характеристики клиента:\n"
                        var i = 0
                        while (i < tlds[0x0006]!!.data.size) {
                            if (tlds[0x0006]!!.data[i] > 0) {
                                when (tlds[0x0006]!!.data[i].toInt()) {
                                    0x0001 -> caps += "[UTF-8 сообщения]\n"
                                    0x0002 -> caps += "[RTF сообщения]\n"
                                    0x0003 -> caps += "[HTML сообщения]\n"
                                    0x0004 -> caps += "[Шифрование сообщений]\n"
                                    0x0005 -> caps += "[Оповещение о наборе текста]\n"
                                    0x0006 -> caps += "[Аватары]\n"
                                    0x0007 -> caps += "[Передача файлов]\n"
                                    0x0008 -> caps += "[Транспорты]\n"
                                    0x0009 -> caps += "[Оповещения будильника]\n"
                                    0x000A -> caps += "[Оповещения почты]\n"
                                }
                            }
                            i++
                        }
                        user["Caps"] = caps
                        if (tlds[0x0004] != null) {
                            if (tlds[0x0004]!!.data.size > 0) {
                                user["XTitle"] = X_STATUS.get(tlds.get(0x0004)!!.data.get(3).toInt())
                            }
                        } else user["XTitle"] = X_STATUS.get(0)
                        if (tlds[0x0005] != null) {
                            if (tlds[0x0005]!!.data.size > 0) {
                                user["XDesc"] = String(tlds.get(0x0005)!!.data)
                            }
                        } else user["XDesc"] = X_STATUS.get(0)
                        user["User"] = String(tlds.get(0x0001)!!.data)
                        if (tlds[0x0003] != null) user["Status"] = String(tlds.get(0x0003)!!.data)
                        user["Client"] = String(tlds.get(0x0008)!!.data)
                        user["Os"] = String(tlds.get(0x000F)!!.data)
                        if (tlds[0x0012] != null) {
                            s += "\n BLK, array of client identification sTLDs defined by transport (" + tlds[0x0012]!!.data + ")"
                        }
                        if (tlds[0x1001] != null) {
                            s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                        }
                        if (tlds[0x0011] != null) {
                            s += "\n Byte, custom transport status picture ID (" + tlds[0x0011]!!.data[0] + ")"
                        }
                        if (tlds[0x000A] != null) {
                            s += "\n DateTime, client connected time (" + Date(DataInputStream(ByteArrayInputStream(tlds[0x000A]!!.data)).readLong() * 1000L).toString() + ")"
                        }
                        if (tlds[0x000B] != null) {
                            s += "\n DateTime, registration date (" + Date(DataInputStream(ByteArrayInputStream(tlds[0x000B]!!.data)).readLong() * 1000L).toString() + ")"
                        }
                        for (usl: UserStatusListener in oc.stat_list) {
                            usl.onUserOnline(user)
                        }
                    }
                    OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE -> {
                        val id = String(tlds[0x0001]!!.data)
                        s = "Contact offline $id"
                        if (tlds[0x1001] != null) {
                            s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                        }
                        for (usl: UserStatusListener in oc.stat_list) {
                            usl.onUserOffline(id)
                        }
                    }
                    OBIMP_BEX_PRES_SRV_PRES_INFO -> {
                        s = "Server say PRES INFO"
                        if (tlds[0x0001] != null) if (tlds[0x0001]!!.data.size > 0) s += "\n UTF8, client's account name (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) s += "\n DateTime, registration date (" + Date(
                            DataInputStream(
                                ByteArrayInputStream(
                                    tlds[0x0002]!!.data
                                )
                            ).readLong() * 1000L
                        ).toString() + ")"
                        if (tlds[0x0003] != null) s += "\n DateTime, current session client connected time (" + Date(
                            (DataInputStream(
                                ByteArrayInputStream(
                                    tlds[0x0003]!!.data
                                )
                            )).readLong() * 1000L
                        ).toString() + ")"
                        if (tlds[0x0004] != null) s += "\n DateTime, previous session client connected time (" + Date(
                            (DataInputStream(
                                ByteArrayInputStream(
                                    tlds[0x0004]!!.data
                                )
                            )).readLong() * 1000L
                        ).toString() + ")"
                        if (tlds[0x0005] != null) if (tlds[0x0005]!!.data.size > 0) s += "\n UTF8, current session client IP address (as it seen by server) (" + String(
                            tlds[0x0005]!!.data
                        ) + ")"
                        if (tlds[0x0006] != null) if (tlds[0x0006]!!.data.size > 0) s += "\n UTF8, previous session client IP address (as it seen by server) (" + String(
                            tlds[0x0006]!!.data
                        ) + ")"
                        if (tlds[0x0007] != null) s += "\n Word, currently signed on instances count (" + tlds[0x0007]!!.data[1] + ")"
                        if (tlds[0x0008] != null) if (tlds[0x0008]!!.data.size > 0) s += "\n UTF8, additional server added description (" + String(
                            tlds[0x0008]!!.data
                        ) + ")"
                        if (tlds[0x0064] != null) {
                            s += "\n BLK, presence information data array (see below how it can be parsed) ( КЛ )"
                            //                             for(int i=0; i<((BLK)tlds.get(0x0064)).getData().length; i++)
//                                 s += ((BLK)tlds.get(0x0064)).getData()[i]+",";
                        }
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                    }
                    OBIMP_BEX_PRES_SRV_MAIL_NOTIF -> {
                        s = "Server say MAIL NOTIF"
                        if (tlds[0x0003] != null) s += "\n UTF8, receiver email address (" + String(tlds[0x0003]!!.data) + ")"
                        if (tlds[0x0004] != null) s += "\n UTF8, sender name (" + String(tlds[0x0004]!!.data) + ")"
                        if (tlds[0x0005] != null) s += "\n UTF8, sender email address (" + String(tlds[0x0005]!!.data) + ")"
                        if (tlds[0x0006] != null) s += "\n UTF8, mail subject (" + String(tlds[0x0006]!!.data) + ")"
                        if (tlds[0x0007] != null) s += "\n UTF8, mail text (" + String(tlds[0x0007]!!.data) + ")"
                        if (tlds[0x0008] != null) s += "\n UTF8, mailbox URL (" + String(tlds[0x0008]!!.data) + ")"
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                    }
                    OBIMP_BEX_PRES_SRV_OWN_MAIL_URL -> {
                        s = "Server say OWN MAIL URL "
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                    }
                }
                OBIMP_BEX_IM -> when (pkt.getSubtype()) {
                    OBIMP_BEX_IM_SRV_PARAMS_REPLY -> {
                        s = "Server say IM PARAMS"
                        if (tlds[0x0001] != null) s += "\n LongWord, maximal UTF-8 encoded account name length (" + tlds[0x0001]!!.data[3] + ")"
                        if (tlds[0x0002] != null) s += "\n LongWord, maximal message data length (" + tlds[0x0002]!!.data[3] + ")"
                        if (tlds[0x0003] != null) s += "\n LongWord, offline messages count waiting for client request (" + tlds[0x0003]!!.data[3] + ")"
                        if (tlds[0x0004] != null) s += "\n LongWord, if True then client can send multiple message BEX (" + tlds[0x0004]!!.data[0] + ")"
                    }
                    OBIMP_BEX_IM_SRV_DONE_OFFLINE -> {
                        s = "Server say IM DONE OFFLINE"
                        oc.send(Packet(OBIMP_BEX_IM, OBIMP_BEX_IM_CLI_DEL_OFFLINE))
                    }
                    OBIMP_BEX_IM_SRV_MESSAGE -> {
                        val id = String(tlds[0x0001]!!.data)
                        val text = String(tlds[0x0004]!!.data)
                        s = "New message from $id: $text"
                        //OBIMP.set_upd_info(oc, "jimbot");
                        for (ml: MessageListener in oc.msg_list) {
                            ml.onIncomingMessage(id, text)
                        }
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of message sender (" + String(tlds[0x0001]!!.data) + ")"
                        if (tlds[0x0002] != null) s += "\n LongWord, unique message ID (" + tlds[0x0002]!!.data[3] + ")"
                        if (tlds[0x0003] != null) s += "\n LongWord, message type (" + tlds[0x0003]!!.data[3] + ")"
                        if (tlds[0x0004] != null) s += "\n BLK, message data (" + String(tlds[0x0004]!!.data) + ")"
                        if (tlds[0x0005] != null) s += "\n empty, request message delivery report from remote client (" + tlds[0x0005]!!.data.size + ")"
                        if (tlds[0x0006] != null) s += "\n LongWord, encryption type (see corresponding BEX for types) (" + tlds[0x0006]!!.data[3] + ")"
                        if (tlds[0x0007] != null) s += "\n empty, offline message flag (" + tlds[0x0007]!!.data.size + ")"
                        if (tlds[0x0008] != null) s += "\n DateTime, offline message time (" + Date(
                            (DataInputStream(
                                ByteArrayInputStream(
                                    tlds[0x0008]!!.data
                                )
                            )).readLong() * 1000L
                        ).toString() + ")"
                        if (tlds[0x0009] != null) s += "\n empty, system message flag (" + tlds[0x0009]!!.data.size + ")"
                        if (tlds[0x000A] != null) s += "\n Byte, system message popup position (0 - default, 1 - screen center) (" + tlds[0x000A]!!.data[0] + ")"
                        if (tlds[0x000B] != null) s += "\n empty, multiple message flag (" + tlds[0x000B]!!.data.size + ")"
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                    }
                    OBIMP_BEX_IM_CLI_SRV_MSG_REPORT -> {
                        s = "Server say IM MSG REPORT"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of delivery report receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) s += "\n LongWord, unique received message ID (" + tlds[0x0002]!!.data[3] + ")"
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                    }
                    OBIMP_BEX_IM_CLI_SRV_NOTIFY -> {
                        s = "Server say IM NOTIFY"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of notification receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) {
                            s += "\n LongWord, notification type (see below) = "
                            when (tlds[0x0002]!!.data[3].toInt()) {
                                0x0001 -> s += "NOTIF_VALUE_USER_TYPING_START"
                                0x0002 -> s += "NOTIF_VALUE_USER_TYPING_FINISH"
                                0x0003 -> s += "NOTIF_VALUE_WAKE_ALARM_PLAY"
                                0x0004 -> s += "NOTIF_VALUE_WAKE_ALARM_WAIT"
                            }
                        }
                        if (tlds[0x0003] != null) {
                            s += "\n LongWord, notification value (see below) = "
                            when (tlds[0x0003]!!.data[3].toInt()) {
                                0x0001 -> s += "NOTIF_TYPE_USER_TYPING"
                                0x0002 -> s += "NOTIF_TYPE_WAKE_ALARM"
                            }
                        }
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                    }
                    OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REQ -> {
                        s = "Server say IM ENCRYPT KEY REQ"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of public key request receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                    }
                    OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY -> {
                        s = "Server say IM ENCRYPT KEY REPLY"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of public key receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) {
                            s += "\n LongWord, encryption type (see below) = "
                            when (tlds[0x0002]!!.data[3].toInt()) {
                                0x0000 -> s += "(encryption disabled or not supported)"
                                0x0001 -> s += "(OBIMP client default internal encryption)"
                                0x0002 -> s += "(currently is not used)"
                            }
                        }
                        if (tlds[0x0003] != null) s += "\n BLK, public key (" + tlds[0x0003]!!.data.size + ")"
                    }
                }
                OBIMP_BEX_UD -> when (pkt.getSubtype()) {
                    OBIMP_BEX_UD_SRV_PARAMS_REPLY -> {
                        s = "Server say UD PARAMS"
                        if (tlds[0x0001] != null) s += "\n LongWord, maximal UTF-8 encoded account name length (" + tlds[0x0001]!!.data[3] + ")"
                        if (tlds[0x0002] != null) s += "\n LongWord, maximal UTF-8 encoded details field length (" + tlds[0x0002]!!.data[3] + ")"
                        if (tlds[0x0003] != null) s += "\n LongWord, maximal UTF-8 encoded about field length (" + tlds[0x0003]!!.data[3] + ")"
                        if (tlds[0x0004] != null) s += "\n Bool, if True then client can change account's secure email (" + tlds[0x0004]!!.data.size + ")"
                        if (tlds[0x0005] != null) s += "\n Bool, if True then client can change account's password (" + tlds[0x0005]!!.data.size + ")"
                        if (tlds[0x0006] != null) s += "\n UTF8, secure email/password changing URL if available (" + String(
                            tlds[0x0006]!!.data
                        ) + ")"
                    }
                    OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY -> {
                        s = "Server say UD DETAILS REQ"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0000 -> s += " - DETAILS_RES_SUCCESS"
                                0x0001 -> s += " - DETAILS_RES_NOT_FOUND"
                                0x0002 -> s += " - DETAILS_RES_TOO_MANY_REQUESTS"
                                0x0003 -> s += " - DETAILS_RES_SERVICE_TEMP_UNAVAILABLE"
                            }
                        } else s += " - NO ERROR"
                        var out_inf: String = "User info:"
                        if ((String(tlds[0x0002]!!.data) == username)) {
                        } else {
                            if (tlds[0x0002] != null) if (tlds[0x0002]!!.data.size > 0) out_inf += "\naccount name: " + String(
                                tlds[0x0002]!!.data
                            )
                            if (tlds[0x0003] != null) if (tlds[0x0003]!!.data.size > 0) out_inf += "\nsecure email: " + String(
                                tlds[0x0003]!!.data
                            )
                            if (tlds[0x0004] != null) if (tlds[0x0004]!!.data.size > 0) out_inf += "\nnick name: " + String(
                                tlds[0x0004]!!.data
                            )
                            if (tlds[0x0005] != null) if (tlds[0x0005]!!.data.size > 0) out_inf += "\nfirst name: " + String(
                                tlds[0x0005]!!.data
                            )
                            if (tlds[0x0006] != null) if (tlds[0x0006]!!.data.size > 0) out_inf += "\nlast name: " + String(
                                tlds[0x0006]!!.data
                            )
                            if (tlds[0x0008] != null) if (tlds[0x0008]!!.data.size > 0) out_inf += "\nregion/state: " + String(
                                tlds[0x0008]!!.data
                            )
                            if (tlds[0x0009] != null) if (tlds[0x0009]!!.data.size > 0) out_inf += "\ncity: " + String(
                                tlds[0x0009]!!.data
                            )
                            if (tlds[0x000A] != null) if (tlds[0x000A]!!.data.size > 0) out_inf += "\nzip code: " + String(
                                tlds[0x000A]!!.data
                            )
                            if (tlds[0x000B] != null) if (tlds[0x000B]!!.data.size > 0) out_inf += "\naddress: " + String(
                                tlds[0x000B]!!.data
                            )
                            if (tlds[0x0010] != null) if (tlds[0x0010]!!.data.size > 0) out_inf += "\nhomepage: " + String(
                                tlds[0x0010]!!.data
                            )
                            if (tlds[0x0011] != null) if (tlds[0x0011]!!.data.size > 0) out_inf += "\nabout: " + String(
                                tlds[0x0011]!!.data
                            )
                            if (tlds[0x0012] != null) if (tlds[0x0012]!!.data.size > 0) out_inf += "\ninterests: " + String(
                                tlds[0x0012]!!.data
                            )
                            if (tlds[0x0013] != null) if (tlds[0x0013]!!.data.size > 0) out_inf += "\nemail: " + String(
                                tlds[0x0013]!!.data
                            )
                            if (tlds[0x0014] != null) if (tlds[0x0014]!!.data.size > 0) out_inf += "\nadditional email: " + String(
                                tlds[0x0014]!!.data
                            )
                            if (tlds[0x0015] != null) if (tlds[0x0015]!!.data.size > 0) out_inf += "\nhome phone: " + String(
                                tlds[0x0015]!!.data
                            )
                            if (tlds[0x0016] != null) if (tlds[0x0016]!!.data.size > 0) out_inf += "\nwork phone: " + String(
                                tlds[0x0016]!!.data
                            )
                            if (tlds[0x0017] != null) if (tlds[0x0017]!!.data.size > 0) out_inf += "\ncellular phone: " + String(
                                tlds[0x0017]!!.data
                            )
                            if (tlds[0x0018] != null) if (tlds[0x0018]!!.data.size > 0) out_inf += "\nfax number: " + String(
                                tlds[0x0018]!!.data
                            )
                            if (tlds[0x0019] != null) out_inf += "\nif True then online status will not be shown in users directory: " + tlds[0x0019]!!.data.size
                            if (tlds[0x001A] != null) if (tlds[0x001A]!!.data.size > 0) out_inf += "\ncompany: " + String(
                                tlds[0x001A]!!.data
                            )
                            if (tlds[0x001B] != null) if (tlds[0x001B]!!.data.size > 0) out_inf += "\ndivision/department: " + String(
                                tlds[0x001B]!!.data
                            )
                            if (tlds[0x001C] != null) if (tlds[0x001C]!!.data.size > 0) out_inf += "\nposition: " + String(
                                tlds[0x001C]!!.data
                            )
                            if (tlds[0x1001] != null) out_inf += "\ntransport Item ID: " + tlds[0x1001]!!.data[3]
                            for (ml: MetaInfoListener in oc.user_info) {
                                ml.onUserInfo((out_inf))
                            }
                        }
                    }
                    OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY -> {
                        s = "Server say UD DETAILS UPD"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0000 -> s += " - UPD_DETAILS_RES_SUCCESS"
                                0x0001 -> s += " - UPD_DETAILS_RES_BAD_REQUEST"
                                0x0002 -> s += " - UPD_DETAILS_RES_SERVICE_TEMP_UNAVAILABLE"
                                0x0003 -> s += " - UPD_DETAILS_RES_NOT_ALLOWED"
                            }
                        } else s += " - NO ERROR"
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID: " + tlds[0x1001]!!.data[3]
                    }
                    OBIMP_BEX_UD_SRV_SEARCH_REPLY -> {
                        s = "Server say UD SEARCH"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0000 -> s += " - SEARCH_RES_SUCCESS"
                                0x0001 -> s += " - SEARCH_RES_NOT_FOUND"
                                0x0002 -> s += " - SEARCH_RES_BAD_REQUEST"
                                0x0003 -> s += " - SEARCH_RES_TOO_MANY_REQUESTS"
                                0x0004 -> s += " - SEARCH_RES_SERVICE_TEMP_UNAVAILABLE"
                            }
                        } else s += " - NO ERROR"
                        var out_search: String = ""
                        if (tlds[0x0002] != null) if (tlds[0x0002]!!.data.size > 0) out_search += "\naccount name: " + String(
                            tlds[0x0002]!!.data
                        )
                        if (tlds[0x0003] != null) if (tlds[0x0003]!!.data.size > 0) out_search += "\nnick name: " + String(
                            tlds[0x0003]!!.data
                        )
                        if (tlds[0x0004] != null) if (tlds[0x0004]!!.data.size > 0) out_search += "\nfirst name: " + String(
                            tlds[0x0004]!!.data
                        )
                        if (tlds[0x0005] != null) if (tlds[0x0005]!!.data.size > 0) out_search += "\nlast name: " + String(
                            tlds[0x0005]!!.data
                        )
                        if (tlds[0x0006] != null) if (tlds[0x0006]!!.data.size > 0) {
                            when (tlds[0x0006]!!.data[0].toInt()) {
                                0x00 -> out_search += "\ngender: not specified"
                                0x01 -> out_search += "\ngender: female"
                                0x02 -> out_search += "\ngender: male"
                            }
                        }
                        if (tlds[0x0007] != null) if (tlds[0x0007]!!.data[0] > 13) out_search += "\nage: " + Integer.toString(
                            tlds[0x0007]!!.data[0].toInt()
                        )
                        if (tlds[0x0008] != null) {
                            if (String(tlds[0x0008]!!.data).isEmpty()) out_search += "\nuser: online"
                        } else out_search += "\nuser: offline"
                        if (tlds[0x0009] != null) s += "\n empty, last search result (" + tlds[0x0009]!!.data.size + ")"
                        if (tlds[0x000A] != null) s += "\n LongWord, total count of results in DB (" + tlds[0x000A]!!.data[3] + ")"
                        if (tlds[0x000B] != null) s += "\n LongWord, status picture flags and index starting from 1 (see below) (" + tlds[0x000B]!!.data[3] + ")"
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                        for (ml: MetaInfoListener in oc.user_info) {
                            ml.onSearch((out_search))
                        }
                    }
                    OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY -> {
                        s = "Server say UD SECURE UPD"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0000 -> s += " - UPD_SECURE_RES_SUCCESS"
                                0x0001 -> s += " - UPD_SECURE_RES_BAD_REQUEST"
                                0x0002 -> s += " - UPD_SECURE_RES_SERVICE_TEMP_UNAVAILABLE"
                                0x0003 -> s += " - UPD_SECURE_RES_NOT_ALLOWED"
                            }
                        } else s += " - NO ERROR"
                    }
                }
                OBIMP_BEX_UA -> when (pkt.getSubtype()) {
                    OBIMP_BEX_UA_SRV_PARAMS_REPLY -> {
                        s = "Server say UA PARAMS"
                        if (tlds[0x0001] != null) s += "\n LongWord, maximal avatar file size (" + tlds[0x0001]!!.data[3] + ")"
                        if (tlds[0x0002] != null) {
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += "\n OctaWord, current client avatar file MD5 hash (" + tlds[0x0002]!!.data[i] + ")"
                                i++
                            }
                        }
                    }
                    OBIMP_BEX_UA_SRV_AVATAR_REPLY -> {
                        s = "Server say UA AVATAR"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0000 -> s += " - AVATAR_REQ_SUCCESS"
                                0x0001 -> s += " - AVATAR_REQ_NOT_FOUND"
                                0x0002 -> s += " - AVATAR_REQ_NOT_ALLOWED"
                            }
                        } else s += " - NO ERROR"
                        if (tlds[0x0002] != null) {
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += "\n OctaWord, avatar file MD5 hash (" + tlds[0x0002]!!.data[i] + ")"
                                i++
                            }
                        }
                        if (tlds[0x0003] != null) {
                            var i = 0
                            while (i < tlds[0x0003]!!.data.size) {
                                s += "\n BLK, avatar file (" + tlds[0x0003]!!.data[i] + ")"
                                i++
                            }
                        }
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                    }
                    OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY -> {
                        s = "Server say UA AVATAR SET"
                        if (tlds[0x0001] != null) {
                            when (tlds[0x0001]!!.data[1].toInt()) {
                                0x0000 -> s += " - AVATAR_SET_SUCCESS"
                                0x0001 -> s += " - AVATAR_SET_BAD_MD5"
                                0x0002 -> s += " - AVATAR_SET_NOT_ALLOWED"
                                0x0003 -> s += " - AVATAR_SET_TEMP_UNAVAILABLE"
                                0x0004 -> s += " - AVATAR_SET_TOO_BIG"
                                0x0005 -> s += " - AVATAR_SET_TOO_SMALL"
                                0x0006 -> s += " - AVATAR_SET_BANNED"
                                0x0007 -> s += " - AVATAR_SET_INVALID_TYPE"
                                0x0008 -> s += " - AVATAR_SET_OTHER_ERROR"
                            }
                        } else s += " - NO ERROR"
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                    }
                }
                OBIMP_BEX_FT -> when (pkt.getSubtype()) {
                    OBIMP_BEX_FT_SRV_PARAMS_REPLY -> {
                        s = "Server say FT PARAMS"
                        if (tlds[0x0001] != null) s += "\n LongWord, maximal UTF-8 encoded account name length (" + tlds[0x0001]!!.data[3] + ")"
                        if (tlds[0x0002] != null) s += "\n LongWord, maximal UTF-8 encoded host/IP length (" + tlds[0x0002]!!.data[3] + ")"
                        if (tlds[0x0003] != null) s += "\n LongWord, maximal UTF-8 encoded file name length (" + tlds[0x0003]!!.data[3] + ")"
                        if (tlds[0x0004] != null) s += "\n LongWord, maximal UTF-8 encoded file path length (" + tlds[0x0004]!!.data[3] + ")"
                        if (tlds[0x0005] != null) s += "\n Bool, if value is True then FT support is enabled (" + tlds[0x0005]!!.data[0] + ")"
                        if (tlds[0x0006] != null) s += "\n Bool, if value is True then proxied file transfer support is enabled (" + tlds[0x0006]!!.data[0] + ")"
                        if (tlds[0x0007] != null) s += "\n UTF8, file proxy server host/IP (" + String(tlds[0x0007]!!.data) + ")"
                        if (tlds[0x0008] != null) s += "\n LongWord, file proxy server port number (" + tlds[0x0008]!!.data[3] + ")"
                    }
                    OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST -> {
                        s = "Server say FT SEND FILE REQUEST"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of file(s) receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) {
                            s += "\n QuadWord, unique file transfer ID ("
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += tlds[0x0002]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                        if (tlds[0x0001] != null) s += "\n LongWord, files count (" + tlds[0x0003]!!.data[3] + ")"
                        if (tlds[0x0004] != null) {
                            s += "\n QuadWord, file size or total size of the all files if more than one ("
                            var i = 0
                            while (i < tlds[0x0004]!!.data.size) {
                                s += tlds[0x0004]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                        if (tlds[0x0005] != null) s += "\n UTF8, file name of file name of the first file if more than one (" + String(
                            tlds[0x0005]!!.data
                        ) + ")"
                        if (tlds[0x0006] != null) s += "\n UTF8, client's IP address of sender (" + String(tlds[0x0006]!!.data) + ")"
                        if (tlds[0x0007] != null) s += "\n LongWord, client's port number of sender that is listening for direct FT connection (" + tlds[0x0007]!!.data[3] + ")"
                        if (tlds[0x0008] != null) s += "\n UTF8, file proxy server host/IP (" + String(tlds[0x0008]!!.data) + ")"
                        if (tlds[0x0009] != null) s += "\n LongWord, file proxy server port number (" + tlds[0x0009]!!.data[3] + ")"
                    }
                    OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY -> {
                        s = "Server say FT SEND FILE REPLY"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of file(s) receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) {
                            s += "\n QuadWord, unique received file transfer ID ("
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += tlds[0x0002]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                        if (tlds[0x0003] != null) {
                            s += "\n Word, FT reply code ("
                            when (tlds[0x0003]!!.data[1].toInt()) {
                                0x0001 -> s += "FT_REPLY_CODE_ACCEPT"
                                0x0002 -> s += "FT_REPLY_CODE_DECLINE"
                                0x0003 -> s += "FT_REPLY_CODE_DISABLED"
                                0x0004 -> s += "FT_REPLY_CODE_NOT_ALLOWED"
                            }
                            s += ")"
                        }
                        if (tlds[0x0004] != null) s += "\n UTF8, client's IP address of receiver (" + String(tlds[0x0004]!!.data) + ")"
                        if (tlds[0x0005] != null) s += "\n LongWord, client's port number of receiver that is listening for direct FT connection (" + tlds[0x0005]!!.data[3] + ")"
                    }
                    OBIMP_BEX_FT_CLI_SRV_CONTROL -> {
                        s = "Server say FT CONTROL"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of file(s) receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) {
                            s += "\n QuadWord, unique received file transfer ID ("
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += tlds[0x0002]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                        if (tlds[0x0003] != null) {
                            s += "\n Word, FT control code ("
                            when (tlds[0x0003]!!.data[1].toInt()) {
                                0x0001 -> s += "FT_CONTROL_CODE_CANCEL"
                                0x0002 -> s += "FT_CONTROL_CODE_DIRECT_FAILED"
                                0x0003 -> s += "FT_CONTROL_CODE_DIRECT_FAILED_TRY_REVERSE"
                                0x0004 -> s += "FT_CONTROL_CODE_DIRECT_FAILED_TRY_PROXY"
                                0x0005 -> s += "FT_CONTROL_CODE_PROXY_FAILED"
                                0x0006 -> s += "FT_CONTROL_CODE_READY"
                            }
                            s += ")"
                        }
                    }
                    OBIMP_BEX_FT_DIR_PROX_ERROR -> {
                        s = "Server say FT DIR PROX ERROR"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of file(s) receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) {
                            s += "\n QuadWord, unique received file transfer ID ("
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += tlds[0x0002]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                        if (tlds[0x0003] != null) {
                            s += "\n Word, FT error code ("
                            when (tlds[0x0003]!!.data[1].toInt()) {
                                0x0001 -> s += "FT_ERROR_CODE_TIMEOUT"
                                0x0002 -> s += "FT_ERROR_CODE_WRONG_UNIQ_FT_ID"
                                0x0003 -> s += "FT_ERROR_CODE_WRONG_FILE_NAME"
                                0x0004 -> s += "FT_ERROR_CODE_WRONG_RELATIVE_PATH"
                                0x0005 -> s += "FT_ERROR_CODE_WRONG_RESUME_POS"
                                0x0006 -> s += "FT_ERROR_CODE_PROXY_TRAFFIC_LIMIT"
                            }
                            s += ")"
                        }
                    }
                    OBIMP_BEX_FT_DIR_PROX_HELLO -> {
                        s = "Server say FT DIR PROX HELLO"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of file(s) receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) {
                            s += "\n QuadWord, unique file transfer ID ("
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += tlds[0x0002]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                    }
                    OBIMP_BEX_FT_DIR_PROX_FILE -> {
                        s = "Server say FT DIR PROX FILE"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of file(s) receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) {
                            s += "\n QuadWord, unique file transfer ID ("
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += tlds[0x0002]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                        if (tlds[0x0003] != null) {
                            s += "\n QuadWord, file size  ("
                            var i = 0
                            while (i < tlds[0x0003]!!.data.size) {
                                s += tlds[0x0003]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                        if (tlds[0x0004] != null) s += "\n UTF8, file name(" + String(tlds[0x0004]!!.data) + ")"
                        if (tlds[0x0005] != null) s += "\n UTF8, relative file path(if sending folders with files) = (" + String(
                            tlds[0x0005]!!.data
                        ) + ")"
                    }
                    OBIMP_BEX_FT_DIR_PROX_FILE_REPLY -> {
                        s = "Server say FT DIR PROX FILE REPLY"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of file(s) receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) {
                            s += "\n QuadWord, unique file transfer ID ("
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += tlds[0x0002]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                        if (tlds[0x0003] != null) {
                            s += "\n QuadWord, resume position where from start sending file data ("
                            var i = 0
                            while (i < tlds[0x0003]!!.data.size) {
                                s += tlds[0x0003]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                    }
                    OBIMP_BEX_FT_DIR_PROX_FILE_DATA -> {
                        s = "Server say FT DIR PROX FILE DATA"
                        if (tlds[0x0001] != null) s += "\n UTF8, account name of file(s) receiver/sender (" + String(
                            tlds[0x0001]!!.data
                        ) + ")"
                        if (tlds[0x0002] != null) {
                            s += "\n QuadWord, unique file transfer ID ("
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += tlds[0x0002]!!.data[i]
                                i++
                            }
                            s += ")"
                        }
                        if (tlds[0x0003] != null) s += "\n Bool, if True then this is the last file (" + tlds[0x0003]!!.data[1] + ")"
                        if (tlds[0x0004] != null) s += "\n Bool, if True then this is the last part of file (" + tlds[0x0004]!!.data[1] + ")"
                        if (tlds[0x0005] != null) s += "\n UTF8, account name of file(s) receiver/sender (" + tlds[0x0005]!!.data.size + ")"
                    }
                }
                OBIMP_BEX_TP -> when (pkt.getSubtype()) {
                    OBIMP_BEX_TP_SRV_PARAMS_REPLY -> s = "Server say TP PARAMS"
                    OBIMP_BEX_TP_SRV_ITEM_READY -> s = "Server say TP ITEM READY"
                    OBIMP_BEX_TP_SRV_SETTINGS_REPLY -> {
                        s = "Server say TP SETTINGS"
                        if (tlds[0x0002] != null) {
                            when (tlds[0x0002]!!.data[1].toInt()) {
                                0x0000 -> s += " - TP_SET_RES_SUCCESS"
                                0x0001 -> s += " - TP_SET_RES_ERROR_WRONG_ID"
                                0x0002 -> s += " - TP_SET_RES_ERROR_NOT_FOUND"
                                0x0003 -> s += " - TP_SET_RES_ERROR_NOT_ALLOWED"
                            }
                        } else s += " - NO ERROR"
                    }
                    OBIMP_BEX_TP_SRV_TRANSPORT_INFO -> {
                        s = "Server say TP TRANSPORT INFO"
                        if (tlds[0x0002] != null) {
                            when (tlds[0x0002]!!.data[1].toInt()) {
                                0x0000 -> s += " - TP_STATE_LOGGEDIN"
                                0x0001 -> s += " - TP_STATE_LOGGEDOFF"
                                0x0002 -> s += " - TP_STATE_STATUS_CHANGED"
                                0x0003 -> s += " - TP_STATE_CON_FAILED"
                                0x0004 -> s += " - TP_STATE_ACCOUNT_INVALID"
                                0x0005 -> s += " - TP_STATE_SERVICE_TEMP_UNAVAILABLE"
                                0x0006 -> s += " - TP_STATE_WRONG_PASSWORD"
                                0x0007 -> s += " - TP_STATE_INVALID_LOGIN"
                                0x0008 -> s += " - TP_STATE_OTHER_PLACE_LOGIN"
                                0x0009 -> s += " - TP_STATE_CANT_LOGIN_TRY_LATER"
                                0x000A -> s += " - TP_STATE_SRV_PAUSED"
                                0x000B -> s += " - TP_STATE_SRV_RESUMED"
                                0x000C -> s += " - TP_STATE_SRV_MIGRATED"
                            }
                        } else s += " - NO ERROR"
                    }
                    OBIMP_BEX_TP_SRV_SHOW_NOTIF -> {
                        s = "Server say TP SHOW NOTIF"
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                        if (tlds[0x0002] != null) s += "\n Bool, auto close notificaton after some seconds (" + tlds[0x0002]!!.data[0] + ")"
                        if (tlds[0x0003] != null) s += "\n Bool, error/warning notification style (" + tlds[0x0003]!!.data[0] + ")"
                        if (tlds[0x0004] != null) s += "\n UTF8, notification title text (" + String(tlds[0x0004]!!.data) + ")"
                        if (tlds[0x0005] != null) s += "\n UTF8, notification content text (" + String(tlds[0x0005]!!.data) + ")"
                    }
                    OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH -> {
                        s = "Server say TP OWN AVATAR HASH"
                        if (tlds[0x1001] != null) s += "\n LongWord, transport Item ID (" + tlds[0x1001]!!.data[3] + ")"
                        if (tlds[0x0002] != null) {
                            var i = 0
                            while (i < tlds[0x0002]!!.data.size) {
                                s += "\n OctaWord, avatar MD5 hash (" + tlds[0x0002]!!.data[i] + ")"
                                i++
                            }
                        }
                    }
                }
                else -> for (b: Byte in pkt.asByteArray(OBIMP_BEX_CL)) {
                    s += "$b "
                }
            }

            if (s != "" && OBIMPConnection.debug) {
                System.err.println("[DEBUG] $s")
            }
        }

        fun MD5(b: ByteArray): ByteArray {
            var hash: ByteArray? = null
            try {
                val md = MessageDigest.getInstance("MD5")
                md.update(b)
                hash = md.digest()
            } catch (ex: Exception) {
                println("Error:$ex")
            }
            return hash!!
        }

        private fun parseByteArrayTosTLDArray(data: ByteArray): HashMap<Int, STLD> {
            val result = HashMap<Int, STLD>()
            var data = data
            while (data.size > 0) {
                val type = data[0].toInt() + data[1].toInt()
                val length = data[2].toInt() + data[3].toInt()
                val value = ByteArray(length)
                for (i in 4 until length + 4) {
                    value[i - 4] = data[i]
                }
                result[type] = STLD(type, BLK(value))
                val next = ByteArray(data.size - 4 - length)
                for (i in 4 + length until data.size) {
                    next[i - 4 - length] = data[i]
                }
                data = next
            }
            return result
        }

        private fun parseByteToCLItems(count: Int, data: ByteArray): HashMap<ContactListItem, ByteArray> {
            val result = HashMap<ContactListItem, ByteArray>()
            var c = 0
            var data = data
            while (data.size > 0) {
                val type = getLength(0.toByte(), 0.toByte(), data[0], data[1])
                val item_id = getLength(data[2], data[3], data[4], data[5])
                val group_id = getLength(data[6], data[7], data[8], data[9])
                val length = getLength(data[10], data[11], data[12], data[13])
                val value = ByteArray(length)
                for (i in 14 until length + 14) {
                    value[i - 14] = data[i]
                }
                when (type) {
                    0x0001 -> result[Group(item_id, group_id, "")] = value
                    0x0002 -> result[Contact(item_id, group_id, "", "", 0, "")] = value
                    0x0003 -> result[Transport(item_id, group_id, "", "")] = value
                    0x0004 -> result[Note(item_id, group_id, "", 0, "")] = value
                }
                val next = ByteArray(data.size - 14 - length)
                for (i in 14 + length until data.size) {
                    next[i - 14 - length] = data[i]
                }
                data = next
                c++
            }
            return result
        }
    }
}