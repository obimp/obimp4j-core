/*
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
import java.io.DataInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 */
public class PacketListener implements Runnable {
    private Socket s;
    private DataInputStream in;
    private OBIMPConnection oc;
    private String username;
    private String password;

    public PacketListener(Socket s, DataInputStream in, OBIMPConnection oc, String _username, String _password) {
        this.s = s;
        this.in = in;
        this.oc = oc;
        this.username = _username;
        this.password = _password;
    }
    
    @Override
    public void run() {
        Vector<Byte> data = new Vector<>();
        int k = 0;
        while(oc.connected) {
            try {
                if(k == 17) {
                    int type = getTypeOrSubtype(data.get(5), data.get(6));
                    int subtype = getTypeOrSubtype(data.get(7), data.get(8));
                    int l = getLength(data.get(13), data.get(14), data.get(15), data.get(16));
                    byte[] body = l > 0 ? new byte[l] : null;
                    if(body != null)  {
                        in.readFully(body);
                        HashMap<Integer, BLK> tlds = parseData(body);
                        Packet p = new Packet(type, subtype);
                        for(int i=0;i<tlds.size();i++) {
                            p.append(new wTLD((int) tlds.keySet().toArray()[i], (BLK) tlds.values().toArray()[i]));
                        }
                        PacketHandler.parsePacket(p, tlds, oc, username, password, s);
                    }
                    data = new Vector<>();
                    k = 0;
                }
                int i = in.read();
                data.add((byte) i);
                k++;
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static int getLength(byte one, byte two, byte three, byte four) {
        int l;
        l  = (((int) one) << 24) & 0xFF000000;
        l |= (((int) two) << 16) & 0x00FF0000;
        l |= (((int) three) << 8) & 0x0000FF00;
        l |= (((int) four)) & 0x000000FF;
        return l;
    }
    
    private int getTypeOrSubtype(byte one, byte two) {
        int l;
        l = (((int) one) << 8) & 0x0000FF00;
        l |= (((int) two)) & 0x000000FF;
        return l;
    }
    
    private HashMap<Integer, BLK> parseData(byte[] data) {
        HashMap tlds = new HashMap<Integer, BLK>();
        while(data.length > 0) {
            int type = getLength(data[0], data[1], data[2], data[3]);
            int length = getLength(data[4], data[5], data[6], data[7]);
            byte[] body = new byte[length];
            int k = 8;
            for(int i=0;i<length;i++) {
                body[i] = data[k];
                k++;
            }
            tlds.put(type, new BLK(body));
            body = new byte[data.length - (length + 8)];
            k = length + 8;
            for(int i=0;i<body.length;i++) {
                body[i] = data[k];
                k++;
            }
            data = body;
        }
        return tlds;
    }
    
}
