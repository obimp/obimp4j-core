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

import com.obimp.data.DataStructure;
import com.obimp.data.structure.sTLD;
import com.obimp.data.structure.wTLD;
import java.util.HashMap;

/**
 * Packet
 * @author Alexander Krysin
 */
public class Packet {
    private byte type;
    private byte subtype;
    private HashMap data = new HashMap<Integer, Byte>();
    private int c = 0; 
    
    public Packet(int type, int subtype) {
        this.type = (byte) type;
        this.subtype = (byte) subtype;
        
        data.put(0, 0x23);
        data.put(1, 0x00);
        data.put(2, 0x00);
        data.put(3, 0x00);
        data.put(4, 0x00);
        data.put(5, 0x00);
        data.put(6, this.type);
        data.put(7, 0x00);
        data.put(8, this.subtype);
        data.put(9, 0x00);
        data.put(10, 0x00);
        data.put(11, 0x00);
        data.put(12, 0x00);
        data.put(13, 0x00);
        data.put(14, 0x00);
        data.put(15, 0x00);
        data.put(16, 0x00);
        c = 17;
    }
    
    public int getType() {
        return type;
    }

    public int getSubType() {
        return subtype;
    }

    public byte[] asByteArray(int seq) {     
        byte[] bseq = getBytes(seq);
        data.put(1, bseq[3]);
        data.put(2, bseq[2]);
        data.put(3, bseq[1]);
        data.put(4, bseq[0]);
        data.put(9, bseq[3]);
        data.put(10, bseq[2]);
        data.put(11, bseq[1]);
        data.put(12, bseq[0]);
        byte[] size = getBytes(data.size() - 17);
        data.put(13, size[3]);
        data.put(14, size[2]);
        data.put(15, size[1]);
        data.put(16, size[0]);
        byte[] p = new byte[data.size()];
        for(int i=0;i<p.length;i++) {
            p[i] = Byte.parseByte(String.valueOf(data.get(i)));
        }
        
        return p;
    }
    
    public void append(DataStructure ds) {
        byte[] d = null;
        byte[] l = getBytes(ds.getLength());
        if(ds instanceof wTLD) {
            d = new byte[] {0x00, 0x00, 0x00, (byte) ds.getType(), l[3], l[2], l[1], l[0]};
        } else if(ds instanceof sTLD) {
            d = new byte[] {0x00, (byte) ds.getType(), l[1], l[0]};
        }
        for(int i = 0;i<d.length;i++) {
            data.put(c, d[i]);
            c++;
        }
        for(int i = 0;i<ds.getData().length;i++) {
            data.put(c, ds.getData()[i]);
            c++;
        }
    }
    
    private byte[] getBytes(int x) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < bytes.length; i++) {
           bytes[i] = (byte)(x >> (i * 8));
        }
        return bytes;
    }
}
