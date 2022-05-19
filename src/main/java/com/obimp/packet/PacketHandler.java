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

package com.obimp.packet;

import com.obimp.OBIMPConnection;
import com.obimp.XStatus;
import com.obimp.cl.Contact;
import com.obimp.cl.ContactListItem;
import com.obimp.cl.Group;
import com.obimp.cl.Note;
import com.obimp.cl.Transport;
import com.obimp.data.structure.sTLD;
import com.obimp.data.structure.wTLD;
import com.obimp.data.type.BLK;
import com.obimp.data.type.OctaWord;
import com.obimp.data.type.UTF8;
import com.obimp.listener.ConnectionListener;
import com.obimp.listener.ContactListListener;
import com.obimp.listener.MessageListener;
import com.obimp.listener.MetaInfoListener;
import com.obimp.listener.UserStatusListener;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

/**
 * Packet handler
 * @author Alexander Krysin
 * @author Warik777
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
    public static synchronized void parsePacket(Packet pkt, HashMap<Integer, BLK> tlds, OBIMPConnection oc, String username, String password, Socket socket) throws IOException {
        String s = "";
//        System.out.println( "0x000"+pkt.getType()+", 0x000"+pkt.getSubType() );
        switch(pkt.getType()) {
            case OBIMP_BEX_COM:
                switch(pkt.getSubType()) {
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
                            Packet login = new Packet(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_LOGIN);
                            login.append(new wTLD(0x0001, new UTF8(username)));
                            if (tlds.containsKey(0x0007)) { // server requires plain-text password authentication
                                login.append(new wTLD(0x0003, new BLK(Base64.getEncoder().encode(password.getBytes()))));
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
                        //oc.send(new Packet(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG));
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
                    switch(pkt.getSubType()) {
                        case OBIMP_BEX_CL_SRV_PARAMS_REPLY:
                            s = "Server say CL PARAMS";
                            if(tlds.get(0x0001) != null)
                                s += "\n LongWord, maximal groups count ("+tlds.get(0x0001).getData()[3]+")";
                            if(tlds.get(0x0002) != null)
                                s += "\n LongWord, maximal UTF-8 encoded group name length ("+tlds.get(0x0002).getData()[3]+")";
                            if(tlds.get(0x0003) != null)
                                s += "\n LongWord, maximal contacts count all over contact list ("+tlds.get(0x0003).getData()[3]+")";
                            if(tlds.get(0x0004) != null)
                                s += "\n LongWord, maximal UTF-8 encoded account name length ("+tlds.get(0x0004).getData()[3]+")";
                            if(tlds.get(0x0005) != null)
                                s += "\n LongWord, maximal UTF-8 encoded contact name/transport friendly name length ("+tlds.get(0x0005).getData()[3]+")";
                            if(tlds.get(0x0006) != null)
                                s += "\n LongWord, maximal UTF-8 encoded authorization reason/revoke length ("+tlds.get(0x0006).getData()[3]+")";
                            if(tlds.get(0x0007) != null)
                                s += "\n LongWord, maximal user/developer sTLDs count in one item ("+tlds.get(0x0007).getData()[3]+")";
                            if(tlds.get(0x0008) != null)
                                s += "\n LongWord, maximal user/developer sTLD length ("+tlds.get(0x0008).getData()[3]+")";
                            if(tlds.get(0x0009) != null)
                                s += "\n LongWord, offline authorization messages (requests, replies, revokes) count waiting for client request ("+tlds.get(0x0009).getData()[3]+")";
                            if(tlds.get(0x000A) != null)
                                s += "\n Bool, if True then server will automatically remove authorization flag after adding contact ("+tlds.get(0x000A).getData()[0]+")";
                            if(tlds.get(0x000B) != null)
                                s += "\n LongWord, maximal notes count ("+tlds.get(0x000B).getData()[3]+")";
                            if(tlds.get(0x000C) != null)
                                s += "\n LongWord, maximal UTF-8 encoded note name length ("+tlds.get(0x000C).getData()[3]+")";
                            if(tlds.get(0x000D) != null)
                                s += "\n LongWord, maximal UTF-8 encoded note text length ("+tlds.get(0x000D).getData()[3]+")";
                            break;
                        case OBIMP_BEX_CL_SRV_REPLY:
                            s = "Server say CL";
/******************************************************************************/
//                            Vector<Byte> bite = new Vector<Byte>();
                            byte[] data = tlds.get(0x0001).getData();
