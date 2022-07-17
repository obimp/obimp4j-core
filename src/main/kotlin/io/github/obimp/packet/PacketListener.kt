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
import io.github.obimp.toInt
import io.github.obimp.toShort
import java.io.DataInputStream

/**
 * @author Alexander Krysin
 */
class PacketListener(
    private val input: DataInputStream,
    private val connection: OBIMPConnection,
    private val username: String,
    private val password: String,
    private val clientName: String,
    private val clientVersion: String
) : Runnable {
    private val packetHandler = PacketHandler()

    override fun run() {
        var data = mutableListOf<Byte>()
        var k = 0
        while (connection.connected) {
            if (k == 17) {
                data = data.drop(1) as MutableList<Byte>
                val sequence = data.take(4).toInt()
                data = data.drop(4) as MutableList<Byte>
                val type = data.take(2).toShort()
                data = data.drop(2) as MutableList<Byte>
                val subtype = data.take(2).toShort()
                val length = data.takeLast(4).toInt()
                val packet = Packet(type, subtype)
                packet.setSequence(sequence)
                if (length != 0) {
                    val body = ByteArray(length)
                    input.readFully(body)
                    packet.setData(parseData(body))
                }
                packetHandler.parsePacket(packet, connection, username, password, clientName, clientVersion)
                data.clear()
                k = 0
            }
            data += input.read().toByte()
            k++
        }
    }

    private fun parseData(body: ByteArray): MutableList<WTLD> {
        val payload = mutableListOf<WTLD>()
        var data = body
        while (data.isNotEmpty()) {
            val type = data.take(4).toInt()
            data = data.drop(4).toByteArray()
            val length = data.take(4).toInt()
            data = data.drop(4).toByteArray()
            payload.add(WTLD(type, data.take(length).toByteArray()))
            data = data.drop(length).toByteArray()
        }
        return payload
    }
}