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

import io.github.obimp.data.DataStructure
import io.github.obimp.data.DataType
import io.github.obimp.toBytes

/**
 * Short Type Length Data
 * @author Alexander Krysin
 */
class STLD(type: Short, data: ByteArray = byteArrayOf()) : DataStructure(type, data) {
    override val length
        get() = data.size.toShort()

    constructor(type: Short, data: DataType) : this(type, data.data)

    constructor(type: Short, data: List<DataType>) : this(type, data.map(DataType::data).reduce(ByteArray::plus))
}