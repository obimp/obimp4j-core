/*
 * OBIMP4J - Java OBIMP Lib
 * Copyright (C) 2013 alex_xpert
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

package com.obimp.packet;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Обработчик пакетов
 * @author alex_xpert
 */
public class PacketHandler {
    private static final int OBIMP_BEX_COM = 0x0001; // Common
    private static final int OBIMP_BEX_CL = 0x0002; // Contact list
    private static final int OBIMP_BEX_PRES = 0x0003; // Presence
    private static final int OBIMP_BEX_IM = 0x0004; // Instant messaging
    private static final int OBIMP_BEX_UD = 0x0005; // Users directory
    private static final int OBIMP_BEX_UA = 0x0006; // User avatars
    private static final int OBIMP_BEX_FT = 0x0007; // File transfer
    private static final int OBIMP_BEX_TP = 0x0008; // Transports
    
    private static final int OBIMP_BEX_COM_CLI_HELLO = 0x0001;
    private static final int OBIMP_BEX_COM_SRV_HELLO = 0x0002;
    private static final int OBIMP_BEX_COM_CLI_LOGIN = 0x0003;
    private static final int OBIMP_BEX_COM_SRV_LOGIN_REPLY = 0x0004;
    private static final int OBIMP_BEX_COM_SRV_BYE = 0x0005;
    private static final int OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING = 0x0006;
    private static final int OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG = 0x0007;
    private static final int OBIMP_BEX_COM_CLI_REGISTER = 0x0008;
    private static final int OBIMP_BEX_COM_SRV_REGISTER_REPLY = 0x0009;
    
    private static final int OBIMP_BEX_CL_CLI_PARAMS = 0x0001;
    private static final int OBIMP_BEX_CL_SRV_PARAMS_REPLY = 0x0002;
    private static final int OBIMP_BEX_CL_CLI_REQUEST = 0x0003;
    private static final int OBIMP_BEX_CL_SRV_REPLY = 0x0004;
    private static final int OBIMP_BEX_CL_CLI_VERIFY = 0x0005;
    private static final int OBIMP_BEX_CL_SRV_VERIFY_REPLY = 0x0006;
    private static final int OBIMP_BEX_CL_CLI_ADD_ITEM = 0x0007;
    private static final int OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY = 0x0008;
    private static final int OBIMP_BEX_CL_CLI_DEL_ITEM = 0x0009;
    private static final int OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY = 0x000A;
    private static final int OBIMP_BEX_CL_CLI_UPD_ITEM = 0x000B;
    private static final int OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY = 0x000C;
    private static final int OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST = 0x000D;
    private static final int OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY = 0x000E;
    private static final int OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE = 0x000F;
    private static final int OBIMP_BEX_CL_CLI_REQ_OFFAUTH = 0x0010;
    private static final int OBIMP_BEX_CL_SRV_DONE_OFFAUTH = 0x0011;
    private static final int OBIMP_BEX_CL_CLI_DEL_OFFAUTH = 0x0012;
    private static final int OBIMP_BEX_CL_SRV_ITEM_OPER = 0x0013;
    private static final int OBIMP_BEX_CL_SRV_BEGIN_UPDATE = 0x0014;
    private static final int OBIMP_BEX_CL_SRV_END_UPDATE = 0x0015;

    private static final int OBIMP_BEX_PRES_CLI_PARAMS = 0x0001;
    private static final int OBIMP_BEX_PRES_SRV_PARAMS_REPLY = 0x0002;
    private static final int OBIMP_BEX_PRES_CLI_SET_PRES_INFO = 0x0003;
    private static final int OBIMP_BEX_PRES_CLI_SET_STATUS = 0x0004;
    private static final int OBIMP_BEX_PRES_CLI_ACTIVATE = 0x0005;
    private static final int OBIMP_BEX_PRES_SRV_CONTACT_ONLINE = 0x0006;
    private static final int OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE = 0x0007;
    private static final int OBIMP_BEX_PRES_CLI_REQ_PRES_INFO = 0x0008;
    private static final int OBIMP_BEX_PRES_SRV_PRES_INFO = 0x0009;

