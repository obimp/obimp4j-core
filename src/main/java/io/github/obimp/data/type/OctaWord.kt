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

import io.github.obimp.data.DataType

/**
 * @author Alexander Krysin
 */
class OctaWord(data: ByteArray) : DataType(16, data) {
    constructor(data: Int) : this(convertToByteArray(data))

    companion object {
        fun convertToByteArray(data: Int) : ByteArray {
            val s = data.toString()
            val b = arrayOf(
                s.substring(0, 2), s.substring(2, 4), s.substring(4, 6), s.substring(6, 8),
                s.substring(8, 10), s.substring(10, 12), s.substring(12, 14), s.substring(14, 16),
                s.substring(16, 18), s.substring(18, 20), s.substring(20, 22), s.substring(22, 24),
                s.substring(24, 26), s.substring(26, 28), s.substring(28, 30), s.substring(30)
            )
            val bytes = ByteArray(16)
            for (i in 0..15) {
                bytes[i] = b[i].toByte()
            }
            return bytes
        }
    }
}