//                            for(int i =0; i<data.length; i++){
//                            bite.add(i, data[i]);
//                            }
//                            System.out.println(bite.toString());
/******************************************************************************/
//                            byte[] data = tlds.get(0x0001).getData();
                            int cl_length = PacketListener.getLength(data[0], data[1], data[2], data[3]);
                            s += "\nYour CL (" + cl_length + " items):";
                            byte[] cl = new byte[data.length - 4];
                            int j = 4;
                            for(int i=0;i<cl.length;i++) {
                                cl[i] = data[j];
                                j++;
                            }
                            ContactListItem[] clist = new ContactListItem[cl_length];
                            HashMap items = parseByteToCLItems(cl_length, cl);
                            for(int i=0;i<items.size();i++) {
                                ContactListItem item = (ContactListItem) items.keySet().toArray()[i];
                                HashMap props = parseByteArrayTosTLDArray((byte[]) items.values().toArray()[i]);
                                if(item instanceof Contact) { // Contact
                                    ((Contact) item).account_name = new String(((sTLD)props.get(0x0002)).getData());
                                    ((Contact) item).contact_name = new String(((sTLD)props.get(0x0003)).getData());
                                    ((Contact) item).privacy_type = (int)((sTLD)props.get(0x0004)).getData()[0];
                                if( ((sTLD)props.get(0x0005)) != null )
                                    ((Contact) item).authorization = (((sTLD)props.get(0x0005)).getData().length == 0 ? "требуется авторизация" : "авторизован" );
                                } else if(item instanceof Group) { // Group
                                    ((Group) item).name = new String(((sTLD) props.get(0x0001)).getData());
                                } else if(item instanceof Transport) { // Transport
                                   //sTLD 0x1002: UUID, transport UUID
                                    ((Transport) item).account_name = new String(((sTLD) props.get(0x1003)).getData());
                                    ((Transport) item).friendly_name = new String(((sTLD) props.get(0x1004)).getData());
                                } else if(item instanceof Note) { // Note
                                    ((Note) item).note_name = new String( ((sTLD) props.get(0x2001)).getData() );
                                    ((Note) item).note_type = ((sTLD) props.get(0x2002)).getData()[0];
                                    if( ((sTLD) props.get(0x2003)) != null )
                                    ((Note) item).note_text = new String( ((sTLD) props.get(0x2003)).getData() );
                                  //sTLD 0x2004: DateTime, note date (UTC)
                                  //sTLD 0x2005: OctaWord, note picture MD5 hash
                                }
                                clist[i] = item;
                            } 
                            for(ContactListListener cll : oc.cl_list) {
                                cll.onLoadContactList(clist);
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
                        if(tlds.get(0x0002) != null)
                            s += " newly added Item ID ("+tlds.get(0x0001).getData()[3]+")";
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
                        case OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST:
                            String userid = new String(tlds.get(0x0001).getData());
                            String reason = new String(tlds.get(0x0002).getData());
                            for(ContactListListener cll : oc.cl_list) {
                                cll.onAuthRequest(userid, reason);
                            }
                            break;
                        case OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY:
                            String us_id = new String(tlds.get(0x0001).getData());
                            boolean reply = (int) tlds.get(0x0002).getData()[1] == 0x01 ? true : false;
                            for(ContactListListener cll : oc.cl_list) {
                                cll.onAuthReply(us_id, reply);
                            }
                            break;
                        case OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE:
                            String uid = new String(tlds.get(0x0001).getData());
                            String rsn = new String(tlds.get(0x0002).getData());
                            for(ContactListListener cll : oc.cl_list) {
                                cll.onAuthRevoke(uid, rsn);
                            }
                            break;
                        case OBIMP_BEX_CL_SRV_DONE_OFFAUTH:
                            s = "Server say CL DONE OFFAUTH";
                            oc.send(new Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_DEL_OFFAUTH));
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
                            if(tlds.get(0x0002) != null)
                                s += " contact list Item Type ("+tlds.get(0x0002).getData()[1]+")";
                            if(tlds.get(0x0003) != null)
                                s += " Item ID ("+tlds.get(0x0003).getData()[3]+")";
                            if(tlds.get(0x0004) != null)
                                s += " Group ID ("+tlds.get(0x0004).getData()[3]+")";
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
                switch(pkt.getSubType()) {
                    case OBIMP_BEX_PRES_SRV_PARAMS_REPLY:
                        s = "Server say PRES PARAMS";
                        if(tlds.get(0x0001) != null)
                           s += "\n LongWord, maximal UTF-8 encoded status name length ("+tlds.get(0x0001).getData()[3]+")";
                        if(tlds.get(0x0002) != null)
                           s += "\n LongWord, maximal UTF-8 encoded additional status picture length ("+tlds.get(0x0002).getData()[3]+")";
                        if(tlds.get(0x0003) != null)
                           s += "\n LongWord, maximal UTF-8 encoded client name length ("+tlds.get(0x0003).getData()[3]+")";
                        if(tlds.get(0x0004) != null)
                           s += "\n LongWord, maximal capabilities count ("+tlds.get(0x0004).getData()[3]+")";
                        if(tlds.get(0x0005) != null)
                           s += "\n LongWord, required optional client information flags (see below) ("+tlds.get(0x0005).getData()[3]+")";
                        break;
                    case OBIMP_BEX_PRES_SRV_CONTACT_ONLINE:
                        HashMap<String, String> user = new HashMap<String, String>();
                        byte[] st = tlds.get(0x0002).getData();
                        int status = (int) st[0] + (int) st[1] + (int) st[2] + (int) st[3];
                        user.put("StatusValue", String.valueOf(status));
                        byte[] v = tlds.get(0x0009).getData();
                        user.put( "Version", ((int) v[0] + (int) v[1]) + "." + ((int) v[2] + (int) v[3]) + "." + ((int) v[4] + (int) v[5]) + "." + ((int) v[6] + (int) v[7]) );
                        String caps = "Характеристики клиента:\n";
                        for(int i=0; i<tlds.get(0x0006).getData().length;i++){
                            if( tlds.get(0x0006).getData()[i]>0 ) {
                                switch(tlds.get(0x0006).getData()[i]){
                                    case 0x0001: caps += "[UTF-8 сообщения]\n"; break;
                                    case 0x0002: caps += "[RTF сообщения]\n"; break;
                                    case 0x0003: caps += "[HTML сообщения]\n"; break;
                                    case 0x0004: caps += "[Шифрование сообщений]\n"; break;
                                    case 0x0005: caps += "[Оповещение о наборе текста]\n"; break;
                                    case 0x0006: caps += "[Аватары]\n"; break;
                                    case 0x0007: caps += "[Передача файлов]\n"; break;
                                    case 0x0008: caps += "[Транспорты]\n"; break;
                                    case 0x0009: caps += "[Оповещения будильника]\n"; break;
                                    case 0x000A: caps += "[Оповещения почты]\n"; break;
                                }
                            }
                        }
                        user.put( "Caps", caps );
                        if(tlds.get(0x0004) != null){
                            if(tlds.get(0x0004).getData().length >0){
                                user.put( "XTitle", XStatus.X_Status[tlds.get(0x0004).getData()[3]] );
                            }
                        } else user.put( "XTitle", XStatus.X_Status[0] );
                        if(tlds.get(0x0005) !=null){
                            if(tlds.get(0x0005).getData().length>0){
                                user.put( "XDesc", new String(tlds.get(0x0005).getData()) );
                            }
                        } else user.put( "XDesc", XStatus.X_Status[0] );
                        user.put( "User", new String(tlds.get(0x0001).getData()) );
                        if(tlds.get(0x0003) != null) user.put( "Status", new String(tlds.get(0x0003).getData()) );
                        user.put( "Client", new String(tlds.get(0x0008).getData()) );
                        user.put( "Os", new String(tlds.get(0x000F).getData()) );
                        if(tlds.get(0x0012) !=null){
                            s += "\n BLK, array of client identification sTLDs defined by transport (" + ((BLK)tlds.get(0x0012)).getData() + ")";
                        }
                        if(tlds.get(0x1001) !=null){
                            s += "\n LongWord, transport Item ID (" + tlds.get(0x1001).getData()[3] + ")";
                        }
                        if(tlds.get(0x0011) !=null){
                            s += "\n Byte, custom transport status picture ID (" + tlds.get(0x0011).getData()[0] + ")";
                        }
                        if(tlds.get(0x000A) !=null){
                            s += "\n DateTime, client connected time (" + new Date((new DataInputStream(new ByteArrayInputStream(tlds.get(0x000A).getData()))).readLong()*1000L).toString() + ")";
                        }
                        if(tlds.get(0x000B) !=null){
                           s += "\n DateTime, registration date (" + new Date((new DataInputStream(new ByteArrayInputStream(tlds.get(0x000B).getData()))).readLong()*1000L).toString() + ")";
                        }
                        for(UserStatusListener usl : oc.stat_list) {
                            usl.onUserOnline(user);
                        }
                        break;
                    case OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE:
                        String id = new String(tlds.get(0x0001).getData());
                        s = "Contact offline " + id;
                        if(tlds.get(0x1001) !=null){
                           s += "\n LongWord, transport Item ID (" + tlds.get(0x1001).getData()[3] + ")";
                        }
                        for(UserStatusListener usl : oc.stat_list) {
                            usl.onUserOffline(id);
                        }
                        break;
                    case OBIMP_BEX_PRES_SRV_PRES_INFO:
                        s = "Server say PRES INFO";
                        if(tlds.get(0x0001) != null)
                            if(tlds.get(0x0001).getData().length>0)
                            s += "\n UTF8, client's account name ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null)
                            s += "\n DateTime, registration date ("+new Date((new DataInputStream(new ByteArrayInputStream(tlds.get(0x0002).getData()))).readLong()*1000L).toString()+")";
                        if(tlds.get(0x0003) != null)
                            s += "\n DateTime, current session client connected time ("+new Date((new DataInputStream(new ByteArrayInputStream(tlds.get(0x0003).getData()))).readLong()*1000L).toString()+")";
                        if(tlds.get(0x0004) != null)
                            s += "\n DateTime, previous session client connected time ("+new Date((new DataInputStream(new ByteArrayInputStream(tlds.get(0x0004).getData()))).readLong()*1000L).toString()+")";
                        if(tlds.get(0x0005) != null)
                            if(tlds.get(0x0005).getData().length>0)
                            s += "\n UTF8, current session client IP address (as it seen by server) ("+new String(tlds.get(0x0005).getData())+")";
                        if(tlds.get(0x0006) != null)
                            if(tlds.get(0x0006).getData().length>0)
                            s += "\n UTF8, previous session client IP address (as it seen by server) ("+new String(tlds.get(0x0006).getData())+")";
                        if(tlds.get(0x0007) != null)
                            s += "\n Word, currently signed on instances count ("+tlds.get(0x0007).getData()[1]+")";
                        if(tlds.get(0x0008) != null)
                            if(tlds.get(0x0008).getData().length>0)
                            s += "\n UTF8, additional server added description ("+new String(tlds.get(0x0008).getData())+")";
                        if(tlds.get(0x0064) != null){
                            s += "\n BLK, presence information data array (see below how it can be parsed) ( КЛ )";
//                             for(int i=0; i<((BLK)tlds.get(0x0064)).getData().length; i++)
//                                 s += ((BLK)tlds.get(0x0064)).getData()[i]+",";
                        }
                        if(tlds.get(0x1001) != null)
                            s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
                        break;
                    case OBIMP_BEX_PRES_SRV_MAIL_NOTIF:
                        s = "Server say MAIL NOTIF";
                        if(tlds.get(0x0003) != null)
                        s += "\n UTF8, receiver email address ("+new String(tlds.get(0x0003).getData())+")";
                        if(tlds.get(0x0004) != null)
                        s += "\n UTF8, sender name ("+new String(tlds.get(0x0004).getData())+")";
                        if(tlds.get(0x0005) != null)
                        s += "\n UTF8, sender email address ("+new String(tlds.get(0x0005).getData())+")";
                        if(tlds.get(0x0006) != null)
                        s += "\n UTF8, mail subject ("+new String(tlds.get(0x0006).getData())+")";
                        if(tlds.get(0x0007) != null)
                        s += "\n UTF8, mail text ("+new String(tlds.get(0x0007).getData())+")";
                        if(tlds.get(0x0008) != null)
                        s += "\n UTF8, mailbox URL ("+new String(tlds.get(0x0008).getData())+")";
                        if(tlds.get(0x1001) != null)
                            s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
                        break;
                    case OBIMP_BEX_PRES_SRV_OWN_MAIL_URL:
                        s = "Server say OWN MAIL URL ";
                        if(tlds.get(0x1001) != null)
                            s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
                        break;
                }
                break;
            case OBIMP_BEX_IM:
                switch(pkt.getSubType()) {
                    case OBIMP_BEX_IM_SRV_PARAMS_REPLY:
                        s = "Server say IM PARAMS";
                        if(tlds.get(0x0001) != null)
                            s += "\n LongWord, maximal UTF-8 encoded account name length ("+tlds.get(0x0001).getData()[3]+")";
                        if(tlds.get(0x0002) != null)
                            s += "\n LongWord, maximal message data length ("+tlds.get(0x0002).getData()[3]+")";
                        if(tlds.get(0x0003) != null)
                            s += "\n LongWord, offline messages count waiting for client request ("+tlds.get(0x0003).getData()[3]+")";
                        if(tlds.get(0x0004) != null)
                            s += "\n LongWord, if True then client can send multiple message BEX ("+tlds.get(0x0004).getData()[0]+")";
                        break;
                    case OBIMP_BEX_IM_SRV_DONE_OFFLINE:
                        s = "Server say IM DONE OFFLINE";
                        oc.send(new Packet(OBIMP_BEX_IM, OBIMP_BEX_IM_CLI_DEL_OFFLINE));
                        break;
                    case OBIMP_BEX_IM_SRV_MESSAGE:
                        String id = new String(tlds.get(0x0001).getData());
                        String text = new String(tlds.get(0x0004).getData());
                        s = "New message from " + id + ": " + text;
                        //OBIMP.set_upd_info(oc, "jimbot");
                        for(MessageListener ml : oc.msg_list) {
                            ml.onIncomingMessage(id, text);
                        }
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of message sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null)
                            s += "\n LongWord, unique message ID ("+tlds.get(0x0002).getData()[3]+")";
                        if(tlds.get(0x0003) != null)
                            s += "\n LongWord, message type ("+tlds.get(0x0003).getData()[3]+")";
                        if(tlds.get(0x0004) != null)
                            s += "\n BLK, message data ("+new String(tlds.get(0x0004).getData())+")";
                        if(tlds.get(0x0005) != null)
                            s += "\n empty, request message delivery report from remote client ("+tlds.get(0x0005).getData().length+")";
                        if(tlds.get(0x0006) != null)
                            s += "\n LongWord, encryption type (see corresponding BEX for types) ("+tlds.get(0x0006).getData()[3]+")";
                        if(tlds.get(0x0007) != null)
                            s += "\n empty, offline message flag ("+tlds.get(0x0007).getData().length+")";
                        if(tlds.get(0x0008) != null)
                            s += "\n DateTime, offline message time ("+new Date((new DataInputStream(new ByteArrayInputStream(tlds.get(0x0008).getData()))).readLong()*1000L).toString()+")";
                        if(tlds.get(0x0009) != null)
                            s += "\n empty, system message flag ("+tlds.get(0x0009).getData().length+")";
                        if(tlds.get(0x000A) != null)
                            s += "\n Byte, system message popup position (0 - default, 1 - screen center) ("+tlds.get(0x000A).getData()[0]+")";
                        if(tlds.get(0x000B) != null)
                            s += "\n empty, multiple message flag ("+tlds.get(0x000B).getData().length+")";
                        if(tlds.get(0x1001) != null)
                            s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
                        break;
                    case OBIMP_BEX_IM_CLI_SRV_MSG_REPORT:
                        s = "Server say IM MSG REPORT";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of delivery report receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null)
                            s += "\n LongWord, unique received message ID ("+tlds.get(0x0002).getData()[3]+")";
                        if(tlds.get(0x1001) != null)
                            s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
                        break;
                    case OBIMP_BEX_IM_CLI_SRV_NOTIFY:
                        s = "Server say IM NOTIFY";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of notification receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null){
                            s += "\n LongWord, notification type (see below) = ";
                            switch(tlds.get(0x0002).getData()[3]){
                                case 0x0001:
                                    s += "NOTIF_VALUE_USER_TYPING_START";
                                    break;
                                case 0x0002:
                                    s += "NOTIF_VALUE_USER_TYPING_FINISH";
                                    break;
                                case 0x0003:
                                    s += "NOTIF_VALUE_WAKE_ALARM_PLAY";
                                    break;
                                case 0x0004:
                                    s += "NOTIF_VALUE_WAKE_ALARM_WAIT";
                                    break;
                            }
                        }
                        if(tlds.get(0x0003) != null){
                            s += "\n LongWord, notification value (see below) = ";
                            switch(tlds.get(0x0003).getData()[3]){
                                case 0x0001:
                                    s += "NOTIF_TYPE_USER_TYPING";
                                    break;
                                case 0x0002:
                                    s += "NOTIF_TYPE_WAKE_ALARM";
                                    break;
                            }
                        }
                        if(tlds.get(0x1001) != null)
                            s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
                        break;
                    case OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REQ:
                        s = "Server say IM ENCRYPT KEY REQ";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of public key request receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        break;
                    case OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY:
                        s = "Server say IM ENCRYPT KEY REPLY";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of public key receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null){
                            s += "\n LongWord, encryption type (see below) = ";
                            switch(tlds.get(0x0002).getData()[3]){
                                case 0x0000:
                                    s += "(encryption disabled or not supported)";
                                    break;
                                case 0x0001:
                                    s += "(OBIMP client default internal encryption)";
                                    break;
                                case 0x0002:
                                    s += "(currently is not used)";
                                    break;
                            }
                        }
                        if(tlds.get(0x0003) != null)
                            s += "\n BLK, public key ("+tlds.get(0x0003).getData().length+")";
                        break;
                }
                break;
            case OBIMP_BEX_UD:
                switch(pkt.getSubType()) {
                    case OBIMP_BEX_UD_SRV_PARAMS_REPLY:
                        s = "Server say UD PARAMS";
                        if(tlds.get(0x0001) != null)
                            s += "\n LongWord, maximal UTF-8 encoded account name length ("+tlds.get(0x0001).getData()[3]+")";
                        if(tlds.get(0x0002) != null)
                            s += "\n LongWord, maximal UTF-8 encoded details field length ("+tlds.get(0x0002).getData()[3]+")";
                        if(tlds.get(0x0003) != null)
                            s += "\n LongWord, maximal UTF-8 encoded about field length ("+tlds.get(0x0003).getData()[3]+")";
                        if(tlds.get(0x0004) != null)
                            s += "\n Bool, if True then client can change account's secure email ("+tlds.get(0x0004).getData().length+")";
                        if(tlds.get(0x0005) != null)
                            s += "\n Bool, if True then client can change account's password ("+tlds.get(0x0005).getData().length+")";
                        if(tlds.get(0x0006) != null)
                            s += "\n UTF8, secure email/password changing URL if available ("+new String(tlds.get(0x0006).getData())+")";
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

                        String out_inf = "Информация о пользователе:";
                        if( new String(tlds.get(0x0002).getData()).equals(username) ){
                        }else {
                        if(tlds.get(0x0002) !=null)
                        if(tlds.get(0x0002).getData().length>0) out_inf += "\naccount name: "+new String(tlds.get(0x0002).getData());
                        if(tlds.get(0x0003) !=null) 
                        if(tlds.get(0x0003).getData().length>0) out_inf += "\nsecure email: "+new String(tlds.get(0x0003).getData());
                        if(tlds.get(0x0004) !=null)
                        if(tlds.get(0x0004).getData().length>0) out_inf += "\nnick name: "+new String(tlds.get(0x0004).getData());
                        if(tlds.get(0x0005) !=null)
                        if(tlds.get(0x0005).getData().length>0) out_inf += "\nfirst name: "+new String(tlds.get(0x0005).getData());
                        if(tlds.get(0x0006) !=null)
                        if(tlds.get(0x0006).getData().length>0) out_inf += "\nlast name: "+new String(tlds.get(0x0006).getData());
                        if(tlds.get(0x0008) !=null)
                        if(tlds.get(0x0008).getData().length>0) out_inf += "\nregion/state: "+new String(tlds.get(0x0008).getData());
                        if(tlds.get(0x0009) !=null)
                        if(tlds.get(0x0009).getData().length>0) out_inf += "\ncity: "+new String(tlds.get(0x0009).getData());
                        if(tlds.get(0x000A) !=null)
                        if(tlds.get(0x000A).getData().length>0) out_inf += "\nzip code: "+new String(tlds.get(0x000A).getData());
                        if(tlds.get(0x000B) !=null)
                        if(tlds.get(0x000B).getData().length>0) out_inf += "\naddress: "+new String(tlds.get(0x000B).getData());
                        if(tlds.get(0x0010) !=null)
                        if(tlds.get(0x0010).getData().length>0) out_inf += "\nhomepage: "+new String(tlds.get(0x0010).getData());
                        if(tlds.get(0x0011) !=null)
                        if(tlds.get(0x0011).getData().length>0) out_inf += "\nabout: "+new String(tlds.get(0x0011).getData());
                        if(tlds.get(0x0012) !=null)
                        if(tlds.get(0x0012).getData().length>0) out_inf += "\ninterests: "+new String(tlds.get(0x0012).getData());
                        if(tlds.get(0x0013) !=null)
                        if(tlds.get(0x0013).getData().length>0) out_inf += "\nemail: "+new String(tlds.get(0x0013).getData());
                        if(tlds.get(0x0014) !=null)
                        if(tlds.get(0x0014).getData().length>0) out_inf += "\nadditional email: "+new String(tlds.get(0x0014).getData());
                        if(tlds.get(0x0015) !=null)
                        if(tlds.get(0x0015).getData().length>0) out_inf += "\nhome phone: "+new String(tlds.get(0x0015).getData());
                        if(tlds.get(0x0016) !=null)
                        if(tlds.get(0x0016).getData().length>0) out_inf += "\nwork phone: "+new String(tlds.get(0x0016).getData());
                        if(tlds.get(0x0017) !=null)
                        if(tlds.get(0x0017).getData().length>0) out_inf += "\ncellular phone: "+new String(tlds.get(0x0017).getData());
                        if(tlds.get(0x0018) !=null)
                        if(tlds.get(0x0018).getData().length>0) out_inf += "\nfax number: "+new String(tlds.get(0x0018).getData());
                        if(tlds.get(0x0019) !=null) out_inf += "\nif True then online status will not be shown in users directory: "+tlds.get(0x0019).getData().length;
                        if(tlds.get(0x001A) !=null)
                        if(tlds.get(0x001A).getData().length>0) out_inf += "\ncompany: "+new String(tlds.get(0x001A).getData());
                        if(tlds.get(0x001B) !=null)
                        if(tlds.get(0x001B).getData().length>0) out_inf += "\ndivision/department: "+new String(tlds.get(0x001B).getData());
                        if(tlds.get(0x001C) !=null)
                        if(tlds.get(0x001C).getData().length>0) out_inf += "\nposition: "+new String(tlds.get(0x001C).getData());
                        if(tlds.get(0x1001) !=null) out_inf += "\ntransport Item ID: "+tlds.get(0x1001).getData()[3];
                            for(MetaInfoListener ml : oc.user_info) {
                                ml.onUserInfo(out_inf);
                            }
                        }
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
                        if(tlds.get(0x1001) !=null) 
                            s += "\n LongWord, transport Item ID: "+tlds.get(0x1001).getData()[3];
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
                        String out_search = "";
                        if(tlds.get(0x0002) != null)
                        if(tlds.get(0x0002).getData().length>0) out_search += "\naccount name: "+new String(tlds.get(0x0002).getData());
                        if(tlds.get(0x0003) != null)
                        if(tlds.get(0x0003).getData().length>0) out_search += "\nnick name: "+new String(tlds.get(0x0003).getData());
                        if(tlds.get(0x0004) != null)
                        if(tlds.get(0x0004).getData().length>0) out_search += "\nfirst name: "+new String(tlds.get(0x0004).getData());
                        if(tlds.get(0x0005) != null)
                        if(tlds.get(0x0005).getData().length>0) out_search += "\nlast name: "+new String(tlds.get(0x0005).getData());
                        if(tlds.get(0x0006) != null)
                        if(tlds.get(0x0006).getData().length>0){
                            switch(tlds.get(0x0006).getData()[0]){
                                case 0x00:
                                    out_search += "\ngender: not specified";
                                    break;
                                case 0x01:
                                    out_search += "\ngender: female";
                                    break;
                                case 0x02:
                                    out_search += "\ngender: male";
                                    break;
                            }
                        }
                        if(tlds.get(0x0007) != null)
                        if(tlds.get(0x0007).getData()[0]>13)
                            out_search += "\nage: "+Integer.toString( tlds.get(0x0007).getData()[0] );
                        if(tlds.get(0x0008) != null){
                        if(new String(tlds.get(0x0008).getData()).isEmpty())
                            out_search += "\nuser: online";
                        } else out_search += "\nuser: offline";
                        if(tlds.get(0x0009) != null)
                            s += "\n empty, last search result ("+tlds.get(0x0009).getData().length+")";
                        if(tlds.get(0x000A) != null)
                            s += "\n LongWord, total count of results in DB ("+tlds.get(0x000A).getData()[3]+")";
                        if(tlds.get(0x000B) != null)
                            s += "\n LongWord, status picture flags and index starting from 1 (see below) ("+tlds.get(0x000B).getData()[3]+")";
                        if(tlds.get(0x1001) != null)
                            s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
                            for(MetaInfoListener ml : oc.user_info) {
                                ml.onSearch(out_search);
                            }
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
                switch(pkt.getSubType()) {
                    case OBIMP_BEX_UA_SRV_PARAMS_REPLY:
                        s = "Server say UA PARAMS";
                        if(tlds.get(0x0001) != null)
                            s += "\n LongWord, maximal avatar file size ("+tlds.get(0x0001).getData()[3]+")";
                        if(tlds.get(0x0002) != null){
                            for(int i=0; i<tlds.get(0x0002).getData().length;i++)
                            s += "\n OctaWord, current client avatar file MD5 hash ("+tlds.get(0x0002).getData()[i]+")";
                        }
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
                       if(tlds.get(0x0002) != null){
                           for(int i=0; i<tlds.get(0x0002).getData().length;i++)
                           s += "\n OctaWord, avatar file MD5 hash ("+tlds.get(0x0002).getData()[i]+")";
                       }
                       if(tlds.get(0x0003) != null){
                           for(int i=0; i<tlds.get(0x0003).getData().length;i++)
                           s += "\n BLK, avatar file ("+tlds.get(0x0003).getData()[i]+")";
                       }
                       if(tlds.get(0x1001) != null)
                           s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
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
                       if(tlds.get(0x1001) != null)
                           s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
                        break;
                }
                break;
            case OBIMP_BEX_FT:
                switch(pkt.getSubType()) {
                    case OBIMP_BEX_FT_SRV_PARAMS_REPLY:
                        s = "Server say FT PARAMS";
                        if(tlds.get(0x0001) != null)
                           s += "\n LongWord, maximal UTF-8 encoded account name length ("+tlds.get(0x0001).getData()[3]+")";
                        if(tlds.get(0x0002) != null)
                           s += "\n LongWord, maximal UTF-8 encoded host/IP length ("+tlds.get(0x0002).getData()[3]+")";
                        if(tlds.get(0x0003) != null)
                           s += "\n LongWord, maximal UTF-8 encoded file name length ("+tlds.get(0x0003).getData()[3]+")";
                        if(tlds.get(0x0004) != null)
                           s += "\n LongWord, maximal UTF-8 encoded file path length ("+tlds.get(0x0004).getData()[3]+")";
                        if(tlds.get(0x0005) != null)
                           s += "\n Bool, if value is True then FT support is enabled ("+tlds.get(0x0005).getData()[0]+")";
                        if(tlds.get(0x0006) != null)
                           s += "\n Bool, if value is True then proxied file transfer support is enabled ("+tlds.get(0x0006).getData()[0]+")";
                        if(tlds.get(0x0007) != null)
                           s += "\n UTF8, file proxy server host/IP ("+new String(tlds.get(0x0007).getData())+")";
                        if(tlds.get(0x0008) != null)
                           s += "\n LongWord, file proxy server port number ("+tlds.get(0x0008).getData()[3]+")";
                        break;
                    case OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST:
                        s = "Server say FT SEND FILE REQUEST";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of file(s) receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null){
                            s += "\n QuadWord, unique file transfer ID (";
                            for(int i=0; i<tlds.get(0x0002).getData().length; i++)
                                s += tlds.get(0x0002).getData()[i];
                            s += ")";
                        }
                        if(tlds.get(0x0001) != null)
                            s += "\n LongWord, files count ("+tlds.get(0x0003).getData()[3]+")";
                        if(tlds.get(0x0004) != null){
                            s += "\n QuadWord, file size or total size of the all files if more than one (";
                            for(int i=0; i<tlds.get(0x0004).getData().length; i++)
                                s += tlds.get(0x0004).getData()[i];
                            s += ")";
                        }
                        if(tlds.get(0x0005) != null)
                            s += "\n UTF8, file name of file name of the first file if more than one ("+new String(tlds.get(0x0005).getData())+")";
                        if(tlds.get(0x0006) != null)
                            s += "\n UTF8, client's IP address of sender ("+new String(tlds.get(0x0006).getData())+")";
                        if(tlds.get(0x0007) != null)
                            s += "\n LongWord, client's port number of sender that is listening for direct FT connection ("+tlds.get(0x0007).getData()[3]+")";
                        if(tlds.get(0x0008) != null)
                            s += "\n UTF8, file proxy server host/IP ("+new String(tlds.get(0x0008).getData())+")";
                        if(tlds.get(0x0009) != null)
                            s += "\n LongWord, file proxy server port number ("+tlds.get(0x0009).getData()[3]+")";
                        break;
                    case OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY:
                        s = "Server say FT SEND FILE REPLY";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of file(s) receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null){
                            s += "\n QuadWord, unique received file transfer ID (";
                            for(int i=0; i<tlds.get(0x0002).getData().length; i++)
                                s += tlds.get(0x0002).getData()[i];
                            s += ")";
                        }
                        if(tlds.get(0x0003) != null){
                            s += "\n Word, FT reply code (";
                            switch(tlds.get(0x0003).getData()[1]){
                                case 0x0001:
                                    s += "FT_REPLY_CODE_ACCEPT";
                                    break;
                                case 0x0002:
                                    s += "FT_REPLY_CODE_DECLINE";
                                    break;
                                case 0x0003:
                                    s += "FT_REPLY_CODE_DISABLED";
                                    break;
                                case 0x0004:
                                    s += "FT_REPLY_CODE_NOT_ALLOWED";
                                    break;
                            }
                            s += ")";
                        }
                        if(tlds.get(0x0004) != null)
                            s += "\n UTF8, client's IP address of receiver ("+new String(tlds.get(0x0004).getData())+")";
                        if(tlds.get(0x0005) != null)
                            s += "\n LongWord, client's port number of receiver that is listening for direct FT connection ("+tlds.get(0x0005).getData()[3]+")";
                        break;
                    case OBIMP_BEX_FT_CLI_SRV_CONTROL:
                        s = "Server say FT CONTROL";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of file(s) receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null){
                            s += "\n QuadWord, unique received file transfer ID (";
                            for(int i=0; i<tlds.get(0x0002).getData().length; i++)
                                s += tlds.get(0x0002).getData()[i];
                            s += ")";
                        }
                        if(tlds.get(0x0003) != null){
                            s += "\n Word, FT control code (";
                            switch(tlds.get(0x0003).getData()[1]){
                                case 0x0001:
                                    s += "FT_CONTROL_CODE_CANCEL";
                                    break;
                                case 0x0002:
                                    s += "FT_CONTROL_CODE_DIRECT_FAILED";
                                    break;
                                case 0x0003:
                                    s += "FT_CONTROL_CODE_DIRECT_FAILED_TRY_REVERSE";
                                    break;
                                case 0x0004:
                                    s += "FT_CONTROL_CODE_DIRECT_FAILED_TRY_PROXY";
                                    break;
                                case 0x0005:
                                    s += "FT_CONTROL_CODE_PROXY_FAILED";
                                    break;
                                case 0x0006:
                                    s += "FT_CONTROL_CODE_READY";
                                    break;
                            }
                            s += ")";
                        }
                        break;
                    case OBIMP_BEX_FT_DIR_PROX_ERROR:
                        s = "Server say FT DIR PROX ERROR";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of file(s) receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null){
                            s += "\n QuadWord, unique received file transfer ID (";
                            for(int i=0; i<tlds.get(0x0002).getData().length; i++)
                                s += tlds.get(0x0002).getData()[i];
                            s += ")";
                        }
                        if(tlds.get(0x0003) != null){
                            s += "\n Word, FT error code (";
                            switch(tlds.get(0x0003).getData()[1]){
                                case 0x0001:
                                    s += "FT_ERROR_CODE_TIMEOUT";
                                    break;
                                case 0x0002:
                                    s += "FT_ERROR_CODE_WRONG_UNIQ_FT_ID";
                                    break;
                                case 0x0003:
                                    s += "FT_ERROR_CODE_WRONG_FILE_NAME";
                                    break;
                                case 0x0004:
                                    s += "FT_ERROR_CODE_WRONG_RELATIVE_PATH";
                                    break;
                                case 0x0005:
                                    s += "FT_ERROR_CODE_WRONG_RESUME_POS";
                                    break;
                                case 0x0006:
                                    s += "FT_ERROR_CODE_PROXY_TRAFFIC_LIMIT";
                                    break;
                            }
                            s += ")";
                        }
                        break;
                    case OBIMP_BEX_FT_DIR_PROX_HELLO:
                        s = "Server say FT DIR PROX HELLO";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of file(s) receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null){
                            s += "\n QuadWord, unique file transfer ID (";
                            for(int i=0; i<tlds.get(0x0002).getData().length; i++)
                                s += tlds.get(0x0002).getData()[i];
                            s += ")";
                        }
                        break;
                    case OBIMP_BEX_FT_DIR_PROX_FILE:
                        s = "Server say FT DIR PROX FILE";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of file(s) receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null){
                            s += "\n QuadWord, unique file transfer ID (";
                            for(int i=0; i<tlds.get(0x0002).getData().length; i++)
                                s += tlds.get(0x0002).getData()[i];
                            s += ")";
                        }
                        if(tlds.get(0x0003) != null){
                            s += "\n QuadWord, file size  (";
                            for(int i=0; i<tlds.get(0x0003).getData().length; i++)
                                s += tlds.get(0x0003).getData()[i];
                            s += ")";
                        }
                        if(tlds.get(0x0004) != null)
                            s += "\n UTF8, file name("+new String(tlds.get(0x0004).getData())+")";
                        if(tlds.get(0x0005) != null)
                            s += "\n UTF8, relative file path(if sending folders with files) = ("+new String(tlds.get(0x0005).getData())+")";
                        break;
                    case OBIMP_BEX_FT_DIR_PROX_FILE_REPLY:
                        s = "Server say FT DIR PROX FILE REPLY";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of file(s) receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null){
                            s += "\n QuadWord, unique file transfer ID (";
                            for(int i=0; i<tlds.get(0x0002).getData().length; i++)
                                s += tlds.get(0x0002).getData()[i];
                            s += ")";
                        }
                        if(tlds.get(0x0003) != null){
                            s += "\n QuadWord, resume position where from start sending file data (";
                            for(int i=0; i<tlds.get(0x0003).getData().length; i++)
                                s += tlds.get(0x0003).getData()[i];
                            s += ")";
                        }
                        break;
                    case OBIMP_BEX_FT_DIR_PROX_FILE_DATA:
                        s = "Server say FT DIR PROX FILE DATA";
                        if(tlds.get(0x0001) != null)
                            s += "\n UTF8, account name of file(s) receiver/sender ("+new String(tlds.get(0x0001).getData())+")";
                        if(tlds.get(0x0002) != null){
                            s += "\n QuadWord, unique file transfer ID (";
                            for(int i=0; i<tlds.get(0x0002).getData().length; i++)
                                s += tlds.get(0x0002).getData()[i];
                            s += ")";
                        }
                        if(tlds.get(0x0003) != null)
                            s += "\n Bool, if True then this is the last file ("+tlds.get(0x0003).getData()[1]+")";
                        if(tlds.get(0x0004) != null)
                            s += "\n Bool, if True then this is the last part of file ("+tlds.get(0x0004).getData()[1]+")";
                        if(tlds.get(0x0005) != null)
                            s += "\n UTF8, account name of file(s) receiver/sender ("+tlds.get(0x0005).getData().length+")";
                        break;
                }
                break;
            case OBIMP_BEX_TP:
                switch(pkt.getSubType()) {
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
                        if(tlds.get(0x1001) != null)
                           s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
                        if(tlds.get(0x0002) != null)
                           s += "\n Bool, auto close notificaton after some seconds ("+tlds.get(0x0002).getData()[0]+")";
                        if(tlds.get(0x0003) != null)
                           s += "\n Bool, error/warning notification style ("+tlds.get(0x0003).getData()[0]+")";
                        if(tlds.get(0x0004) != null)
                           s += "\n UTF8, notification title text ("+new String(tlds.get(0x0004).getData())+")";
                        if(tlds.get(0x0005) != null)
                           s += "\n UTF8, notification content text ("+new String(tlds.get(0x0005).getData())+")";
                        break;
                    case OBIMP_BEX_TP_SRV_OWN_AVATAR_HASH:
                        s = "Server say TP OWN AVATAR HASH";
                        if(tlds.get(0x1001) != null)
                           s += "\n LongWord, transport Item ID ("+tlds.get(0x1001).getData()[3]+")";
                        if(tlds.get(0x0002) != null){
                           for(int i=0; i<tlds.get(0x0002).getData().length;i++)
                           s += "\n OctaWord, avatar MD5 hash ("+tlds.get(0x0002).getData()[i]+")";
                       }
                        break;
                }
                break;
                
                
                
            default:
                for(byte b : pkt.asByteArray(OBIMP_BEX_CL)) {
                    s += b + " ";
                }
        }

        if(!s.equals("") && oc.getDebug()) {
            System.err.println("[DEBUG] "+s);
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
    
    private static HashMap parseByteArrayTosTLDArray(byte[] data) {
        HashMap<Integer, sTLD> result = new HashMap<Integer, sTLD>();
        while(data.length > 0) {
            int type = (int) data[0] + (int) data[1];
            int length = (int) data[2] + (int) data[3];
            byte[] value = new byte[length];
            for(int i=4;i<length+4;i++) {
                value[i-4] = data[i];
            }
            result.put(type, new sTLD(type, new BLK(value)));
            byte[] next = new byte[data.length-4-length];
            for(int i=4+length;i<data.length;i++) {
                next[i-4-length] = data[i];
            }
            data = next;
        }
        return result;
    }
    
    private static HashMap parseByteToCLItems(int count, byte[] data) {
        HashMap<ContactListItem, byte[]> result = new HashMap<ContactListItem, byte[]>();
        int c = 0;
        while(data.length > 0) {
            int type = PacketListener.getLength((byte) 0, (byte) 0, data[0], data[1]);
            int item_id = PacketListener.getLength(data[2], data[3], data[4], data[5]);
            int group_id = PacketListener.getLength(data[6], data[7], data[8], data[9]);
            int length = PacketListener.getLength(data[10], data[11], data[12], data[13]);
            byte[] value = new byte[length];
            for(int i=14;i<length+14;i++) {
                value[i-14] = data[i];
            }
//            result.put(type == 2 ? new Contact(item_id, group_id, "", "", 0) : new Group(item_id, group_id, ""), value);
            switch(type){
                case 0x0001: //CL_ITEM_TYPE_GROUP 
                    result.put(new Group(item_id, group_id, ""), value);
                    break;
                case 0x0002: //CL_ITEM_TYPE_CONTACT 
                    result.put(new Contact(item_id, group_id, "", "", 0, ""), value);
                    break;
                case 0x0003: //CL_ITEM_TYPE_TRANSPORT 
                    result.put(new Transport("", ""), value);
                    break;
                case 0x0004: //CL_ITEM_TYPE_NOTE 
                    result.put(new Note("", 0, ""), value);
                    break;
            }
            
            byte[] next = new byte[data.length-14-length];
            for(int i=14+length;i<data.length;i++) {
                next[i-14-length] = data[i];
            }
            data = next;
            c++;
        }
        return result;
    }    
}
