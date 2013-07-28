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

package com.obimp.packet.cli;

import com.obimp.data.DataStructure;
import com.obimp.data.structure.sTLD;
import com.obimp.data.structure.wTLD;
import com.obimp.packet.Packet;
import java.util.HashMap;

/**
 * Запрос параметров Users directory 
 * @author alex_xpert
 */
public class OBIMP_BEX_UD_CLI_PARAMS extends Packet {
    private final byte type = 0x0005;
    private final byte subtype = 0x0001;
    private HashMap data = new HashMap<Integer, Byte>();
    private int c = 0;

    public OBIMP_BEX_UD_CLI_PARAMS() {
        data.put(0, 0x23);
        data.put(1, 0x00);
        data.put(2, 0x00);
        data.put(3, 0x00);
        data.put(4, 0x00);
        data.put(5, 0x00);
        data.put(6, 0x05);
        data.put(7, 0x00);
        data.put(8, 0x01);
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
    
    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getSubType() {
        return subtype;
    }

    @Override
    public byte[] asByteArray(int seq) {
        data.put(4, seq);
        data.put(12, seq);
        byte[] p = new byte[data.size()];
        for(int i=0;i<p.length;i++) {
            p[i] = Byte.valueOf(String.valueOf(data.get(i)));
        }
        return p;
    }
    
    @Override
    public void append(DataStructure ds) {
        byte[] d = null;
        if(ds instanceof wTLD) {
            d = new byte[] {0x00, 0x00, 0x00, (byte) ds.getType(), 0x00, 0x00, 0x00, (byte) ds.getLength()};
        } else if(ds instanceof sTLD) {
            d = new byte[] {0x00, (byte) ds.getType(), 0x00, (byte) ds.getLength()};
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
    
}
