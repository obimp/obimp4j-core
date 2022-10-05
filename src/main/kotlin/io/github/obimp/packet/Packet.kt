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

package io.github.obimp.packet

import io.github.obimp.data.structure.DataStructure
import io.github.obimp.packet.body.Body
import io.github.obimp.packet.header.Header
import io.github.obimp.util.BytesSerializable
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */
sealed interface Packet<T: DataStructure<*>> : BytesSerializable {
    var header: Header
    var body: Body<T>

    fun getType() = header.type

    fun getSubtype() = header.subtype

    fun hasItems() = body.content.isNotEmpty()

    fun addItem(item: T) = body.content.add(item)

    fun nextItem() = body.content.removeFirst()

    override fun toBytes(): ByteBuffer {
        val headerBytes = header.toBytes()
        val bodyBytes = body.toBytes()
        val capacity = headerBytes.capacity() + bodyBytes.capacity()
        return ByteBuffer.allocate(capacity).put(headerBytes.array()).put(bodyBytes.array()).rewind() as ByteBuffer
    }
}