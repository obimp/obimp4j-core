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

import io.github.obimp.data.DataStructure
import io.github.obimp.data.structure.STLD
import io.github.obimp.data.structure.WTLD

/**
 * @author Alexander Krysin
 */
class Packet(type: Int, subtype: Int) {
    private val type: Byte = type.toByte()
    private val subtype: Byte = subtype.toByte()
    private val data = hashMapOf<Int, Byte>()
    private var c = 0

    init {
        data[0] = 0x23
        data[1] = 0x00
        data[2] = 0x00
        data[3] = 0x00
        data[4] = 0x00
        data[5] = 0x00
        data[6] = this.type
        data[7] = 0x00
        data[8] = this.subtype
        data[9] = 0x00
        data[10] = 0x00
        data[11] = 0x00
        data[12] = 0x00
        data[13] = 0x00
        data[14] = 0x00
        data[15] = 0x00
        data[16] = 0x00
        c = 17
    }

    fun getType() = type.toInt()

    fun getSubtype() = subtype.toInt()

    fun asByteArray(seq: Int): ByteArray {
        val bseq = getBytes(seq)
        data[1] = bseq[3]
        data[2] = bseq[2]
        data[3] = bseq[1]
        data[4] = bseq[0]
        data[9] = bseq[3]
        data[10] = bseq[2]
        data[11] = bseq[1]
        data[12] = bseq[0]
        val size = getBytes(data.size - 17)
        data[13] = size[3]
        data[14] = size[2]
        data[15] = size[1]
        data[16] = size[0]
        val p = ByteArray(data.size)
        for (i in p.indices) {
            p[i] = data[i].toString().toByte()
        }
        return p
    }

    fun append(ds: DataStructure) {
        var d: ByteArray? = null
        val l = getBytes(ds.length)
        if (ds is WTLD) {
            d = byteArrayOf(0x00, 0x00, 0x00, ds.type.toByte(), l[3], l[2], l[1], l[0])
        } else if (ds is STLD) {
            d = byteArrayOf(0x00, ds.type.toByte(), l[1], l[0])
        }
        for (i in d!!.indices) {
            data[c] = d[i]
            c++
        }
        for (i in ds.data.indices) {
            data[c] = ds.data[i]
            c++
        }
    }

    fun getBytes(x: Int): ByteArray {
        val bytes = ByteArray(4)
        for (i in bytes.indices) {
            bytes[i] = (x shr i * 8).toByte()
        }
        return bytes
    }
}