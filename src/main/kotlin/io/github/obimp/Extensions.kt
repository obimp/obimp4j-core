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

package io.github.obimp

/**
 * @author Alexander Krysin
 */

fun Number.toBytes(): ByteArray {
    val length = when (this) {
        is Short -> Short.SIZE_BYTES
        is Int -> Int.SIZE_BYTES
        is Long -> Long.SIZE_BYTES
        else -> Int.SIZE_BYTES
    }
    return ByteArray(length) {
        (toLong() shr (length - 1 - it) * 8).toByte()
    }
}

fun ByteArray.toShort(): Short {
    var short = 0
    for (i in indices) {
        short = short or (get(i).toInt() and 0xFF shl (Short.SIZE_BYTES - 1 - i) * 8)
    }
    return short.toShort()
}

fun ByteArray.toInt(): Int {
    var int = 0
    for (i in indices) {
        int = int or (get(i).toInt() and 0xFF shl (Int.SIZE_BYTES - 1 - i) * 8)
    }
    return int
}

fun ByteArray.toLong(): Long {
    var long: Long = 0
    for (i in indices) {
        long = long or (get(i).toLong() and 0xFF shl (Long.SIZE_BYTES - 1 - i) * 8)
    }
    return long
}

fun List<Byte>.toShort(): Short {
    var short = 0
    for (i in indices) {
        short = short or (get(i).toInt() and 0xFF shl (Short.SIZE_BYTES - 1 - i) * 8)
    }
    return short.toShort()
}

fun List<Byte>.toInt(): Int {
    var int = 0
    for (i in indices) {
        int = int or (get(i).toInt() and 0xFF shl (Int.SIZE_BYTES - 1 - i) * 8)
    }
    return int
}

fun List<Byte>.toLong(): Long {
    var long: Long = 0
    for (i in indices) {
        long = long or (get(i).toLong() and 0xFF shl (Long.SIZE_BYTES - 1 - i) * 8)
    }
    return long
}