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

package io.github.obimp.packet.body

import io.github.obimp.data.Data
import io.github.obimp.data.structure.WTLD
import java.nio.ByteBuffer

/**
 * Body of OBIMP packet
 * @author Alexander Krysin
 */
class OBIMPBody : Body<WTLD> {
    private val content = mutableListOf<WTLD>()

    override fun getLength() = content.sumOf(Data::size)

    override fun hasItems() = content.isNotEmpty()

    override fun addItem(item: WTLD) {
        content.add(item)
    }

    override fun nextItem() = content.removeFirst()

    override fun toBytes(): ByteBuffer {
        val buffer = ByteBuffer.allocate(getLength())
        content.forEach { data -> buffer.put(data.toBytes().array()) }
        buffer.rewind()
        return buffer
    }

}