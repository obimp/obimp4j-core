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

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Alexander Krysin
 */
internal class ExtensionsTest {
    @Test
    fun convertNumbersToByteArrayIsCorrect() {
        assertArrayEquals(byteArrayOf(0x00, 0x00, 0x00, 0x0a), 10.toBytes())
        assertArrayEquals(byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xf6.toByte()), (-10).toBytes())
        assertArrayEquals(byteArrayOf(0x00, 0x00, 0x00, 0x00), 0.toBytes())
        assertArrayEquals(byteArrayOf(0x80.toByte(), 0x00, 0x00, 0x00), Int.MIN_VALUE.toBytes())
        assertArrayEquals(byteArrayOf(0x7f, 0xff.toByte(), 0xff.toByte(), 0xff.toByte()), Int.MAX_VALUE.toBytes())
        assertArrayEquals(byteArrayOf(0x80.toByte(), 0x00), Short.MIN_VALUE.toBytes())
        assertArrayEquals(byteArrayOf(0x7f.toByte(), 0xff.toByte()), Short.MAX_VALUE.toBytes())
        assertArrayEquals(
            byteArrayOf(0x80.toByte(), 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
            Long.MIN_VALUE.toBytes()
        )
        assertArrayEquals(
            byteArrayOf(
                0x7f,
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte()
            ),
            Long.MAX_VALUE.toBytes()
        )
    }

    @Test
    fun convertByteArrayToShortIsCorrect() {
        assertEquals(10, byteArrayOf(0x00, 0x0A).toShort())
        assertEquals(-10, byteArrayOf(0xff.toByte(), 0xf6.toByte()).toShort())
        assertEquals(0, byteArrayOf(0x00, 0x00).toShort())
        assertEquals(Short.MIN_VALUE, byteArrayOf(0x80.toByte(), 0x00).toShort())
        assertEquals(Short.MAX_VALUE, byteArrayOf(0x7f, 0xff.toByte()).toShort())
    }

    @Test
    fun convertByteArrayToIntIsCorrect() {
        assertEquals(10, byteArrayOf(0x00, 0x00, 0x00, 0x0A).toInt())
        assertEquals(-10, byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xf6.toByte()).toInt())
        assertEquals(0, byteArrayOf(0x00, 0x00, 0x00, 0x00).toInt())
        assertEquals(Int.MIN_VALUE, byteArrayOf(0x80.toByte(), 0x00, 0x00, 0x00).toInt())
        assertEquals(Int.MAX_VALUE, byteArrayOf(0x7f, 0xff.toByte(), 0xff.toByte(), 0xff.toByte()).toInt())
    }

    @Test
    fun convertByteArrayToLongIsCorrect() {
        assertEquals(10, byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A).toLong())
        assertEquals(
            -10,
            byteArrayOf(
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xf6.toByte()
            ).toLong()
        )
        assertEquals(0, byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00).toLong())
        assertEquals(Long.MIN_VALUE, byteArrayOf(0x80.toByte(), 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00).toLong())
        assertEquals(
            Long.MAX_VALUE,
            byteArrayOf(
                0x7f,
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte(),
                0xff.toByte()
            ).toLong()
        )
    }
}