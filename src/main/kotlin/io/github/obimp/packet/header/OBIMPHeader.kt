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

package io.github.obimp.packet.header

import java.nio.ByteBuffer

/**
 * Header of OBIMP packet
 * @author Alexander Krysin
 */
class OBIMPHeader(
    override var sequence: Int = 0,
    override val type: Short,
    override val subtype: Short,
    override var requestID: Int = 0,
    override var contentLength: Int = 0
) : Header {
    override fun toBytes(): ByteBuffer {
        val buffer = ByteBuffer.allocate(HEADER_LENGTH)
        buffer.put(CHECK)
        buffer.putInt(sequence)
        buffer.putShort(type)
        buffer.putShort(subtype)
        buffer.putInt(requestID)
        buffer.putInt(contentLength)
        return buffer
    }

    companion object {
        private const val HEADER_LENGTH = 17
        private const val CHECK: Byte = 0x23
    }
}