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

package io.github.obimp.data.structure

import io.github.obimp.data.Data
import io.github.obimp.data.DataStructure
import io.github.obimp.data.DataType
import io.github.obimp.toInt
import io.github.obimp.toShort

/**
 * Wide Type Length Data
 * @author Alexander Krysin
 */
class WTLD(type: Int, data: ByteArray = byteArrayOf()) : DataStructure(type, data) {
    override val length
        get() = data.size

    constructor(type: Int, data: DataType) : this(type, data.data)

    constructor(type: Int, data: DataStructure) : this(type, data.toBytes())

    constructor(type: Int, data: List<Data>) : this(type, data.map(Data::toBytes).reduce(ByteArray::plus))

    fun addDataType(dataType: DataType) {
        data += dataType.data
    }

    fun addDataStructure(dataStructure: DataStructure) {
        data += dataStructure.toBytes()
    }

    fun getSTLD(): STLD {
        val type = takeShort()
        val length = takeShort()
        val data = takeBytes(length)
        return STLD(type, data)
    }

    fun getWTLD(): WTLD {
        val type = takeInt()
        val length = takeInt()
        val data = takeBytes(length)
        return WTLD(type, data)
    }

    fun getListOfSTLD(): List<STLD> {
        val stldList = mutableListOf<STLD>()
        while (data.isNotEmpty()) {
            stldList.add(getSTLD())
        }
        return stldList
    }

    fun getListOfWTLD(): List<WTLD> {
        val wtldList = mutableListOf<WTLD>()
        while (data.isNotEmpty()) {
            wtldList.add(getWTLD())
        }
        return wtldList
    }

    private fun takeInt(): Int {
        val int = data.take(Int.SIZE_BYTES).toInt()
        data = data.drop(Int.SIZE_BYTES).toByteArray()
        return int
    }

    private fun takeShort(): Short {
        val short = data.take(Short.SIZE_BYTES).toShort()
        data = data.drop(Short.SIZE_BYTES).toByteArray()
        return short
    }
}