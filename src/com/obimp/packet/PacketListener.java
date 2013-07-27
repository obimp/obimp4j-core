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
        int k = -1;
        while(OBIMPConnection.connected) {
            try {
                int i = in.read();
                if(i == -1 && k == -1) continue;
                if(i == -1 || (i == 35 && data.size() > 0)) {
                    byte[] packet = new byte[data.size()];
                    for(int j=0;j<packet.length;j++) {
                        packet[j] = data.get(j);
                    }
                    PacketHandler.parsePacket(packet);
                    data = new Vector<>();
                }
                data.add((byte) i);
                k = i;
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
