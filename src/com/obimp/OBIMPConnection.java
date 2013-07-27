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
                Packet hello = new OBIMP_BEX_COM_CLI_HELLO(seq, username);
                out.write(hello.asByteArray());
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
                t.start();
                Packet login = new OBIMP_BEX_COM_CLI_LOGIN(seq, username, password, srv_key);
                out.write(login.asByteArray());
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
                Packet pres_info = new OBIMP_BEX_PRES_CLI_SET_PRES_INFO(seq);
                out.write(pres_info.asByteArray());
                out.flush();
                seq++;
                Packet set_status = new OBIMP_BEX_PRES_CLI_SET_STATUS(seq);
                out.write(set_status.asByteArray());
                out.flush();
                seq++;
                Packet activate = new OBIMP_BEX_PRES_CLI_ACTIVATE(seq);
                out.write(activate.asByteArray());
                out.flush();
                seq++;
                Thread.sleep(10000);
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
                out.write(new OBIMP_BEX_IM_CLI_MESSAGE(seq, id, text).asByteArray());
                out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
}
