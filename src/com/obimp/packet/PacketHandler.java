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

import com.obimp.OBIMPConnection;
import com.obimp.data.structure.wTLD;
import com.obimp.data.type.BLK;
import com.obimp.data.type.OctaWord;
import com.obimp.data.type.UTF8;
import com.obimp.listener.ConnectionListener;
import com.obimp.listener.MessageListener;
import com.obimp.listener.UserStatusListener;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Vector;

/**
 * Обработчик пакетов
 * @author alex_xpert
 */
public class PacketHandler {
    public static volatile boolean logged = false;
    
    //General BEX types
    public static final int OBIMP_BEX_COM = 0x0001; // Common
    public static final int OBIMP_BEX_CL = 0x0002; // Contact list
    public static final int OBIMP_BEX_PRES = 0x0003; // Presence
    public static final int OBIMP_BEX_IM = 0x0004; // Instant messaging
    public static final int OBIMP_BEX_UD = 0x0005; // Users directory
    public static final int OBIMP_BEX_UA = 0x0006; // User avatars
    public static final int OBIMP_BEX_FT = 0x0007; // File transfer
    public static final int OBIMP_BEX_TP = 0x0008; // Transports
    //BEX 0x0001, Common. (OBIMP_BEX_COM)
    public static final int OBIMP_BEX_COM_CLI_HELLO = 0x0001;//Subtype: 0x0001. (OBIMP_BEX_COM_CLI_HELLO)
    private static final int OBIMP_BEX_COM_SRV_HELLO = 0x0002;//Subtype: 0x0002. (OBIMP_BEX_COM_SRV_HELLO)
    public static final int OBIMP_BEX_COM_CLI_LOGIN = 0x0003;//Subtype: 0x0003. (OBIMP_BEX_COM_CLI_LOGIN)
    private static final int OBIMP_BEX_COM_SRV_LOGIN_REPLY = 0x0004;//Subtype: 0x0004. (OBIMP_BEX_COM_SRV_LOGIN_REPLY)
    private static final int OBIMP_BEX_COM_SRV_BYE = 0x0005;//Subtype: 0x0005. (OBIMP_BEX_COM_SRV_BYE)
    public static final int OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING = 0x0006;//Subtype: 0x0006. (OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING)
    public static final int OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG = 0x0007;//Subtype: 0x0007. (OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG)
    public static final int OBIMP_BEX_COM_CLI_REGISTER = 0x0008;//Subtype: 0x0008. (OBIMP_BEX_COM_CLI_REGISTER)
    private static final int OBIMP_BEX_COM_SRV_REGISTER_REPLY = 0x0009;//Subtype: 0x0009. (OBIMP_BEX_COM_SRV_REGISTER_REPLY)
    //BEX 0x0002, Contact list. (OBIMP_BEX_CL)
    public static final int OBIMP_BEX_CL_CLI_PARAMS = 0x0001;//Subtype: 0x0001. (OBIMP_BEX_CL_CLI_PARAMS)
    private static final int OBIMP_BEX_CL_SRV_PARAMS_REPLY = 0x0002;//Subtype: 0x0002. (OBIMP_BEX_CL_SRV_PARAMS_REPLY)
    public static final int OBIMP_BEX_CL_CLI_REQUEST = 0x0003;//Subtype: 0x0003. (OBIMP_BEX_CL_CLI_REQUEST)
    private static final int OBIMP_BEX_CL_SRV_REPLY = 0x0004;//Subtype: 0x0004. (OBIMP_BEX_CL_SRV_REPLY)
    public static final int OBIMP_BEX_CL_CLI_VERIFY = 0x0005;//Subtype: 0x0005. (OBIMP_BEX_CL_CLI_VERIFY)
    private static final int OBIMP_BEX_CL_SRV_VERIFY_REPLY = 0x0006;//Subtype: 0x0006. (OBIMP_BEX_CL_SRV_VERIFY_REPLY)
    public static final int OBIMP_BEX_CL_CLI_ADD_ITEM = 0x0007;//Subtype: 0x0007. (OBIMP_BEX_CL_CLI_ADD_ITEM)
    private static final int OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY = 0x0008;//Subtype: 0x0008. (OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY)
    public static final int OBIMP_BEX_CL_CLI_DEL_ITEM = 0x0009;//Subtype: 0x0009. (OBIMP_BEX_CL_CLI_DEL_ITEM)
    private static final int OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY = 0x000A;//Subtype: 0x000A. (OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY)
    public static final int OBIMP_BEX_CL_CLI_UPD_ITEM = 0x000B;//Subtype: 0x000B. (OBIMP_BEX_CL_CLI_UPD_ITEM)
    private static final int OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY = 0x000C;//Subtype: 0x000C. (OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY)
    public static final int OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST = 0x000D;//Subtype: 0x000D. (OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST)
    public static final int OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY = 0x000E;//Subtype: 0x000E. (OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY)
    public static final int OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE = 0x000F;//Subtype: 0x000F. (OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE)
    public static final int OBIMP_BEX_CL_CLI_REQ_OFFAUTH = 0x0010;//Subtype: 0x0010. (OBIMP_BEX_CL_CLI_REQ_OFFAUTH)
    private static final int OBIMP_BEX_CL_SRV_DONE_OFFAUTH = 0x0011;//Subtype: 0x0011. (OBIMP_BEX_CL_SRV_DONE_OFFAUTH)
    public static final int OBIMP_BEX_CL_CLI_DEL_OFFAUTH = 0x0012;//Subtype: 0x0012. (OBIMP_BEX_CL_CLI_DEL_OFFAUTH)
    private static final int OBIMP_BEX_CL_SRV_ITEM_OPER = 0x0013;//Subtype: 0x0013. (OBIMP_BEX_CL_SRV_ITEM_OPER)
    private static final int OBIMP_BEX_CL_SRV_BEGIN_UPDATE = 0x0014;//Subtype: 0x0014. (OBIMP_BEX_CL_SRV_BEGIN_UPDATE)
    private static final int OBIMP_BEX_CL_SRV_END_UPDATE = 0x0015;//Subtype: 0x0015. (OBIMP_BEX_CL_SRV_END_UPDATE)
    //BEX 0x0003, Presence (OBIMP_BEX_PRES)
    public static final int OBIMP_BEX_PRES_CLI_PARAMS = 0x0001;//Subtype: 0x0001. (OBIMP_BEX_PRES_CLI_PARAMS)
    private static final int OBIMP_BEX_PRES_SRV_PARAMS_REPLY = 0x0002;//Subtype: 0x0002. (OBIMP_BEX_PRES_SRV_PARAMS_REPLY)
    public static final int OBIMP_BEX_PRES_CLI_SET_PRES_INFO = 0x0003;//Subtype: 0x0003. (OBIMP_BEX_PRES_CLI_SET_PRES_INFO)
    public static final int OBIMP_BEX_PRES_CLI_SET_STATUS = 0x0004;//Subtype: 0x0004. (OBIMP_BEX_PRES_CLI_SET_STATUS)
    public static final int OBIMP_BEX_PRES_CLI_ACTIVATE = 0x0005;//Subtype: 0x0005. (OBIMP_BEX_PRES_CLI_ACTIVATE)
    private static final int OBIMP_BEX_PRES_SRV_CONTACT_ONLINE = 0x0006;//Subtype: 0x0006. (OBIMP_BEX_PRES_SRV_CONTACT_ONLINE)
    private static final int OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE = 0x0007;//Subtype: 0x0007. (OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE)
    public static final int OBIMP_BEX_PRES_CLI_REQ_PRES_INFO = 0x0008;//Subtype: 0x0008. (OBIMP_BEX_PRES_CLI_REQ_PRES_INFO)
    private static final int OBIMP_BEX_PRES_SRV_PRES_INFO = 0x0009;//Subtype: 0x0009. (OBIMP_BEX_PRES_SRV_PRES_INFO)
    private static final int OBIMP_BEX_PRES_SRV_MAIL_NOTIF = 0x000A;//Subtype: 0x000A. (OBIMP_BEX_PRES_SRV_MAIL_NOTIF)
    public static final int OBIMP_BEX_PRES_CLI_REQ_OWN_MAIL_URL = 0x000B;//Subtype: 0x000B. (OBIMP_BEX_PRES_CLI_REQ_OWN_MAIL_URL)
    private static final int OBIMP_BEX_PRES_SRV_OWN_MAIL_URL = 0x000C;//Subtype: 0x000C. (OBIMP_BEX_PRES_SRV_OWN_MAIL_URL)
    //BEX 0x0004, Instant messaging (OBIMP_BEX_IM)
    public static final int OBIMP_BEX_IM_CLI_PARAMS = 0x0001;//Subtype: 0x0001. (OBIMP_BEX_IM_CLI_PARAMS)
    private static final int OBIMP_BEX_IM_SRV_PARAMS_REPLY = 0x0002;//Subtype: 0x0002. (OBIMP_BEX_IM_SRV_PARAMS_REPLY)
    public static final int OBIMP_BEX_IM_CLI_REQ_OFFLINE = 0x0003;//Subtype: 0x0003. (OBIMP_BEX_IM_CLI_REQ_OFFLINE)
    private static final int OBIMP_BEX_IM_SRV_DONE_OFFLINE = 0x0004;//Subtype: 0x0004. (OBIMP_BEX_IM_SRV_DONE_OFFLINE)
    public static final int OBIMP_BEX_IM_CLI_DEL_OFFLINE = 0x0005;//Subtype: 0x0005. (OBIMP_BEX_IM_CLI_DEL_OFFLINE)
    public static final int OBIMP_BEX_IM_CLI_MESSAGE = 0x0006;//Subtype: 0x0006. (OBIMP_BEX_IM_CLI_MESSAGE)
    private static final int OBIMP_BEX_IM_SRV_MESSAGE = 0x0007;//Subtype: 0x0007. (OBIMP_BEX_IM_SRV_MESSAGE)
    public static final int OBIMP_BEX_IM_CLI_SRV_MSG_REPORT = 0x0008;//Subtype: 0x0008. (OBIMP_BEX_IM_CLI_SRV_MSG_REPORT)
    public static final int OBIMP_BEX_IM_CLI_SRV_NOTIFY = 0x0009;//Subtype: 0x0009. (OBIMP_BEX_IM_CLI_SRV_NOTIFY)
    public static final int OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REQ = 0x000A;//Subtype: 0x000A. (OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REQ)
    public static final int OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY = 0x000B;//Subtype: 0x000B. (OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY)
    public static final int OBIMP_BEX_IM_CLI_MULTIPLE_MSG = 0x000C;//Subtype: 0x000C. (OBIMP_BEX_IM_CLI_MULTIPLE_MSG)
    //BEX 0x0005, Users directory (OBIMP_BEX_UD)
    public static final int OBIMP_BEX_UD_CLI_PARAMS = 0x0001;//Subtype: 0x0001. (OBIMP_BEX_UD_CLI_PARAMS)
    private static final int OBIMP_BEX_UD_SRV_PARAMS_REPLY = 0x0002;//Subtype: 0x0002. (OBIMP_BEX_UD_SRV_PARAMS_REPLY)
    public static final int OBIMP_BEX_UD_CLI_DETAILS_REQ = 0x0003;//Subtype: 0x0003. (OBIMP_BEX_UD_CLI_DETAILS_REQ)
    private static final int OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY = 0x0004;//Subtype: 0x0004. (OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY)
    public static final int OBIMP_BEX_UD_CLI_DETAILS_UPD = 0x0005;//Subtype: 0x0005. (OBIMP_BEX_UD_CLI_DETAILS_UPD)
    private static final int OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY = 0x0006;//Subtype: 0x0006. (OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY)
    public static final int OBIMP_BEX_UD_CLI_SEARCH = 0x0007;//Subtype: 0x0007. (OBIMP_BEX_UD_CLI_SEARCH)
    private static final int OBIMP_BEX_UD_SRV_SEARCH_REPLY = 0x0008;//Subtype: 0x0008. (OBIMP_BEX_UD_SRV_SEARCH_REPLY)
    public static final int OBIMP_BEX_UD_CLI_SECURE_UPD = 0x0009;//Subtype: 0x0009. (OBIMP_BEX_UD_CLI_SECURE_UPD)
    private static final int OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY = 0x000A;//Subtype: 0x000A. (OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY)
    //BEX 0x0006, User avatars (OBIMP_BEX_UA)
    public static final int OBIMP_BEX_UA_CLI_PARAMS = 0x0001;//Subtype: 0x0001. (OBIMP_BEX_UA_CLI_PARAMS)
    private static final int OBIMP_BEX_UA_SRV_PARAMS_REPLY = 0x0002;//Subtype: 0x0002. (OBIMP_BEX_UA_SRV_PARAMS_REPLY)
    public static final int OBIMP_BEX_UA_CLI_AVATAR_REQ = 0x0003;//Subtype: 0x0003. (OBIMP_BEX_UA_CLI_AVATAR_REQ)
    private static final int OBIMP_BEX_UA_SRV_AVATAR_REPLY = 0x0004;//Subtype: 0x0004. (OBIMP_BEX_UA_SRV_AVATAR_REPLY)
    public static final int OBIMP_BEX_UA_CLI_AVATAR_SET = 0x0005;//Subtype: 0x0005. (OBIMP_BEX_UA_CLI_AVATAR_SET)
    private static final int OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY = 0x0006;//Subtype: 0x0006. (OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY)
    //BEX 0x0007, File transfer (OBIMP_BEX_FT)
    public static final int OBIMP_BEX_FT_CLI_PARAMS = 0x0001;//Subtype: 0x0001. (OBIMP_BEX_FT_CLI_PARAMS)
    private static final int OBIMP_BEX_FT_SRV_PARAMS_REPLY = 0x0002;//Subtype: 0x0002. (OBIMP_BEX_FT_SRV_PARAMS_REPLY)
    public static final int OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST = 0x0003;//Subtype: 0x0003. (OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST)
    public static final int OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY = 0x0004;//Subtype: 0x0004. (OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY)
    public static final int OBIMP_BEX_FT_CLI_SRV_CONTROL = 0x0005;//Subtype: 0x0005. (OBIMP_BEX_FT_CLI_SRV_CONTROL)
    private static final int OBIMP_BEX_FT_DIR_PROX_ERROR = 0x0101;//Subtype: 0x0101. (OBIMP_BEX_FT_DIR_PROX_ERROR)
    private static final int OBIMP_BEX_FT_DIR_PROX_HELLO = 0x0102;//Subtype: 0x0102. (OBIMP_BEX_FT_DIR_PROX_HELLO)
    private static final int OBIMP_BEX_FT_DIR_PROX_FILE = 0x0103;//Subtype: 0x0103. (OBIMP_BEX_FT_DIR_PROX_FILE)
    private static final int OBIMP_BEX_FT_DIR_PROX_FILE_REPLY = 0x0104;//Subtype: 0x0104. (OBIMP_BEX_FT_DIR_PROX_FILE_REPLY)
    private static final int OBIMP_BEX_FT_DIR_PROX_FILE_DATA = 0x0105;//Subtype: 0x0105. (OBIMP_BEX_FT_DIR_PROX_FILE_DATA)
    //BEX 0x0008, Transports (OBIMP_BEX_TP)
    public static final int OBIMP_BEX_TP_CLI_PARAMS = 0x0001;//Subtype: 0x0001. (OBIMP_BEX_TP_CLI_PARAMS)
    private static final int OBIMP_BEX_TP_SRV_PARAMS_REPLY = 0x0002;//Subtype: 0x0002. (OBIMP_BEX_TP_SRV_PARAMS_REPLY)
    private static final int OBIMP_BEX_TP_SRV_ITEM_READY = 0x0003;//Subtype: 0x0003. (OBIMP_BEX_TP_SRV_ITEM_READY)
    public static final int OBIMP_BEX_TP_CLI_SETTINGS = 0x0004;//Subtype: 0x0004. (OBIMP_BEX_TP_CLI_SETTINGS)
    private static final int OBIMP_BEX_TP_SRV_SETTINGS_REPLY = 0x0005;//Subtype: 0x0005. (OBIMP_BEX_TP_SRV_SETTINGS_REPLY)
    public static final int OBIMP_BEX_TP_CLI_MANAGE = 0x0006;//Subtype: 0x0006. (OBIMP_BEX_TP_CLI_MANAGE)
    private static final int OBIMP_BEX_TP_SRV_TRANSPORT_INFO = 0x0007;//Subtype: 0x0007. (OBIMP_BEX_TP_SRV_TRANSPORT_INFO)
    private static final int OBIMP_BEX_TP_SRV_SHOW_NOTIF = 0x0008;//Subtype: 0x0008. (OBIMP_BEX_TP_SRV_SHOW_NOTIF)
    private static final int OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH = 0x0009;//Subtype: 0x0009. (OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH)
    
