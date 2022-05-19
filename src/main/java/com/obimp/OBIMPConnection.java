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

package com.obimp;

import com.obimp.data.DataType;
import com.obimp.data.structure.wTLD;
import com.obimp.data.type.LongWord;
import com.obimp.data.type.QuadWord;
import com.obimp.data.type.UTF8;
import com.obimp.data.type.Word;
import com.obimp.listener.ConnectionListener;
import com.obimp.listener.ContactListListener;
import com.obimp.listener.MessageListener;
import com.obimp.listener.MetaInfoListener;
import com.obimp.listener.UserStatusListener;
import com.obimp.packet.Packet;
import com.obimp.packet.PacketHandler;
import com.obimp.packet.PacketListener;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Vector;

/**
 * OBIMP connection
 * @author Alexander Krysin
 */
public class OBIMPConnection {
    private String server = "";
    private String username = "";
    private String password = "";
    
    private Socket con;
    private DataInputStream in;
    protected DataOutputStream out;
    
    private PacketListener listener;
    private Thread t;
    
    public int seq = 0;
    
    public Vector<ConnectionListener> con_list = new Vector<ConnectionListener>();
    public Vector<MessageListener> msg_list = new Vector<MessageListener>();
    public Vector<UserStatusListener> stat_list = new Vector<UserStatusListener>();
    public Vector<MetaInfoListener> user_info = new Vector<MetaInfoListener>();
    public Vector<ContactListListener> cl_list = new Vector<ContactListListener>();
    
    public volatile boolean connected = false;
    
    public static boolean debug = false;
    
    public OBIMPConnection(String server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
    }
    
    public synchronized int getSeq() {
        return seq++;
    }
    
    public void addConnectionListener(ConnectionListener cl) {
        con_list.add(cl);
    }
    
    public void addMessageListener(MessageListener ml) {
        msg_list.add(ml);
    }
    
    public void addUserStatusListener(UserStatusListener usl) {
        stat_list.add(usl);
    }
    
    public void addMetaInfoListener(MetaInfoListener ui) {
    	System.out.println("MetaInfoListener " + ui.getClass().getName() + " has been added");
        user_info.add(ui);
    }
    
    public void addContactListListener(ContactListListener cll) {
        cl_list.add(cll);
    }
    
    public boolean removeConnectionListener(ConnectionListener cl) {
    	System.out.println("ConnectionListener " + cl.getClass().getName() + " has been removed");
        return user_info.remove(cl);
    }
    
    public boolean removeMessageListener(MessageListener ml) {
    	System.out.println("MessageListener " + ml.getClass().getName() + " has been removed");
        return user_info.remove(ml);
    }
    
    public boolean removeUserStatusListener(UserStatusListener usl) {
    	System.out.println("UserStatusListener " + usl.getClass().getName() + " has been removed");
        return user_info.remove(usl);
    }
    
    public boolean removeMetaInfoListener(MetaInfoListener ui) {
    	System.out.println("MetaInfoListener " + ui.getClass().getName() + " has been removed");
        return user_info.remove(ui);
    }
    
    public boolean removeContactListListener(ContactListListener cll) {
    	System.out.println("ContactListListener " + cll.getClass().getName() + " has been removed");
        return user_info.remove(cll);
    }
    
