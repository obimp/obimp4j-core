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
 * Пакет установки информации о клиенте
 * @author alex_xpert
 */
public class OBIMP_BEX_PRES_CLI_SET_PRES_INFO extends Packet {
    private final byte type = 0x0003;
    private final byte subtype = 0x0003;
    private byte[] data;

    public OBIMP_BEX_PRES_CLI_SET_PRES_INFO() {
        data = new byte[71 + "Java OBIMP Lib".getBytes().length];
        data[0] = 0x23;
        data[1] = 0x00;
        data[2] = 0x00;
        data[3] = 0x00;
        data[4] = 0x03;
        data[5] = 0x00;
        data[6] = 0x03;
        data[7] = 0x00;
        data[8] = 0x03;
        data[9] = 0x00;
        data[10] = 0x00;
        data[11] = 0x00;
        data[12] = 0x03;
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
        data[24] = (byte) 2;
        data[25] = 0x00;
        data[26] = 0x01;
        data[27] = 0x00;
        data[28] = 0x00;
        data[29] = 0x00;
        data[30] = 0x02;
        data[31] = 0x00;
        data[32] = 0x00;
        data[33] = 0x00;
        data[34] = (byte) 2;
        data[35] = 0x00;
        data[36] = 0x02;
        data[37] = 0x00;
        data[38] = 0x00;
        data[39] = 0x00;
        data[40] = 0x03;
        data[41] = 0x00;
        data[42] = 0x00;
        data[43] = 0x00;
        data[44] = (byte) "Java OBIMP Lib".getBytes().length;
        int k = 45;
        for(int i=0;i<"Java OBIMP Lib".getBytes().length;i++) {
            data[k] = "Java OBIMP Lib".getBytes()[i];
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
        data[k] = (byte) 8;
        k++;
        data[k] = (byte) 0;
        k++;
        data[k] = (byte) 1;
        k++;
        data[k] = (byte) 0;
        k++;
        data[k] = (byte) 0;
        k++;
        data[k] = (byte) 0;
        k++;
        data[k] = (byte) 0;
        k++;
        data[k] = (byte) 0;
        k++;
        data[k] = (byte) 0;
        k++;
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
        data[k] = (byte) 2;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = (byte) 52;
        k++;
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
