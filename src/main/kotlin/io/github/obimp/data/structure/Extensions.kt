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

import io.github.obimp.data.type.*
import io.github.obimp.data.type.Byte
import io.github.obimp.util.Version
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author Alexander Krysin
 */

inline fun <reified T : DataType<*>> DataStructure<*>.readDataType() = when (T::class) {
    Byte::class -> Byte(buffer.get()) as T
    Bool::class -> Bool(buffer.get() == 0x01.toByte()) as T
    Word::class -> Word(buffer.short) as T
    LongWord::class -> LongWord(buffer.int) as T
    QuadWord::class -> QuadWord(buffer.long) as T
    VersionQuadWord::class -> VersionQuadWord(
        Version(
            buffer.short.toInt(), buffer.short.toInt(), buffer.short.toInt(), buffer.short.toInt()
        )
    ) as T

    OctaWord::class -> {
        val bytes = ByteArray(16)
        buffer.get(bytes)
        OctaWord(ByteBuffer.wrap(bytes)) as T
    }

    DateTime::class -> DateTime(LocalDateTime.ofEpochSecond(buffer.long, 0, ZoneOffset.UTC)) as T
    UUID::class -> {
        val bytes = ByteArray(16)
        buffer.get(bytes)
        UUID(bytes.decodeToString()) as T
    }

    BLK::class -> {
        val byteBuffer = ByteBuffer.allocate(buffer.remaining())
        byteBuffer.put(buffer)
        byteBuffer.rewind()
        BLK(byteBuffer) as T
    }

    UTF8::class -> {
        val byteArray = ByteArray(buffer.remaining())
        buffer[byteArray]
        UTF8(byteArray.decodeToString()) as T
    }

    else -> throw IllegalArgumentException("Unsupported DataType")
}

inline fun <reified T : DataType<*>> DataStructure<*>.readDataTypeList(): List<T> {
    val dataTypeList = mutableListOf<T>()
    while (buffer.hasRemaining()) {
        dataTypeList.add(readDataType())
    }
    return dataTypeList
}