    private static final int OBIMP_BEX_IM_CLI_PARAMS = 0x0001;
    private static final int OBIMP_BEX_IM_SRV_PARAMS_REPLY = 0x0002;
    private static final int OBIMP_BEX_IM_CLI_REQ_OFFLINE = 0x0003;
    private static final int OBIMP_BEX_IM_SRV_DONE_OFFLINE = 0x0004;
    private static final int OBIMP_BEX_IM_CLI_DEL_OFFLINE = 0x0005;
    private static final int OBIMP_BEX_IM_CLI_MESSAGE = 0x0006;
    private static final int OBIMP_BEX_IM_SRV_MESSAGE = 0x0007;
    private static final int OBIMP_BEX_IM_CLI_SRV_MSG_REPORT = 0x0008;
    private static final int OBIMP_BEX_IM_CLI_SRV_NOTIFY = 0x0009;
    private static final int OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REQ = 0x000A;
    private static final int OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY = 0x000B;
    private static final int OBIMP_BEX_IM_CLI_MULTIPLE_MSG = 0x000C;

    private static final int OBIMP_BEX_UD_CLI_PARAMS = 0x0001;
    private static final int OBIMP_BEX_UD_SRV_PARAMS_REPLY = 0x0002;
    private static final int OBIMP_BEX_UD_CLI_DETAILS_REQ = 0x0003;
    private static final int OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY = 0x0004;
    private static final int OBIMP_BEX_UD_CLI_DETAILS_UPD = 0x0005;
    private static final int OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY = 0x0006;
    private static final int OBIMP_BEX_UD_CLI_SEARCH = 0x0007;
    private static final int OBIMP_BEX_UD_SRV_SEARCH_REPLY = 0x0008;
    private static final int OBIMP_BEX_UD_CLI_SECURE_UPD = 0x0009;
    private static final int OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY = 0x000A;


    private static final int OBIMP_BEX_UA_CLI_PARAMS = 0x0001;
    private static final int OBIMP_BEX_UA_SRV_PARAMS_REPLY = 0x0002;
    private static final int OBIMP_BEX_UA_CLI_AVATAR_REQ = 0x0003;
    private static final int OBIMP_BEX_UA_SRV_AVATAR_REPLY = 0x0004;
    private static final int OBIMP_BEX_UA_CLI_AVATAR_SET = 0x0005;
    private static final int OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY = 0x0006;

    private static final int OBIMP_BEX_FT_CLI_PARAMS = 0x0001;
    private static final int OBIMP_BEX_FT_SRV_PARAMS_REPLY = 0x0002;
    private static final int OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST = 0x0003;
    private static final int OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY = 0x0004;
    private static final int OBIMP_BEX_FT_CLI_SRV_CONTROL = 0x0005;
    private static final int OBIMP_BEX_FT_DIR_PROX_ERROR = 0x0101;
    private static final int OBIMP_BEX_FT_DIR_PROX_HELLO = 0x0102;
    private static final int OBIMP_BEX_FT_DIR_PROX_FILE = 0x0103;
    private static final int OBIMP_BEX_FT_DIR_PROX_FILE_REPLY = 0x0104;
    private static final int OBIMP_BEX_FT_DIR_PROX_FILE_DATA = 0x0105;

    private static final int OBIMP_BEX_TP_CLI_PARAMS = 0x0001;
    private static final int OBIMP_BEX_TP_SRV_PARAMS_REPLY = 0x0002;
    private static final int OBIMP_BEX_TP_SRV_ITEM_READY = 0x0003;
    private static final int OBIMP_BEX_TP_CLI_SETTINGS = 0x0004;
    private static final int OBIMP_BEX_TP_SRV_SETTINGS_REPLY = 0x0005;
    private static final int OBIMP_BEX_TP_CLI_MANAGE = 0x0006;
    private static final int OBIMP_BEX_TP_SRV_TRANSPORT_INFO = 0x0007;
    private static final int OBIMP_BEX_TP_SRV_SHOW_NOTIF = 0x0008;
    private static final int OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH = 0x0009;
    
    public static void parsePacket(byte[] packet, /*byte[] auth,*/ DataOutputStream out) throws IOException {
        byte[] header = new byte[17]; // header is always 17 bytes length
        for(int i=0;i<17;i++) {
            header[i] = packet[i];
        }
        int type = header[5] + header[6];
        int subtype = header[7] + header[8];
        System.out.println("Server say: " + type + " " + subtype);
        if(type == OBIMP_BEX_COM) {
            if(subtype == OBIMP_BEX_COM_SRV_HELLO) {
                System.out.println("Server say \"HELLO\" =)");
            } else if(subtype == OBIMP_BEX_COM_SRV_BYE) {
                System.out.println("Server say \"BYE\" =(");
            }
        }
    }
    
}
