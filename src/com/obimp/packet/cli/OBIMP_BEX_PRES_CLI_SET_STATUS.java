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

import com.obimp.packet.Packet;

/**
 * Пакет установки статуса
 * @author alex_xpert
 */
public class OBIMP_BEX_PRES_CLI_SET_STATUS extends Packet {
    private final byte type = 0x0003;
    private final byte subtype = 0x0004;
    private byte[] data;

    public OBIMP_BEX_PRES_CLI_SET_STATUS() {
        data = new byte[89 + "It's work!".getBytes().length + "Java OBIMP Lib (OBIMP4J) is work! ave alex_xpert!".getBytes().length];
        data[0] = 0x23;
        data[1] = 0x00;
        data[2] = 0x00;
        data[3] = 0x00;
        data[4] = 0x04;
        data[5] = 0x00;
        data[6] = 0x03;
        data[7] = 0x00;
        data[8] = 0x04;
        data[9] = 0x00;
        data[10] = 0x00;
        data[11] = 0x00;
        data[12] = 0x04;
        data[13] = 0x00;
        data[14] = 0x00;
        data[15] = 0x00;
        data[16] = (byte) (data.length-17);
        data[17] = 0x00;
        data[18] = 0x00;
        data[19] = 0x00;
        data[20] = 0x01;
        data[21] = 0x00;
        data[22] = 0x00;
        data[23] = 0x00;
        data[24] = (byte) 4;
        data[25] = 0x00;
        data[26] = 0x00;
        data[27] = 0x00;
        data[28] = 0x00;
        data[29] = 0x00;
        data[30] = 0x00;
        data[31] = 0x00;
        data[32] = 0x02;
        data[33] = 0x00;
        data[34] = 0x00;
        data[35] = 0x00;
        data[36] = (byte) "It's work!".getBytes().length;
        int k = 37;
        for(int i=0;i<"It's work!".getBytes().length;i++) {
            data[k] = "It's work!".getBytes()[i];
            k++;
        }
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x04;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = (byte) "Java OBIMP Lib (obimp4j) is work! ave alex_xpert!".getBytes().length;
        k++;
        for(int i=0;i<"Java OBIMP Lib (obimp4j) is work! ave alex_xpert!".getBytes().length;i++) {
            data[k] = "Java OBIMP Lib (obimp4j) is work! ave alex_xpert!".getBytes()[i];
            k++;
        }
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x05;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = (byte) 16;
        k++;
        for(int i=0;i<"10790001-3AE3-4779-0034-340000000019".getBytes().length;i++) {
            data[k] = "10790001-3AE3-4779-0034-340000000019".getBytes()[i];
            k++;
        }
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
    public byte[] asByteArray() {
        return data;
    }
    
}
