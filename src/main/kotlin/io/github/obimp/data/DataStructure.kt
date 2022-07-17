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

package io.github.obimp.data

import io.github.obimp.data.type.*
import io.github.obimp.data.type.Byte
import io.github.obimp.toBytes
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

/**
 * @author Alexander Krysin
 */
abstract class DataStructure(val type: Number, var data: ByteArray) : Data() {
    abstract val length: Number

    override fun toBytes() = type.toBytes() + length.toBytes() + data

    fun isNotEmpty() = data.isNotEmpty()

    inline fun <reified T : DataType> getDataType(): T {
        val bytes = takeBytes(getLength(T::class))
        val constructor =
            T::class.constructors.first { it.parameters.run { size == 1 && get(0).type == ByteArray::class.createType() } }
        return constructor.call(bytes)
    }

    inline fun <reified T : DataType> getDataTypeList(): List<T> {
        val kClass = T::class
        val length = getLength(kClass)
        val dataTypeList = mutableListOf<T>()
        while (data.isNotEmpty()) {
            val bytes = takeBytes(length)
            val constructor =
                kClass.constructors.first { it.parameters.run { size == 1 && get(0).type == ByteArray::class.createType() } }
            dataTypeList.add(constructor.call(bytes))
        }
        return dataTypeList
    }

    fun <T : DataType> getLength(dataTypeClass: KClass<T>) = when (dataTypeClass) {
        Byte::class, Bool::class -> Byte.LENGTH
        Word::class -> Word.LENGTH
        LongWord::class -> LongWord.LENGTH
        QuadWord::class, DateTime::class -> QuadWord.LENGTH
        OctaWord::class, UUID::class -> OctaWord.LENGTH
        else -> data.size
    }

    fun takeBytes(length: Number): ByteArray {
        val bytes = data.take(length.toInt()).toByteArray()
        data = data.drop(length.toInt()).toByteArray()
        return bytes
    }
}
