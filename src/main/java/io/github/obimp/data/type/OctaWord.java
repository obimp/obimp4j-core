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

package io.github.obimp.data.type;

import io.github.obimp.data.DataType;

/**
 * OctaWord – unsigned 16 bytes
 * @author Alexander Krysin
 */
public class OctaWord extends DataType {
    private int length = 16;
    private byte[] data = new byte[length];

    public OctaWord(int _data) {
        String d = String.valueOf(_data).replace("0x", "");
        String[] b = {d.substring(0, 2), d.substring(2, 4), d.substring(4, 6), d.substring(6, 8),
            d.substring(8, 10), d.substring(10, 12), d.substring(12, 14), d.substring(14, 16),
            d.substring(16, 18), d.substring(18, 20), d.substring(20, 22), d.substring(22, 24),
            d.substring(24, 26), d.substring(26, 28), d.substring(28, 30), d.substring(30)};
        for(int i=0;i<length;i++) {
            this.data[i] = java.lang.Byte.valueOf(b[i]);
        }
    }
    
    public OctaWord(byte[] _data) {
        this.data = _data;
    }
    
    @Override
    public int getLength() {
        return this.length;
    }

    @Override
    public byte[] getData() {
        return this.data;
    }

}
