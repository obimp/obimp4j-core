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
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Vector;

/**
 *
 */
public class PacketListener implements Runnable {
    private Socket s;
    private DataInputStream in;

    public PacketListener(Socket _s, DataInputStream _in) {
        s = _s;
        in = _in;
    }

    @Override
    public void run() {
        Vector<Byte> data = new Vector<>();
        int k = 0;
        while(OBIMPConnection.connected) {
            try {
                if(k == 13) {
                    int l;
                    byte one = in.readByte(), two = in.readByte(), three = in.readByte(), four = in.readByte();
                    l  = (((int) one) << 24) & 0xFF000000;
                    l |= (((int) two) << 16) & 0x00FF0000;
                    l |= (((int) three) << 8) & 0x0000FF00;
                    l |= (((int) four)) & 0x000000FF;
                    byte[] body = new byte[l];
                    in.readFully(body);
                    data.add(one);
                    data.add(two);
                    data.add(three);
                    data.add(four);
                    for(int i=0;i<body.length;i++) {
                        data.add(body[i]);
                    }
                    byte[] packet = new byte[data.size()];
                    for(int j=0;j<packet.length;j++) {
                        packet[j] = data.get(j);
                    }
                    PacketHandler.parsePacket(packet);
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

}
