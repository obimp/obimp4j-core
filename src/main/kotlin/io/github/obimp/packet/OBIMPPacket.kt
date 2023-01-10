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

import io.github.obimp.data.structure.WTLD
import io.github.obimp.packet.body.OBIMPBody
import io.github.obimp.packet.header.OBIMPHeader
import java.nio.ByteBuffer

/**
 * OBIMP Packet
 * @author Alexander Krysin
 */
open class OBIMPPacket(val header: OBIMPHeader, val body: OBIMPBody = OBIMPBody()) : Packet<WTLD> {
    override fun getType() = header.type

    override fun getSubtype() = header.subtype

    override fun hasItems() = body.hasItems()

    override fun addItem(item: WTLD) = body.addItem(item)

    override fun nextItem() = body.nextItem()

    override fun toBytes(): ByteBuffer {
        val headerBytes = header.toBytes()
        val bodyBytes = body.toBytes()
        val capacity = headerBytes.capacity() + bodyBytes.capacity()
        val buffer = ByteBuffer.allocate(capacity)
        buffer.put(headerBytes.array())
        buffer.put(bodyBytes.array())
        buffer.rewind()
        return buffer
    }

    companion object {
        //General BEX types
        const val OBIMP_BEX_COM: Short = 0x0001 // Common
        const val OBIMP_BEX_CL: Short = 0x0002 // Contact list
        const val OBIMP_BEX_PRES: Short = 0x0003 // Presence
        const val OBIMP_BEX_IM: Short = 0x0004 // Instant messaging
        const val OBIMP_BEX_UD: Short = 0x0005 // Users directory
        const val OBIMP_BEX_UA: Short = 0x0006 // User avatars
        const val OBIMP_BEX_FT: Short = 0x0007 // File transfer
        const val OBIMP_BEX_TP: Short = 0x0008 // Transports

        //BEX 0x0001, Common. (OBIMP_BEX_COM)
        const val OBIMP_BEX_COM_CLI_HELLO: Short = 0x0001
        const val OBIMP_BEX_COM_SRV_HELLO: Short = 0x0002
        const val OBIMP_BEX_COM_CLI_LOGIN: Short = 0x0003
        const val OBIMP_BEX_COM_SRV_LOGIN_REPLY: Short = 0x0004
        const val OBIMP_BEX_COM_SRV_BYE: Short = 0x0005
        const val OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING: Short = 0x0006
        const val OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG: Short = 0x0007
        const val OBIMP_BEX_COM_CLI_REGISTER: Short = 0x0008
        const val OBIMP_BEX_COM_SRV_REGISTER_REPLY: Short = 0x0009

        //BEX 0x0002, Contact list. (OBIMP_BEX_CL)
        const val OBIMP_BEX_CL_CLI_PARAMS: Short = 0x0001
        const val OBIMP_BEX_CL_SRV_PARAMS_REPLY: Short = 0x0002
        const val OBIMP_BEX_CL_CLI_REQUEST: Short = 0x0003
        const val OBIMP_BEX_CL_SRV_REPLY: Short = 0x0004
        const val OBIMP_BEX_CL_CLI_VERIFY: Short = 0x0005
        const val OBIMP_BEX_CL_SRV_VERIFY_REPLY: Short = 0x0006
        const val OBIMP_BEX_CL_CLI_ADD_ITEM: Short = 0x0007
        const val OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY: Short = 0x0008
        const val OBIMP_BEX_CL_CLI_DEL_ITEM: Short = 0x0009
        const val OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY: Short = 0x000A
        const val OBIMP_BEX_CL_CLI_UPD_ITEM: Short = 0x000B
        const val OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY: Short = 0x000C
        const val OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST: Short = 0x000D
        const val OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY: Short = 0x000E
        const val OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE: Short = 0x000F
        const val OBIMP_BEX_CL_CLI_REQ_OFFAUTH: Short = 0x0010
        const val OBIMP_BEX_CL_SRV_DONE_OFFAUTH: Short = 0x0011
        const val OBIMP_BEX_CL_CLI_DEL_OFFAUTH: Short = 0x0012
        const val OBIMP_BEX_CL_SRV_ITEM_OPER: Short = 0x0013
        const val OBIMP_BEX_CL_SRV_BEGIN_UPDATE: Short = 0x0014
        const val OBIMP_BEX_CL_SRV_END_UPDATE: Short = 0x0015

        //BEX 0x0003, Presence (OBIMP_BEX_PRES)
        const val OBIMP_BEX_PRES_CLI_PARAMS: Short = 0x0001
        const val OBIMP_BEX_PRES_SRV_PARAMS_REPLY: Short = 0x0002
        const val OBIMP_BEX_PRES_CLI_SET_PRES_INFO: Short = 0x0003
        const val OBIMP_BEX_PRES_CLI_SET_STATUS: Short = 0x0004
        const val OBIMP_BEX_PRES_CLI_ACTIVATE: Short = 0x0005
        const val OBIMP_BEX_PRES_SRV_CONTACT_ONLINE: Short = 0x0006
        const val OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE: Short = 0x0007
        const val OBIMP_BEX_PRES_CLI_REQ_PRES_INFO: Short = 0x0008
        const val OBIMP_BEX_PRES_SRV_PRES_INFO: Short = 0x0009
        const val OBIMP_BEX_PRES_SRV_MAIL_NOTIF: Short = 0x000A
        const val OBIMP_BEX_PRES_CLI_REQ_OWN_MAIL_URL: Short = 0x000B
        const val OBIMP_BEX_PRES_SRV_OWN_MAIL_URL: Short = 0x000C

        //BEX 0x0004, Instant messaging (OBIMP_BEX_IM)
        const val OBIMP_BEX_IM_CLI_PARAMS: Short = 0x0001
        const val OBIMP_BEX_IM_SRV_PARAMS_REPLY: Short = 0x0002
        const val OBIMP_BEX_IM_CLI_REQ_OFFLINE: Short = 0x0003
        const val OBIMP_BEX_IM_SRV_DONE_OFFLINE: Short = 0x0004
        const val OBIMP_BEX_IM_CLI_DEL_OFFLINE: Short = 0x0005
        const val OBIMP_BEX_IM_CLI_MESSAGE: Short = 0x0006
        const val OBIMP_BEX_IM_SRV_MESSAGE: Short = 0x0007
        const val OBIMP_BEX_IM_CLI_SRV_MSG_REPORT: Short = 0x0008
        const val OBIMP_BEX_IM_CLI_SRV_NOTIFY: Short = 0x0009
        const val OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REQ: Short = 0x000A
        const val OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY: Short = 0x000B
        const val OBIMP_BEX_IM_CLI_MULTIPLE_MSG: Short = 0x000C

        //BEX 0x0005, Users directory (OBIMP_BEX_UD)
        const val OBIMP_BEX_UD_CLI_PARAMS: Short = 0x0001
        const val OBIMP_BEX_UD_SRV_PARAMS_REPLY: Short = 0x0002
        const val OBIMP_BEX_UD_CLI_DETAILS_REQ: Short = 0x0003
        const val OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY: Short = 0x0004
        const val OBIMP_BEX_UD_CLI_DETAILS_UPD: Short = 0x0005
        const val OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY: Short = 0x0006
        const val OBIMP_BEX_UD_CLI_SEARCH: Short = 0x0007
        const val OBIMP_BEX_UD_SRV_SEARCH_REPLY: Short = 0x0008
        const val OBIMP_BEX_UD_CLI_SECURE_UPD: Short = 0x0009
        const val OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY: Short = 0x000A

        //BEX 0x0006, User avatars (OBIMP_BEX_UA)
        const val OBIMP_BEX_UA_CLI_PARAMS: Short = 0x0001
        const val OBIMP_BEX_UA_SRV_PARAMS_REPLY: Short = 0x0002
        const val OBIMP_BEX_UA_CLI_AVATAR_REQ: Short = 0x0003
        const val OBIMP_BEX_UA_SRV_AVATAR_REPLY: Short = 0x0004
        const val OBIMP_BEX_UA_CLI_AVATAR_SET: Short = 0x0005
        const val OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY: Short = 0x0006

        //BEX 0x0007, File transfer (OBIMP_BEX_FT)
        const val OBIMP_BEX_FT_CLI_PARAMS: Short = 0x0001
        const val OBIMP_BEX_FT_SRV_PARAMS_REPLY: Short = 0x0002
        const val OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST: Short = 0x0003
        const val OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY: Short = 0x0004
        const val OBIMP_BEX_FT_CLI_SRV_CONTROL: Short = 0x0005
        const val OBIMP_BEX_FT_DIR_PROX_ERROR: Short = 0x0101
        const val OBIMP_BEX_FT_DIR_PROX_HELLO: Short = 0x0102
        const val OBIMP_BEX_FT_DIR_PROX_FILE: Short = 0x0103
        const val OBIMP_BEX_FT_DIR_PROX_FILE_REPLY: Short = 0x0104
        const val OBIMP_BEX_FT_DIR_PROX_FILE_DATA: Short = 0x0105

        //BEX 0x0008, Transports (OBIMP_BEX_TP)
        const val OBIMP_BEX_TP_CLI_PARAMS: Short = 0x0001
        const val OBIMP_BEX_TP_SRV_PARAMS_REPLY: Short = 0x0002
        const val OBIMP_BEX_TP_SRV_ITEM_READY: Short = 0x0003
        const val OBIMP_BEX_TP_CLI_SETTINGS: Short = 0x0004
        const val OBIMP_BEX_TP_SRV_SETTINGS_REPLY: Short = 0x0005
        const val OBIMP_BEX_TP_CLI_MANAGE: Short = 0x0006
        const val OBIMP_BEX_TP_SRV_TRANSPORT_INFO: Short = 0x0007
        const val OBIMP_BEX_TP_SRV_SHOW_NOTIF: Short = 0x0008
        const val OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH: Short = 0x0009
    }
}