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

package com.obimp;

import com.obimp.data.structure.wTLD;
import com.obimp.data.type.BLK;
import com.obimp.data.type.LongWord;
import com.obimp.data.type.OctaWord;
import com.obimp.data.type.QuadWord;
import com.obimp.data.type.UTF8;
import com.obimp.data.type.UUID;
import com.obimp.data.type.Word;
import com.obimp.packet.Packet;
import com.obimp.packet.PacketListener;
import com.obimp.packet.cli.OBIMP_BEX_CL_CLI_PARAMS;
import com.obimp.packet.cli.OBIMP_BEX_CL_CLI_VERIFY;
import com.obimp.packet.cli.OBIMP_BEX_COM_CLI_HELLO;
import com.obimp.packet.cli.OBIMP_BEX_COM_CLI_LOGIN;
import com.obimp.packet.cli.OBIMP_BEX_FT_CLI_PARAMS;
import com.obimp.packet.cli.OBIMP_BEX_IM_CLI_MESSAGE;
import com.obimp.packet.cli.OBIMP_BEX_IM_CLI_PARAMS;
import com.obimp.packet.cli.OBIMP_BEX_PRES_CLI_ACTIVATE;
import com.obimp.packet.cli.OBIMP_BEX_PRES_CLI_PARAMS;
import com.obimp.packet.cli.OBIMP_BEX_PRES_CLI_REQ_PRES_INFO;
import com.obimp.packet.cli.OBIMP_BEX_PRES_CLI_SET_PRES_INFO;
import com.obimp.packet.cli.OBIMP_BEX_PRES_CLI_SET_STATUS;
import com.obimp.packet.cli.OBIMP_BEX_TP_CLI_PARAMS;
import com.obimp.packet.cli.OBIMP_BEX_UA_CLI_PARAMS;
import com.obimp.packet.cli.OBIMP_BEX_UD_CLI_PARAMS;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Vector;

/**
 * Подключение к OBIMP (Bimoid) серверу
 * @author alex_xpert
 */
public class OBIMPConnection {
    private String server = "";
    private String username = "";
    private String password = "";
    private Socket con;
    private DataInputStream in;
    private DataOutputStream out;
    private PacketListener listener;
    private int seq = 0;
    
    public static boolean connected = false;
    
    public OBIMPConnection(String server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
    }
    
