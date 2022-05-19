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
 * UTF8 - UTF-8 encoded string, variable length
 * @author alex_xpert
 */
public class UTF8 extends DataType {
    private int length;
    private byte[] data;
    
    public UTF8(String utf8) {
        length = utf8.getBytes().length;
        data = new byte[length];
        for(int i=0;i<data.length;i++) {
            this.data[i] = utf8.getBytes()[i];
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
