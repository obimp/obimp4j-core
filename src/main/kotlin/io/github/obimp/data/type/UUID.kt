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
 * UUID - unsigned 16 bytes, Universally Unique Identifier
 * @author Alexander Krysin
 */
class UUID(override var value: String) : DataType<String> {
    override var length = 16

    override fun toBytes(): ByteBuffer = ByteBuffer.wrap(value.encodeToByteArray())
}