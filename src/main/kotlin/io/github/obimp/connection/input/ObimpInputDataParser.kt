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

package io.github.obimp.connection.input

import io.github.obimp.connection.Connection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.LongWord
import io.github.obimp.packet.ObimpPacket
import io.github.obimp.packet.handle.ObimpPacketHandler
import io.github.obimp.packet.header.Header
import io.github.obimp.packet.header.ObimpHeader
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */
object ObimpInputDataParser : InputDataParser<WTLD> {
    private val packetHandler = ObimpPacketHandler()
    private val inputBuffer = mutableMapOf<Connection<WTLD>, Pair<ByteBuffer, Header?>>()

    override fun parseInputData(connection: Connection<WTLD>, buffer: ByteBuffer) {
        buffer.rewind()
        inputBuffer[connection]?.let { pair ->
            val (currentBuffer, header) = pair
            header?.let {
                val neededBytesCount = it.contentLength - currentBuffer.capacity()
                if (buffer.capacity() >= neededBytesCount) {
                    val bodyBytes = ByteArray(neededBytesCount)
                    buffer[bodyBytes]
                    parseBody(connection, it, ByteBuffer.wrap(currentBuffer.array() + bodyBytes))
                    inputBuffer.remove(connection)
                    if (buffer.hasRemaining()) {
                        parseInputData(connection, ByteBuffer.allocate(buffer.remaining()).put(buffer))
                    }
                } else {
                    val actualBuffer = ByteBuffer.allocate(currentBuffer.capacity() + buffer.capacity())
                    currentBuffer.rewind()
                    actualBuffer.put(currentBuffer)
                    actualBuffer.put(buffer)
                    inputBuffer.replace(connection, Pair(actualBuffer, it))
                }
                return
            }
            val neededForHeaderBytesCount = 17 - currentBuffer.capacity()
            if (buffer.capacity() >= neededForHeaderBytesCount) {
                val neededForHeaderBytes = ByteArray(neededForHeaderBytesCount)
                buffer[neededForHeaderBytes]
                val headerBytes = ByteBuffer.wrap(currentBuffer.array() + neededForHeaderBytes)
                val actualHeader = parseHeader(headerBytes)
                if (buffer.remaining() >= actualHeader.contentLength) {
                    val bodyBytes = ByteArray(actualHeader.contentLength)
                    buffer[bodyBytes]
                    parseBody(connection, actualHeader, ByteBuffer.wrap(bodyBytes))
                    inputBuffer.remove(connection)
                    if (buffer.hasRemaining()) {
                        parseInputData(connection, ByteBuffer.allocate(buffer.remaining()).put(buffer))
                    }
                } else {
                    inputBuffer.replace(connection, Pair(ByteBuffer.allocate(buffer.remaining()).put(buffer), actualHeader))
                }
            } else {
                inputBuffer.replace(connection, Pair(ByteBuffer.wrap(currentBuffer.array() + buffer.array()), null))
            }
            return
        }
        if (buffer.capacity() >= 17) {
            val headerBytes = ByteArray(17)
            buffer[headerBytes]
            val header = parseHeader(ByteBuffer.wrap(headerBytes))
            if (buffer.remaining() >= header.contentLength) {
                val bodyBytes = ByteArray(header.contentLength)
                buffer[bodyBytes]
                parseBody(connection, header, ByteBuffer.wrap(bodyBytes))
                if (buffer.hasRemaining()) {
                    parseInputData(connection, ByteBuffer.allocate(buffer.remaining()).put(buffer))
                }
            } else {
                val actualBuffer = ByteBuffer.allocate(buffer.remaining()).put(buffer)
                actualBuffer.rewind()
                inputBuffer[connection] = Pair(actualBuffer, header)
            }
        } else {
            inputBuffer[connection] = Pair(ByteBuffer.wrap(buffer.array()), null)
        }
    }

    private fun parseHeader(buffer: ByteBuffer): Header {
        buffer.get() // Skipping check byte (0x23 - "#")
        val sequence = buffer.int
        val type = buffer.short
        val subtype = buffer.short
        val requestID = buffer.int
        val contentLength = buffer.int
        return ObimpHeader(sequence, type, subtype, requestID, contentLength)
    }

    private fun parseBody(connection: Connection<WTLD>, header: Header, buffer: ByteBuffer) {
        val packet = ObimpPacket()
        packet.header = header
        if (header.contentLength > 0) {
            while (buffer.hasRemaining()) {
                val type = buffer.int
                val length = buffer.int
                val wtld = WTLD(LongWord(type))
                if (length > 0) {
                    val data = ByteArray(length)
                    buffer[data]
                    wtld.buffer = ByteBuffer.wrap(data)
                }
                packet.body.content.add(wtld)
            }
        }
        packetHandler.handlePacket(connection, packet)
    }
}