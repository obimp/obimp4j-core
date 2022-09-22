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

import io.github.obimp.util.Version
import java.nio.ByteBuffer

/** QuadWord - unsigned 8 bytes, cast to [Version] object
 * @author Alexander Krysin
 */
class VersionQuadWord(override var value: Version) : DataType<Version> {
    override var length = Long.SIZE_BYTES

    override fun toBytes(): ByteBuffer {
        val buffer = ByteBuffer.allocate(length)
        buffer.putShort(value.major.toShort())
        buffer.putShort(value.minor.toShort())
        buffer.putShort(value.release.toShort())
        buffer.putShort(value.build.toShort())
        buffer.rewind()
        return buffer
    }
}