    @SuppressWarnings("element-type-mismatch")
    public static synchronized void parsePacket(Packet packet, HashMap<Integer, BLK> tlds, OBIMPConnection oc, String username, String password, Socket socket) throws IOException {
        String s = "";
        switch(packet.getType()) {
            case OBIMP_BEX_COM:
                switch(packet.getSubType()) {
                    case OBIMP_BEX_COM_SRV_HELLO:
                        s = "Server say HELLO";
                        if(tlds.get(0x0001) != null){
                            switch(tlds.get(0x0001).getData()[1]){
                                case 0x0001:
                                    s += " - HELLO_ERROR_ACCOUNT_INVALID";
                                    break;
                                case 0x0002:
                                    s += " - HELLO_ERROR_SERVICE_TEMP_UNAVAILABLE";
                                    break;
                                case 0x0003:
                                    s += " - HELLO_ERROR_ACCOUNT_BANNED";
                                    break;
                                case 0x0004:
                                    s += " - HELLO_ERROR_WRONG_COOKIE";
                                    break;
                                case 0x0005:
                                    s += " - HELLO_ERROR_TOO_MANY_CLIENTS";
                                    break;
                                case 0x0006:
                                    s += " - HELLO_ERROR_INVALID_LOGIN";
                                    break;
                            }
                            for(ConnectionListener cl : oc.con_list) {
                                cl.onLoginFailed(s.split(" - ")[1]);
                            }
                        } else {
                            s += " - NO ERROR";
                            Packet login = new Packet(0x0001, 0x0003);
                            login.append(new wTLD(0x0001, new UTF8(username)));
                            if (tlds.containsKey(0x0007)) { // server requires plain-text password authentication
                                login.append(new wTLD(0x0003, new BLK(Base64.encode(password.getBytes()).getBytes())));
                            } else { // generate one-time MD5 password hash
                                byte[] srv_key = tlds.get(0x0002).getData();
                                byte[] pre_hash = MD5((username + "OBIMPSALT" + password).getBytes());
                                byte[] md5 = new byte[pre_hash.length + srv_key.length];
                                int j = 0;
                                for(int i=0;i<pre_hash.length;i++) {
                                    md5[j] = pre_hash[i];
                                    j++;
                                }
                                for(int i=0;i<srv_key.length;i++) {
                                    md5[j] = srv_key[i];
                                    j++;
                                }
                                byte[] hash = MD5(md5);
                                login.append(new wTLD(0x0002, new OctaWord(hash)));
                            }
                            socket.getOutputStream().write(login.asByteArray(oc.getSeq()));
                            socket.getOutputStream().flush();
                        }
                        break;
                    case OBIMP_BEX_COM_SRV_LOGIN_REPLY:
                        s = "Server say LOGIN";
                        if(tlds.get(0x0001) != null){
                            switch(tlds.get(0x0001).getData()[1]){
                                case 0x0001:
                                    s += " - LOGIN_ERROR_ACCOUNT_INVALID";
                                    break;
                                case 0x0002:
                                    s += " - LOGIN_ERROR_SERVICE_TEMP_UNAVAILABLE";
                                    break;
                                case 0x0003:
                                    s += " - LOGIN_ERROR_ACCOUNT_BANNED";
                                    break;
                                case 0x0004:
                                    s += " - LOGIN_ERROR_WRONG_PASSWORD";
                                    break;
                                case 0x0005:
                                    s += " - LOGIN_ERROR_INVALID_LOGIN";
                                    break;
                            }
                            for(ConnectionListener cl : oc.con_list) {
                                cl.onLoginFailed(s.split(" - ")[1]);
                            }
                        }else {
                            s += " - NO ERROR";
                            logged = true;
                            for(ConnectionListener cl : oc.con_list) {
                                cl.onLoginSuccess();
                            }
                        }
                        break;
                    case OBIMP_BEX_COM_SRV_BYE:
                        s = "Server say BYE";
//                        for(byte b : packet.asByteArray(OBIMP_BEX_CL)) {
//                            s += b + " ";
//                        }
                        if(tlds.get(0x0001) != null){
                            switch(tlds.get(0x0001).getData()[1]){
                                case 0x0001:
                                    s += " - BYE_REASON_SRV_SHUTDOWN";
                                    break;
                                case 0x0002:
                                    s += " - BYE_REASON_CLI_NEW_LOGIN";
                                    break;
                                case 0x0003:
                                    s += " - BYE_REASON_ACCOUNT_KICKED";
                                    break;
                                case 0x0004:
                                    s += " - BYE_REASON_INCORRECT_SEQ";
                                    break;
                                case 0x0005:
                                    s += " - BYE_REASON_INCORRECT_BEX_TYPE";
                                    break;
                                case 0x0006:
                                    s += " - BYE_REASON_INCORRECT_BEX_SUB";
                                    break;
                                case 0x0007:
                                    s += " - BYE_REASON_INCORRECT_BEX_STEP";
                                    break;
                                case 0x0008:
                                    s += " - BYE_REASON_TIMEOUT";
                                    break;
                                case 0x0009:
                                    s += " - BYE_REASON_INCORRECT_WTLD";
                                    break;
                                case 0x000A:
                                    s += " - BYE_REASON_NOT_ALLOWED";
                                    break;
                                case 0x000B:
                                    s += " - BYE_REASON_FLOODING";
                                    break;
                            }
                            for(ConnectionListener cl : oc.con_list) {
                                cl.onLogout(s.split(" - ")[1]);
                            }
                        }else s += " - NO ERROR";
                        break;
                    case OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PING:
                        s = "Server say PING";
                        oc.sendPong();
                        break;
                    case OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG:
                        s = "Server say PONG";
                        break;
                    case OBIMP_BEX_COM_SRV_REGISTER_REPLY:
                        s = "Server say REGISTER";
                        if(tlds.get(0x0001) != null){
                            switch(tlds.get(0x0001).getData()[1]){
                                case 0x0000:
                                    s += " - REG_RES_SUCCESS";
                                    break;
                                case 0x0001:
                                    s += " - REG_RES_DISABLED";
                                    break;
                                case 0x0002:
                                    s += " - REG_RES_ACCOUNT_EXISTS";
                                    break;
                                case 0x0003:
                                    s += " - REG_RES_BAD_ACCOUNT_NAME";
                                    break;
                                case 0x0004:
                                    s += " - REG_RES_BAD_REQUEST";
                                    break;
                                case 0x0005:
                                    s += " - REG_RES_BAD_SERVER_KEY";
                                    break;
                                case 0x0006:
                                    s += " - REG_RES_SERVICE_TEMP_UNAVAILABLE";
                                    break;
                                case 0x0007:
                                    s += " - REG_RES_EMAIL_REQUIRED";
                                    break;
                            }
                        }else s += " - NO ERROR";
                        break;
                }
                break;
            case OBIMP_BEX_CL:
                    switch(packet.getSubType()) {
                        case OBIMP_BEX_CL_SRV_PARAMS_REPLY:
                            s = "Server say CL PARAMS";
                            break;
                        case OBIMP_BEX_CL_SRV_REPLY:
                            s = "Server say CL";
                            byte[] data = tlds.get(0x0001).getData();
                            int cl_length = PacketListener.getLength(data[0], data[1], data[2], data[3]);
                            s += "\nYour CL (" + cl_length + " items):";
                            byte[] cl = new byte[data.length - 4];
                            int j = 4;
                            for(int i=0;i<cl.length;i++) {
                                cl[i] = data[j];
                                j++;
                            }
                            //Vector items = new Vector<String>();
                            while(cl.length > 0) {
                                int type = PacketListener.getLength((byte) 0, (byte) 0, cl[0], cl[1]);
                                int item_id = PacketListener.getLength(cl[2], cl[3], cl[4], cl[5]);
                                int group_id = PacketListener.getLength(cl[6], cl[7], cl[8], cl[9]);
                                int length = PacketListener.getLength(cl[10], cl[11], cl[12], cl[13]);
                                s += "\n";
                                switch(type) {
                                    case 1:
                                        s += "[Group] ";
                                        break;
                                    case 2:
                                        s += "[Contact] ";
                                        break;
                                    case 3:
                                        s += "[Transport] ";
                                        break;
                                    case 4:
                                        s += "[Note] ";
                                        break;
                                }
                                int st =  17 + PacketListener.getLength((byte) 0, (byte) 0, cl[16], cl[17]) + 5;
                                int l = PacketListener.getLength((byte) 0, (byte) 0, cl[st - 2], cl[st - 1]);
                                byte[] name = new byte[l];
                                j = st;
                                for(int i=0;i<name.length;i++) {
                                    name[i] = cl[j];
                                    j++;
                                }
                                s += "Name = " + new String(name) + " ";
                                s += "Item ID = " + item_id + ", Group ID = " + group_id;
                                byte[] next = new byte[cl.length - (length + 14)];
                                j = length + 13;
                                for(int i=0;i<next.length;i++) {
                                    next[i] = cl[j];
                                    j++;
                                }
                                cl = next;
                            }
                            break;
                        case OBIMP_BEX_CL_SRV_VERIFY_REPLY:
                            s = "Server say CL VERIFY";
                            break;
                        case OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY:
                            s = "Server say CL ADD ITEM";
                        if(tlds.get(0x0001) != null){
                        switch(tlds.get(0x0001).getData()[1]){
                            case 0x0000:
                                s += " - ADD_RES_SUCCESS";
                                break;
                            case 0x0001:
                                s += " - ADD_RES_ERROR_WRONG_ITEM_TYPE";
                                break;
                            case 0x0002:
                                s += " - ADD_RES_ERROR_WRONG_PARENT_GROUP";
                                break;
                            case 0x0003:
                                s += " - ADD_RES_ERROR_NAME_LEN_LIMIT";
                                break;
                            case 0x0004:
                                s += " - ADD_RES_ERROR_WRONG_NAME";
                                break;
                            case 0x0005:
                                s += " - ADD_RES_ERROR_ITEM_ALREADY_EXISTS";
                                break;
                            case 0x0006:
                                s += " - ADD_RES_ERROR_ITEM_LIMIT_REACHED";
                                break;
                            case 0x0007:
                                s += " - ADD_RES_ERROR_BAD_REQUEST";
                                break;
                            case 0x0008:
                                s += " - ADD_RES_ERROR_BAD_ITEM_STLD";
                                break;
                            case 0x0009:
                                s += " - ADD_RES_ERROR_NOT_ALLOWED";
                                break;
                        }
                        }else s += " - NO ERROR";
                            break;
                        case OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY:
                            s = "Server say CL DEL ITEM";
                        if(tlds.get(0x0001) != null){
                        switch(tlds.get(0x0001).getData()[1]){
                            case 0x0000:
                                s += " - DEL_RES_SUCCESS";
                                break;
                            case 0x0001:
                                s += " - DEL_RES_ERROR_NOT_FOUND";
                                break;
                            case 0x0002:
                                s += " - DEL_RES_ERROR_NOT_ALLOWED";
                                break;
                            case 0x0003:
                                s += " - DEL_RES_ERROR_GROUP_NOT_EMPTY";
                                break;
                        }
                        }else s += " - NO ERROR";
                            break;
                        case OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY:
                            s = "Server say CL UPD ITEM";
                        if(tlds.get(0x0001) != null){
                        switch(tlds.get(0x0001).getData()[1]){
                            case 0x0000:
                                s += " - UPD_RES_SUCCESS";
                                break;
                            case 0x0001:
                                s += " - UPD_RES_ERROR_NOT_FOUND";
                                break;
                            case 0x0002:
                                s += " - UPD_RES_ERROR_WRONG_PARENT_GROUP";
                                break;
                            case 0x0003:
                                s += " - UPD_RES_ERROR_NAME_LEN_LIMIT";
                                break;
                            case 0x0004:
                                s += " - UPD_RES_ERROR_WRONG_NAME";
                                break;
                            case 0x0005:
                                s += " - UPD_RES_ERROR_ITEM_ALREADY_EXISTS";
                                break;
                            case 0x0006:
                                s += " - UPD_RES_ERROR_BAD_REQUEST";
                                break;
                            case 0x0007:
                                s += " - UPD_RES_ERROR_BAD_ITEM_STLD";
                                break;
                            case 0x0008:
                                s += " - UPD_RES_ERROR_NOT_ALLOWED";
                                break;
                        }
                        }else s += " - NO ERROR";
                            break;
                        case OBIMP_BEX_CL_SRV_DONE_OFFAUTH:
                            s = "Server say CL DONE OFFAUTH";
                            break;
                        case OBIMP_BEX_CL_SRV_ITEM_OPER:
                            s = "Server say CL ITEM OPER";
                        if(tlds.get(0x0001) != null){
                        switch(tlds.get(0x0001).getData()[1]){
                            case 0x0001:
                                s += " - OPER_ADD_ITEM";
                                break;
                            case 0x0002:
                                s += " - OPER_DEL_ITEM";
                                break;
                            case 0x0003:
                                s += " - OPER_UPD_ITEM";
                                break;
                        }
                        }else s += " - NO ERROR";
                            break;
                        case OBIMP_BEX_CL_SRV_BEGIN_UPDATE:
                            s = "Server say CL BEGIN UPDATE";
                            break;
                        case OBIMP_BEX_CL_SRV_END_UPDATE:
                            s = "Server say CL END UPDATE";
                            break;
                }
                break;
            case OBIMP_BEX_PRES:
                switch(packet.getSubType()) {
                    case OBIMP_BEX_PRES_SRV_PARAMS_REPLY:
                        s = "Server say PRES PARAMS";
                        break;
                    case OBIMP_BEX_PRES_SRV_CONTACT_ONLINE:
                        String n = new String(tlds.get(0x0001).getData());
                        String s_n = new String(tlds.get(0x0003).getData());
                        String c_n = new String(tlds.get(0x0008).getData());
                        byte[] v = tlds.get(0x0009).getData();
                     
                        String c_v = ((int) v[0] + (int) v[1]) + "." + ((int) v[2] + (int) v[3]) + "." + ((int) v[4] + (int) v[5]) + "." + ((int) v[6] + (int) v[7]);
                        String os_n = new String(tlds.get(0x000F).getData());
                        s = "Contact online " + n + ": " + s_n + " (" + c_n + "  " + c_v + " | " + os_n +" )";
                        for(UserStatusListener usl : oc.stat_list) {
                            usl.onUserOnline(n, s_n, c_n, c_v, os_n);
                        }
                        break;
                    case OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE:
                        String id = new String(tlds.get(0x0001).getData());
                        s = "Contact offline " + id;
                        for(UserStatusListener usl : oc.stat_list) {
                            usl.onUserOffline(id);
                        }
                        break;
                    case OBIMP_BEX_PRES_SRV_PRES_INFO:
                        s = "Server say PRES INFO "+new String(tlds.get(0x0001).getData());
//                        s += "\ncurrent session client IP address : "+new String(tlds.get(0x0005).getData());
//                        s += "\nprevious session client IP address : "+new String(tlds.get(0x0006).getData());
                        break;
                    case OBIMP_BEX_PRES_SRV_MAIL_NOTIF:
                        s = "Server say MAIL NOTIF";
//                        s += "\nreceiver email address"+new String(tlds.get(0x0003).getData());
//                        s += "sender name"+new String(tlds.get(0x0004).getData())+"\n";
//                        s += "sender email address"+new String(tlds.get(0x0005).getData())+"\n";
//                        s += "mail subject"+new String(tlds.get(0x0006).getData())+"\n";
//                        s += "mail text"+new String(tlds.get(0x0007).getData())+"\n";
//                        s += "mailbox URL "+new String(tlds.get(0x0008).getData())+"\n";
                        break;
                    case OBIMP_BEX_PRES_SRV_OWN_MAIL_URL:
                        s = "Server say OWN MAIL URL ";//+new String(tlds.get(0x0001).getData());
                        break;

                }
                break;
            case OBIMP_BEX_IM:
                switch(packet.getSubType()) {
                    case OBIMP_BEX_IM_SRV_PARAMS_REPLY:
                        s = "Server say IM PARAMS";
                        break;
                    case OBIMP_BEX_IM_SRV_DONE_OFFLINE:
                        s = "Server say IM DONE OFFLINE";
                        break;
                    case OBIMP_BEX_IM_SRV_MESSAGE:
                        String id = new String(tlds.get(0x0001).getData());
                        String text = new String(tlds.get(0x0004).getData());
                        s = "New message from " + id + ": " + text;
                        //OBIMP.set_upd_info(oc, "jimbot");
                        for(MessageListener ml : oc.msg_list) {
                            ml.onIncomingMessage(id, text);
                        }
                        break;
                }
                break;
            case OBIMP_BEX_UD:
                switch(packet.getSubType()) {
                    case OBIMP_BEX_UD_SRV_PARAMS_REPLY:
                        s = "Server say UD PARAMS";
                        break;
                    case OBIMP_BEX_UD_SRV_DETAILS_REQ_REPLY:
                        s = "Server say UD DETAILS REQ";
                        if(tlds.get(0x0001) != null){
                        switch(tlds.get(0x0001).getData()[1]){
                            case 0x0000:
                                s += " - DETAILS_RES_SUCCESS";
                                break;
                            case 0x0001:
                                s += " - DETAILS_RES_NOT_FOUND";
                                break;
                            case 0x0002:
                                s += " - DETAILS_RES_TOO_MANY_REQUESTS";
                                break;
                            case 0x0003:
                                s += " - DETAILS_RES_SERVICE_TEMP_UNAVAILABLE";
                                break;
                        }
                        }else s += " - NO ERROR";
//                        if(tlds.get(0x0002).getData().length>0)s += "\naccount name: "+new String(tlds.get(0x0002).getData());
//                        if(tlds.get(0x0003).getData().length>0)s += "\nsecure email: "+new String(tlds.get(0x0003).getData());
//                        if(tlds.get(0x0004).getData().length>0)s += "\nnick name: "+new String(tlds.get(0x0004).getData());
//                        if(tlds.get(0x0005).getData().length>0)s += "\nfirst name: "+new String(tlds.get(0x0005).getData());
//                        if(tlds.get(0x0006).getData().length>0)s += "\nlast name: "+new String(tlds.get(0x0006).getData());
//                        if(tlds.get(0x0008).getData().length>0)s += "\nregion/state: "+new String(tlds.get(0x0008).getData());
//                        if(tlds.get(0x0009).getData().length>0)s += "\ncity: "+new String(tlds.get(0x0009).getData());
//                        if(tlds.get(0x000A).getData().length>0)s += "\nzip code: "+new String(tlds.get(0x000A).getData());
//                        if(tlds.get(0x000B).getData().length>0)s += "\naddress: "+new String(tlds.get(0x000B).getData());
//                        if(tlds.get(0x0010).getData().length>0)s += "\nhomepage: "+new String(tlds.get(0x0010).getData());
//                        if(tlds.get(0x0011).getData().length>0)s += "\nabout: "+new String(tlds.get(0x0011).getData());
//                        if(tlds.get(0x0012).getData().length>0)s += "\ninterests: "+new String(tlds.get(0x0012).getData());
//                        if(tlds.get(0x0013).getData().length>0)s += "\nemail: "+new String(tlds.get(0x0013).getData());
//                        if(tlds.get(0x0014).getData().length>0)s += "\nadditional email: "+new String(tlds.get(0x0014).getData());
//                        if(tlds.get(0x0015).getData().length>0)s += "\nhome phone: "+new String(tlds.get(0x0015).getData());
//                        if(tlds.get(0x0016).getData().length>0)s += "\nwork phone: "+new String(tlds.get(0x0016).getData());
//                        if(tlds.get(0x0017).getData().length>0)s += "\ncellular phone: "+new String(tlds.get(0x0017).getData());
//                        if(tlds.get(0x0018).getData().length>0)s += "\nfax number: "+new String(tlds.get(0x0018).getData());
//                        if(tlds.get(0x001A).getData().length>0)s += "\ncompany: "+new String(tlds.get(0x001A).getData());
//                        if(tlds.get(0x001B).getData().length>0)s += "\ndivision/department: "+new String(tlds.get(0x001B).getData());
//                        if(tlds.get(0x001C).getData().length>0)s += "\nposition: "+new String(tlds.get(0x001C).getData());
                        break;
                    case OBIMP_BEX_UD_SRV_DETAILS_UPD_REPLY:
                        s = "Server say UD DETAILS UPD";
                        if(tlds.get(0x0001) != null){
                        switch(tlds.get(0x0001).getData()[1]){
                            case 0x0000:
                                s += " - UPD_DETAILS_RES_SUCCESS";
                                break;
                            case 0x0001:
                                s += " - UPD_DETAILS_RES_BAD_REQUEST";
                                break;
                            case 0x0002:
                                s += " - UPD_DETAILS_RES_SERVICE_TEMP_UNAVAILABLE";
                                break;
                            case 0x0003:
                                s += " - UPD_DETAILS_RES_NOT_ALLOWED";
                                break;
                        }
                        }else s += " - NO ERROR";
                        break;
                    case OBIMP_BEX_UD_SRV_SEARCH_REPLY:
                        s = "Server say UD SEARCH";
                        if(tlds.get(0x0001) != null){
                        switch(tlds.get(0x0001).getData()[1]){
                            case 0x0000:
                                s += " - SEARCH_RES_SUCCESS";
                                break;
                            case 0x0001:
                                s += " - SEARCH_RES_NOT_FOUND";
                                break;
                            case 0x0002:
                                s += " - SEARCH_RES_BAD_REQUEST";
                                break;
                            case 0x0003:
                                s += " - SEARCH_RES_TOO_MANY_REQUESTS";
                                break;
                            case 0x0004:
                                s += " - SEARCH_RES_SERVICE_TEMP_UNAVAILABLE";
                                break;
                        }
                        }else s += " - NO ERROR";
                        if(tlds.get(0x0002).getData().length>0) s += "\naccount name: "+new String(tlds.get(0x0002).getData());
//                        if(tlds.get(0x0003).getData().length>0) s += "\nnick name: "+new String(tlds.get(0x0003).getData());
//                        if(tlds.get(0x0004).getData().length>0) s += "\nfirst name: "+new String(tlds.get(0x0004).getData());
//                        if(tlds.get(0x0005).getData().length>0) s += "\nlast name: "+new String(tlds.get(0x0005).getData());
                        if(tlds.get(0x0006).getData().length>0){
                            switch(tlds.get(0x0006).getData()[0]){
                                case 0x00:
                                    s += "\ngender: not specified";
                                    break;
                                case 0x01:
                                    s += "\ngender: female";
                                    break;
                                case 0x02:
                                    s += "\ngender: male";
                                    break;
                            }
                        }
//                        if(tlds.get(0x0007).getData().length>1)s += "\nage: "+new String(tlds.get(0x0007).getData());
                        if(tlds.get(0x0008) != null){
                        if(new String(tlds.get(0x0008).getData()).isEmpty())
                            s += "\nuser: online";
                        } else s += "\nuser: offline";
                        break;
                    case OBIMP_BEX_UD_SRV_SECURE_UPD_REPLY:
                        s = "Server say UD SECURE UPD";
                        if(tlds.get(0x0001) != null){
                        switch(tlds.get(0x0001).getData()[1]){
                            case 0x0000:
                                s += " - UPD_SECURE_RES_SUCCESS";
                                break;
                            case 0x0001:
                                s += " - UPD_SECURE_RES_BAD_REQUEST";
                                break;
                            case 0x0002:
                                s += " - UPD_SECURE_RES_SERVICE_TEMP_UNAVAILABLE";
                                break;
                            case 0x0003:
                                s += " - UPD_SECURE_RES_NOT_ALLOWED";
                                break;
                        }
                        }else s += " - NO ERROR";
                        break;
                }
                break;
            case OBIMP_BEX_UA:
                switch(packet.getSubType()) {
                    case OBIMP_BEX_UA_SRV_PARAMS_REPLY:
                        s = "Server say UA PARAMS";
                        break;
                   case OBIMP_BEX_UA_SRV_AVATAR_REPLY:
                       s = "Server say UA AVATAR";
                       if(tlds.get(0x0001) != null){
                        switch(tlds.get(0x0001).getData()[1]){
                            case 0x0000:
                                s += " - AVATAR_REQ_SUCCESS";
                                break;
                            case 0x0001:
                                s += " - AVATAR_REQ_NOT_FOUND";
                                break;
                            case 0x0002:
                                s += " - AVATAR_REQ_NOT_ALLOWED";
                                break;
                        }
                        }else s += " - NO ERROR";
                        break;
                   case OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY:
                       s = "Server say UA AVATAR SET";
                       if(tlds.get(0x0001) != null){
                        switch(tlds.get(0x0001).getData()[1]){
                            case 0x0000:
                                s += " - AVATAR_SET_SUCCESS";
                                break;
                            case 0x0001:
                                s += " - AVATAR_SET_BAD_MD5";
                                break;
                            case 0x0002:
                                s += " - AVATAR_SET_NOT_ALLOWED";
                                break;
                            case 0x0003:
                                s += " - AVATAR_SET_TEMP_UNAVAILABLE";
                                break;
                            case 0x0004:
                                s += " - AVATAR_SET_TOO_BIG";
                                break;
                            case 0x0005:
                                s += " - AVATAR_SET_TOO_SMALL";
                                break;
                            case 0x0006:
                                s += " - AVATAR_SET_BANNED";
                                break;
                            case 0x0007:
                                s += " - AVATAR_SET_INVALID_TYPE";
                                break;
                            case 0x0008:
                                s += " - AVATAR_SET_OTHER_ERROR";
                                break;
                        }
                        }else s += " - NO ERROR";
                        break;
                }
                break;
            case OBIMP_BEX_FT:
                switch(packet.getSubType()) {
                    case OBIMP_BEX_FT_SRV_PARAMS_REPLY:
                        s = "Server say FT PARAMS";
                        break;
                }
                break;
            case OBIMP_BEX_TP:
                switch(packet.getSubType()) {
                    case OBIMP_BEX_TP_SRV_PARAMS_REPLY:
                        s = "Server say TP PARAMS";
                        break;
                    case OBIMP_BEX_TP_SRV_ITEM_READY:
                        s = "Server say TP ITEM READY";
                        break;
                    case OBIMP_BEX_TP_SRV_SETTINGS_REPLY:
                        s = "Server say TP SETTINGS";
                        if(tlds.get(0x0002) != null){
                        switch(tlds.get(0x0002).getData()[1]){
                            case 0x0000:
                                s += " - TP_SET_RES_SUCCESS";
                                break;
                            case 0x0001:
                                s += " - TP_SET_RES_ERROR_WRONG_ID";
                                break;
                            case 0x0002:
                                s += " - TP_SET_RES_ERROR_NOT_FOUND";
                                break;
                            case 0x0003:
                                s += " - TP_SET_RES_ERROR_NOT_ALLOWED";
                                break;
                        }
                        }else s += " - NO ERROR";
                        break;
                    case OBIMP_BEX_TP_SRV_TRANSPORT_INFO:
                        s = "Server say TP TRANSPORT INFO";
                        if(tlds.get(0x0002) != null){
                        switch(tlds.get(0x0002).getData()[1]){
                            case 0x0000:
                                s += " - TP_STATE_LOGGEDIN";
                                break;
                            case 0x0001:
                                s += " - TP_STATE_LOGGEDOFF";
                                break;
                            case 0x0002:
                                s += " - TP_STATE_STATUS_CHANGED";
                                break;
                            case 0x0003:
                                s += " - TP_STATE_CON_FAILED";
                                break;
                            case 0x0004:
                                s += " - TP_STATE_ACCOUNT_INVALID";
                                break;
                            case 0x0005:
                                s += " - TP_STATE_SERVICE_TEMP_UNAVAILABLE";
                                break;
                            case 0x0006:
                                s += " - TP_STATE_WRONG_PASSWORD";
                                break;
                            case 0x0007:
                                s += " - TP_STATE_INVALID_LOGIN";
                                break;
                            case 0x0008:
                                s += " - TP_STATE_OTHER_PLACE_LOGIN";
                                break;
                            case 0x0009:
                                s += " - TP_STATE_CANT_LOGIN_TRY_LATER";
                                break;
                            case 0x000A:
                                s += " - TP_STATE_SRV_PAUSED";
                                break;
                            case 0x000B:
                                s += " - TP_STATE_SRV_RESUMED";
                                break;
                            case 0x000C:
                                s += " - TP_STATE_SRV_MIGRATED";
                                break;
                        }
                        }else s += " - NO ERROR";
                        break;
                    case OBIMP_BEX_TP_SRV_SHOW_NOTIF:
                        s = "Server say TP SHOW NOTIF";
                        break;
                    case OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH:
                        s = "Server say TP OWN AVATAR HASH";
                        break;
                }
                break;
                
                
                
            default:
                for(byte b : packet.asByteArray(OBIMP_BEX_CL)) {
                    s += b + " ";
                }
        }
        boolean debug = false;
        if(!s.equals("") && debug) {
            System.out.println(s);
        }
    }

    public static byte[] MD5(byte[] b) {
        byte hash[] = null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(b);
            hash = md.digest();
        } catch(Exception ex) {
            System.out.println("Error:" + ex);
        }
        return hash;
    }
    
}
