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
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.Word
import java.nio.ByteBuffer

/**
 * Wide Type Length Data
 * @author Alexander Krysin
 */
class WTLD(override val type: LongWord) : DataStructure<LongWord> {
    override val length: LongWord
        get() = LongWord(if (::buffer.isInitialized) buffer.capacity() else data.sumOf(Data::size))
    override var data = mutableListOf<Data>()
    override lateinit var buffer: ByteBuffer

    constructor(type: LongWord, vararg data: Data) : this(type) {
        this.data.addAll(data)
    }

    override fun size() = Int.SIZE_BYTES * 2 + length.value

    override fun toBytes(): ByteBuffer {
        val buffer = ByteBuffer.allocate(Int.SIZE_BYTES * 2 + length.value)
        buffer.putInt(type.value)
        buffer.putInt(length.value)
        data.forEach { data -> buffer.put(data.toBytes()) }
        buffer.rewind()
        return buffer
    }

    fun readWTLD(): WTLD {
        val type = buffer.int
        val length = buffer.int
        val data = ByteArray(length)
        buffer.get(data)
        val wtld = WTLD(LongWord(type))
        wtld.buffer = ByteBuffer.wrap(data)
        return wtld
    }

    fun readSTLD(): STLD {
        val type = buffer.short
        val length = buffer.short
        val data = ByteArray(length.toInt())
        buffer.get(data)
        val stld = STLD(Word(type))
        stld.buffer = ByteBuffer.wrap(data)
        return stld
    }

    fun readWTLDList() : List<WTLD> {
        val wtldSList= mutableListOf<WTLD>()
        while (buffer.hasRemaining()) {
            wtldSList.add(readWTLD())
        }
        return wtldSList
    }

    fun readSTLDList() : List<STLD> {
        val stldSList= mutableListOf<STLD>()
        while (buffer.hasRemaining()) {
            stldSList.add(readSTLD())
        }
        return stldSList
    }
}