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

package io.github.obimp.data.type

import java.nio.ByteBuffer

/**
 * Word - unsigned 2 bytes
 * @author Alexander Krysin
 */
class Word(override var value: Short) : DataType<Short> {
    override var length = Short.SIZE_BYTES

    override fun toBytes(): ByteBuffer {
        val buffer = ByteBuffer.allocate(length)
        buffer.putShort(value)
        buffer.rewind()
        return buffer
    }
}