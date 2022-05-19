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

package com.obimp.data.structure;

import com.obimp.data.DataStructure;
import com.obimp.data.DataType;

/**
 * sTLD Structure
 * @author Alexander Krysin
 */
public class sTLD extends DataStructure {
    private int type;
    private int length;
    private byte[] data;
    
    public sTLD(int type, DataType dt) {
        this.type = type;
        this.length = dt.getLenght();
        this.data = dt.getData();
    }

    @Override
    public int getType() {
        return this.type;
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
