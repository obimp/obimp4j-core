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

package com.obimp.data.type;

import com.obimp.data.DataType;

/**
 * UUID - unsigned 16 bytes, Universally Unique Identifier
 * @author alex_xpert
 */
public class UUID extends DataType {
    private int length = 16;
    private byte[] data = new byte[length];
    
    public UUID(int type, byte uuid) {
        switch(type) {
            case 1: // X-Status
                data = new byte[] {0x10, 0x79, 0x00, 0x01, 0x3A, (byte) 0xE3, 0x47, 0x79, 0x00, 0x34, 0x34, 0x00, 0x00, 0x00, 0x00, uuid};
        }
    }

    @Override
    public int getLenght() {
        return this.length;
    }

    @Override
    public byte[] getData() {
        return this.data;
    }
}