    public void connect() {
        try {
            this.connected = true;
            con = new Socket(server, 7023);
            in = new DataInputStream(con.getInputStream());
            out = new DataOutputStream(con.getOutputStream());
            listener = new PacketListener(con, in, this, username, password);
            t = new Thread(listener);
            
            try {
                t.start();
                
                Packet hello = new Packet(0x0001, 0x0001); // OBIMP_BEX_COM_CLI_HELLO
                hello.append(new wTLD(0x00000001, new UTF8(username)));
                send(hello);
                while(!PacketHandler.logged) {
                    Thread.sleep(100);
                }
//                Packet login = new Packet(0x0001, 0x0003); // OBIMP_BEX_COM_CLI_LOGIN
//                login.append(new wTLD(0x00000001, new UTF8(username)));
//                login.append(new wTLD(0x00000002, new OctaWord(hash)));
//                out.write(login.asByteArray(getSeq()));
//                out.flush();
                send(new Packet(0x0002, 0x0001)); // OBIMP_BEX_CL_CLI_PARAMS
                send(new Packet(0x0003, 0x0001)); // OBIMP_BEX_PRES_CLI_PARAMS
                send(new Packet(0x0004, 0x0001)); // OBIMP_BEX_IM_CLI_PARAMS
                send(new Packet(0x0005, 0x0001)); // OBIMP_BEX_UD_CLI_PARAMS
                send(new Packet(0x0006, 0x0001)); // OBIMP_BEX_UA_CLI_PARAMS
                send(new Packet(0x0007, 0x0001)); // OBIMP_BEX_FT_CLI_PARAMS
                send(new Packet(0x0008, 0x0001)); // OBIMP_BEX_TP_CLI_PARAMS
                send(new Packet(0x0003, 0x0008)); // OBIMP_BEX_PRES_CLI_REQ_PRES_INFO
                send(new Packet(0x0002, 0x0003)); // OBIMP_BEX_CL_CLI_REQUEST
                send(new Packet(0x0002, 0x0005)); // OBIMP_BEX_CL_CLI_VERIFY
                Packet pres_info = new Packet(0x0003, 0x0003); // OBIMP_BEX_PRES_CLI_SET_PRES_INFO
                pres_info.append(new wTLD(0x00000001, new DataType[] {new Word(0x0001), new Word(0x0002)}));
                pres_info.append(new wTLD(0x00000002, new Word(0x0001)));
                pres_info.append(new wTLD(0x00000003, new UTF8("Java OBIMP Lib (OBIMP4J)")));
                pres_info.append(new wTLD(0x00000004, new QuadWord(0, 0, 0, 1, 0, 0, 0, 1)));
                pres_info.append(new wTLD(0x00000005, new Word(0x0052)));
                pres_info.append(new wTLD(0x00000006, new UTF8(System.getProperty("os.name") + " " +
                        (System.getProperty("os.arch").contains("64") ? "x64" : "x86")))); //операционная система
                send(pres_info);
                Packet set_status = new Packet(0x0003, 0x0004); // OBIMP_BEX_PRES_CLI_SET_STATUS
                set_status.append(new wTLD(0x00000001, new LongWord(0, 0, 0, Status.PRES_STATUS_ONLINE)));
                //set_status.append(new wTLD(0x00000002, new UTF8("Работает!")));
                //set_status.append(new wTLD(0x00000004, new UTF8("Моя библиотека работает!")));
                //set_status.append(new wTLD(0x00000005, new UUID(1, XStatus.COFFEE)));
                send(set_status);
                send(new Packet(0x0003, 0x0005)); // OBIMP_BEX_PRES_CLI_ACTIVATE
                Packet ud_details = new Packet(0x0005, 0x0003); // OBIMP_BEX_UD_CLI_DETAILS_REQ
                ud_details.append(new wTLD(0x0001, new UTF8(username)));
                send(ud_details);
                send(new Packet(0x0004, 0x0003)); // OBIMP_BEX_IM_CLI_REQ_OFFLINE
                send(new Packet(0x0004, 0x0005)); // OBIMP_BEX_IM_CLI_DEL_OFFLINE
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        } catch(Exception ex) {
            System.out.println("Error:\n");
            ex.printStackTrace();
        }
    }
    
    public void send(Packet packet) {
        try {
            for(byte b : packet.asByteArray(0)) System.out.print(b + " ");
            System.out.println();
            out.write(packet.asByteArray(getSeq()));
        } catch(Exception ex){
            System.out.println("Error:" + ex);
        }
    }
    
    public void sendPong() {
        try {
            Packet pong = new Packet(0x0001, 0x0007); // OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG
            out.write(pong.asByteArray(getSeq()));
        } catch(Exception ex){
            System.out.println("Error:" + ex);
        }
    }
    
    public void disconnect() {
        try {
            this.connected = false;
            con.close();
            seq = 0;
            for(ConnectionListener cl : con_list) {
                cl.onLogout("USER_DISCONNECTED");
            }
            con = null;
            in = null;
            out = null;
            listener = null;
            t = null;
        } catch(Exception ex){
            System.out.println("Error:" + ex);
        }
    }

    public void setDebug(boolean _debug) {
        debug = _debug;
    }
    
    public boolean getDebug() {
        return debug;
    }

}
