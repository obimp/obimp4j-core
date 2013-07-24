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
import com.obimp.packet.cli.OBIMP_BEX_COM_CLI_HELLO;
import com.obimp.packet.cli.OBIMP_BEX_COM_CLI_LOGIN;
import com.obimp.packet.cli.OBIMP_BEX_PRES_CLI_ACTIVATE;
import com.obimp.packet.cli.OBIMP_BEX_PRES_CLI_SET_PRES_INFO;
import com.obimp.packet.cli.OBIMP_BEX_PRES_CLI_SET_STATUS;
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
    
    public OBIMPConnection(String server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
    }
    
    public void connect() {
        try {
            con = new Socket(server, 7023);
            in = new DataInputStream(con.getInputStream());
            out = new DataOutputStream(con.getOutputStream());
            try {
                Packet hello = new OBIMP_BEX_COM_CLI_HELLO(username);
                out.write(hello.asByteArray());
                out.flush();
                Vector<Byte> res = new Vector<>();
                int c;
                do {
                    c = in.read();
                    res.add((byte) c);
                } while(in.available() > 0);
                System.out.println("Server " + server + " response:\n" + res.toString());
                byte[] srv_key = new byte[32];
                int k = 25;
                for(int i=0;i<srv_key.length;i++) {
                    srv_key[i] = res.get(k);
                    k++;
                }
                Packet login = new OBIMP_BEX_COM_CLI_LOGIN(username, password, srv_key);
                out.write(login.asByteArray());
                out.flush();
                res = new Vector<>();
                do {
                    c = in.read();
                    res.add((byte) c);
                } while(in.available() > 0);
                System.out.println("Server " + server + " response:\n" + res.toString());
                Packet pres_info = new OBIMP_BEX_PRES_CLI_SET_PRES_INFO();
                out.write(pres_info.asByteArray());
                out.flush();
                res = new Vector<>();
                do {
                    c = in.read();
                    res.add((byte) c);
                } while(in.available() > 0);
                System.out.println("Server " + server + " response:\n" + res.toString());
                Packet set_status = new OBIMP_BEX_PRES_CLI_SET_STATUS();
                out.write(set_status.asByteArray());
                out.flush();
                res = new Vector<>();
                do {
                    c = in.read();
                    res.add((byte) c);
                } while(in.available() > 0);
                System.out.println("Server " + server + " response:\n" + res.toString());
                Packet activate = new OBIMP_BEX_PRES_CLI_ACTIVATE();
                out.write(activate.asByteArray());
                out.flush();
                res = new Vector<>();
                do {
                    c = in.read();
                    res.add((byte) c);
                } while(in.available() > 0);
                System.out.println("Server " + server + " response:\n" + res.toString());
                Thread.sleep(60000);
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        } catch(Exception ex) {
            System.out.println("Error:\n");
            ex.printStackTrace();
        }
    }
}
