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

import io.github.obimp.OBIMPConnection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.BLK
import java.io.DataInputStream
import java.net.Socket
import java.util.*

/**
 * @author Alexander Krysin
 */
class PacketListener(
    private val s: Socket,
    private val dis: DataInputStream,
    private val oc: OBIMPConnection,
    private val username: String,
    private val password: String
) : Runnable {

    override fun run() {
        var data = Vector<Byte>()
        var k = 0
        while (oc.connected) {
            try {
                if (k == 17) {
                    val type: Int = getTypeOrSubtype(data[5], data[6])
                    val subtype: Int = getTypeOrSubtype(data[7], data[8])
                    val l: Int = getLength(data[13], data[14], data[15], data[16])
                    val body = if (l > 0) ByteArray(l) else null
                    if (body != null) {
                        dis.readFully(body)
                        val tlds: HashMap<Int, BLK> = parseData(body)
                        val p = Packet(type, subtype)
                        for (i in 0 until tlds.size) {
                            p.append(WTLD(tlds.keys.toTypedArray()[i], (tlds.values.toTypedArray()[i] as BLK)!!))
                        }
                        PacketHandler.parsePacket(p, tlds, oc, username, password, s)
                    }
                    data = Vector()
                    k = 0
                }
                val i: Int = dis.read()
                data.add(i.toByte())
                k++
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun getTypeOrSubtype(one: Byte, two: Byte): Int {
        var l: Int = one.toInt() shl 8 and 0x0000FF00
        l = l or (two.toInt() and 0x000000FF)
        return l
    }

    private fun parseData(data: ByteArray): HashMap<Int, BLK> {
        val tlds: HashMap<Int, BLK> = hashMapOf()
        var data = data
        while (data.isNotEmpty()) {
            val type: Int = getLength(data[0], data[1], data[2], data[3])
            val length: Int = getLength(data[4], data[5], data[6], data[7])
            var body = ByteArray(length)
            var k = 8
            for (i in 0 until length) {
                body[i] = data[k]
                k++
            }
            tlds[type] = BLK(body)
            body = ByteArray(data.size - (length + 8))
            k = length + 8
            for (i in body.indices) {
                body[i] = data[k]
                k++
            }
            data = body
        }
        return tlds
    }

    companion object {
        @JvmStatic
        fun getLength(one: Byte, two: Byte, three: Byte, four: Byte): Int {
            var l: Int = one.toInt() shl 24 and -0x1000000
            l = l or (two.toInt() shl 16 and 0x00FF0000)
            l = l or (three.toInt() shl 8 and 0x0000FF00)
            l = l or (four.toInt() and 0x000000FF)
            return l
        }
    }
}