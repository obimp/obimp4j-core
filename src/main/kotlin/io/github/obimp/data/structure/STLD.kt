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
import io.github.obimp.data.type.Word
import java.nio.ByteBuffer

/**
 * Short Type Length Data
 * @author Alexander Krysin
 */
class STLD(override val type: Word) : DataStructure<Word> {
    override val length: Word
        get() = Word(if (::buffer.isInitialized) buffer.capacity().toShort() else data.sumOf(Data::size).toShort())
    override var data = mutableListOf<Data>()
    override lateinit var buffer: ByteBuffer

    constructor(type: Word, vararg data: Data) : this(type) {
        this.data.addAll(data)
    }

    override fun size() = Short.SIZE_BYTES * 2 + length.value.toInt()

    override fun toBytes(): ByteBuffer {
        val buffer = ByteBuffer.allocate(Short.SIZE_BYTES * 2 + length.value.toInt())
        buffer.putShort(type.value)
        buffer.putShort(length.value)
        data.forEach { data -> buffer.put(data.toBytes()) }
        return buffer
    }
}