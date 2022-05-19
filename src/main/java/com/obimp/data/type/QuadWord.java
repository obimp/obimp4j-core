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

package com.obimp.data.type;

import com.obimp.data.DataType;

/**
 * QuadWord – unsigned 8 bytes
 * @author Alexander Krysin
 */
public class QuadWord extends DataType {
    private int length = 8;
    private byte[] data = new byte[length];
    
    public QuadWord(int one, int two, int three, int four, int five, int six, int seven, int eight) {
        int[] _data = {one, two, three, four, five, six, seven, eight};
        for(int i=0;i<length;i++) {
            this.data[i] = (byte) _data[i];
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
