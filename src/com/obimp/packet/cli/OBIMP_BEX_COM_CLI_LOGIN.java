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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Пакет авторизации
 * @author alex_xpert
 */
public class OBIMP_BEX_COM_CLI_LOGIN extends Packet {
    private final byte type = 0x0001;
    private final byte subtype = 0x0003;
    private byte[] data;
    private final String OBIMPSALT = "OBIMPSALT";

    public OBIMP_BEX_COM_CLI_LOGIN(int seq, String username, String password, byte[] srv_key) throws UnsupportedEncodingException {
        byte[] one = (username + OBIMPSALT + password).getBytes("UTF-8");
        byte[] one_hash = MD5(one);
        byte[] two = new byte[one_hash.length + srv_key.length];
        int k = 0;
        for(int i=0;i<one_hash.length;i++) {
            two[k] = one_hash[i];
            k++;
        }
        for(int i=0;i<srv_key.length;i++) {
            two[k] = srv_key[i];
            k++;
        }
        byte[] hash = MD5(two);

        data = new byte[33 + username.getBytes().length + hash.length];
        data[0] = 0x23;
        data[1] = 0x00;
        data[2] = 0x00;
        data[3] = 0x00;
        data[4] = (byte) seq;
        data[5] = 0x00;
        data[6] = 0x01;
        data[7] = 0x00;
        data[8] = 0x03;
        data[9] = 0x00;
        data[10] = 0x00;
        data[11] = 0x00;
        data[12] = (byte) seq;
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
        data[24] = (byte) (username.getBytes().length);
        k = 25;
        for(int i=0;i<username.getBytes().length;i++) {
            data[k] = username.getBytes()[i];
            k++;
        }
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x02;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = 0x00;
        k++;
        data[k] = (byte) (hash.length);
        k++;
        for(int i=0;i<hash.length;i++) {
            data[k] = hash[i];
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
