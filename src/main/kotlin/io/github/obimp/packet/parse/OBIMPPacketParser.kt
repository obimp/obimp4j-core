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

package io.github.obimp.packet.parse

import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.LongWord
import io.github.obimp.packet.OBIMPPacket
import io.github.obimp.packet.Packet
import io.github.obimp.packet.body.OBIMPBody
import io.github.obimp.packet.header.OBIMPHeader
import java.nio.ByteBuffer

/**
 * Parser of OBIMP packets
 * @author Alexander Krysin
 */
object OBIMPPacketParser : PacketParser<WTLD> {
    /**
     * Parse of packets
     * @param buffer packets bytes
     * @return List of packet
     * @throws IllegalArgumentException if [buffer] contains less than 17 bytes
     */
    override fun parsePackets(buffer: ByteBuffer): List<Packet<WTLD>> {
        if (buffer.remaining() < 17) {
            throw IllegalArgumentException("Packet must have size at least 17 bytes")
        }
        val packets = mutableListOf<OBIMPPacket>()
        while (buffer.hasRemaining()) {
            val header = parseHeader(buffer)
            val body = parseBody(buffer, header.contentLength)
            packets.add(OBIMPPacket(header, body))
        }
        return packets
    }

    /**
     * Parse header of OBIMP packet
     * @param buffer packet or header bytes
     * @return Header of OBIMP packet
     * @throws IllegalArgumentException if [buffer] has incorrect first byte
     */
    private fun parseHeader(buffer: ByteBuffer): OBIMPHeader {
        if (buffer.get() != 0x23.toByte()) {
            throw IllegalArgumentException("Every server/client command must start from byte 0x23 - '#'")
        }
        val sequence = buffer.int
        val type = buffer.short
        val subtype = buffer.short
        val requestID = buffer.int
        val contentLength = buffer.int
        return OBIMPHeader(sequence, type, subtype, requestID, contentLength)
    }

    /**
     * Parse body of OBIMP packet
     * @param buffer packet body bytes
     * @return Body of OBIMP packet
     */
    private fun parseBody(buffer: ByteBuffer, contentLength: Int): OBIMPBody {
        val body = OBIMPBody()
        val targetRemaining = buffer.remaining() - contentLength
        if (buffer.remaining() > targetRemaining) {
            val content = mutableListOf<WTLD>()
            while (buffer.hasRemaining()) {
                val type = buffer.int
                val length = buffer.int
                if (length > 0) {
                    val data = ByteArray(length)
                    buffer[data]
                    content.add(WTLD(LongWord(type), ByteBuffer.wrap(data)))
                } else {
                    content.add(WTLD(LongWord(type)))
                }
            }
        }
        return body
    }
}