    public void connect() {
        try {
            connected = true;
            con = new Socket(server, 7023);
            in = new DataInputStream(con.getInputStream());
            out = new DataOutputStream(con.getOutputStream());
            listener = new PacketListener(con, in);
            
            try {
                Thread t = new Thread(listener);
                //t.setDaemon(true);
                Packet hello = new OBIMP_BEX_COM_CLI_HELLO();
                hello.append(new wTLD(0x00000001, new UTF8(username)));
                out.write(hello.asByteArray(seq));
                out.flush();
                seq++;
                Vector<Byte> res = new Vector<>();
                int c;
                do {
                    c = in.read();
                    if(c == -1) break;
                    res.add((byte) c);
                } while(in.available() > 0);
                System.out.println("Server response:\n" + res.toString());
                byte[] srv_key = new byte[32];
                int k = 25;
                for(int i=0;i<srv_key.length;i++) {
                    srv_key[i] = res.get(k);
                    k++;
                }
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
                t.start();
                Packet login = new OBIMP_BEX_COM_CLI_LOGIN();
                login.append(new wTLD(0x00000001, new UTF8(username)));
                login.append(new wTLD(0x00000002, new OctaWord(hash)));
                out.write(login.asByteArray(seq));
                out.flush();
                seq++;
//                Packet cl_params = new OBIMP_BEX_CL_CLI_PARAMS(seq);
//                out.write(cl_params.asByteArray());
//                out.flush();
//                seq++;
//                Packet pres_params = new OBIMP_BEX_PRES_CLI_PARAMS(seq);
//                out.write(pres_params.asByteArray());
//                out.flush();
//                seq++;
//                Packet im_params = new OBIMP_BEX_IM_CLI_PARAMS(seq);
//                out.write(im_params.asByteArray());
//                out.flush();
//                seq++;
//                Packet ud_params = new OBIMP_BEX_UD_CLI_PARAMS(seq);
//                out.write(ud_params.asByteArray());
//                out.flush();
//                seq++;
//                Packet ua_params = new OBIMP_BEX_UA_CLI_PARAMS(seq);
//                out.write(ua_params.asByteArray());
//                out.flush();
//                seq++;
//                Packet ft_params = new OBIMP_BEX_FT_CLI_PARAMS(seq);
//                out.write(ft_params.asByteArray());
//                out.flush();
//                seq++;
//                Packet tp_params = new OBIMP_BEX_TP_CLI_PARAMS(seq);
//                out.write(tp_params.asByteArray());
//                out.flush();
//                seq++;
//                Packet req_pres_info = new OBIMP_BEX_PRES_CLI_REQ_PRES_INFO(seq);
//                out.write(req_pres_info.asByteArray());
//                out.flush();
//                seq++;
//                Packet verify = new OBIMP_BEX_CL_CLI_VERIFY(seq);
//                out.write(verify.asByteArray());
//                out.flush();
//                seq++;
                Packet pres_info = new OBIMP_BEX_PRES_CLI_SET_PRES_INFO();
                pres_info.append(new wTLD(0x00000001, new Word(0x0001)));
                pres_info.append(new wTLD(0x00000002, new Word(0x0002)));
                pres_info.append(new wTLD(0x00000003, new UTF8("Java OBIMP Lib (OBIMP4J)")));
                pres_info.append(new wTLD(0x00000004, new QuadWord(0, 1, 0, 0, 0, 2, 0, 2)));
                pres_info.append(new wTLD(0x00000005, new Word(0x0052)));
                out.write(pres_info.asByteArray(seq));
                out.flush();
                seq++;
                Packet set_status = new OBIMP_BEX_PRES_CLI_SET_STATUS();
                set_status.append(new wTLD(0x00000001, new LongWord(0, 0, 0, 0)));
                set_status.append(new wTLD(0x00000002, new UTF8("Работает!")));
                set_status.append(new wTLD(0x00000004, new UTF8("Моя библиотека работает!")));
                String s = "107900013AE347790034340000000033";
                byte[] b = new byte[16];
                b = new byte[] {0x10, 0x79, 0x00, 0x01, 0x3A, (byte) 0xE3, 0x47, 0x79, 0x00, 0x34, 0x34, 0x00, 0x00, 0x00,
                    0x00, (byte) 0x33};
                set_status.append(new wTLD(0x00000005, new UUID(b)));
                out.write(set_status.asByteArray(seq));
                out.flush();
                seq++;
                Packet activate = new OBIMP_BEX_PRES_CLI_ACTIVATE();
                out.write(activate.asByteArray(seq));
                out.flush();
                seq++;
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        } catch(Exception ex) {
            System.out.println("Error:\n");
            ex.printStackTrace();
        }
    }

    public void sendMsg(String id, String text) {
        if(con != null) {
            try {
                OBIMP_BEX_IM_CLI_MESSAGE message = new OBIMP_BEX_IM_CLI_MESSAGE();
                message.append(new wTLD(0x00000001, new UTF8(id)));
                message.append(new wTLD(0x00000002, new LongWord(0, 0, 0, 1)));
                message.append(new wTLD(0x00000003, new LongWord(0, 0, 0, 1)));
                message.append(new wTLD(0x00000004, new BLK(text.getBytes())));
                out.write(message.asByteArray(seq));
                out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    public static byte[] MD5(byte[] b) {
        byte hash[] = null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(b);
            hash = md.digest();
        }catch(Exception ex){
            System.out.println("Error:" + ex);
        }
        return hash;
    }
}
