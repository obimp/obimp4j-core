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
import java.time.LocalDateTime
import java.time.ZoneOffset


/**
 * DateTime - signed 8 bytes, 64-bit unix date time
 * @author Alexander Krysin
 */
class DateTime(override var value: LocalDateTime) : DataType<LocalDateTime> {
    override var length = 8

    override fun toBytes(): ByteBuffer {
        val buffer = ByteBuffer.allocate(8)
        buffer.putLong(value.toEpochSecond(ZoneOffset.UTC))
        buffer.rewind()
        return buffer
    }
}