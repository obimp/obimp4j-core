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
class DateTime(datetime: Long) : DataType(8, convertToByteArray(datetime)) {
    companion object {
        fun convertToByteArray(datetime: Long) : ByteArray {
            val d = StringBuilder(datetime.toString())
            while (d.length < 16) {
                d.insert(0, "0")
            }
            val b = arrayOf(
                d.substring(0, 2), d.substring(2, 4), d.substring(4, 6), d.substring(6, 8),
                d.substring(8, 10), d.substring(10, 12), d.substring(12, 14), d.substring(14)
            )
            val data = ByteArray(8)
            for (i in 0..7) {
                data[i] = b[i].toByte()
            }
            return data
        }
    }
}