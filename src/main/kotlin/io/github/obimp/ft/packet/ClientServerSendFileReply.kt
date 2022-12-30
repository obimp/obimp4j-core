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

package io.github.obimp.ft.packet

import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.QuadWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.data.type.Word
import io.github.obimp.ft.SendFileReply
import io.github.obimp.packet.OBIMPPacket
import io.github.obimp.packet.header.OBIMPHeader

/**
 * @author Alexander Krysin
 */
class ClientServerSendFileReply(
    sendFileReply: SendFileReply
) : OBIMPPacket(OBIMPHeader(type = OBIMP_BEX_FT, subtype = OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY)) {
    init {
        addItem(WTLD(LongWord(0x0001), UTF8(sendFileReply.accountName)))
        addItem(WTLD(LongWord(0x0002), QuadWord(sendFileReply.uniqueFileTransferID)))
        addItem(WTLD(LongWord(0x0003), Word(sendFileReply.fileTransferReply.code)))
        sendFileReply.receiverClientIP?.let { addItem(WTLD(LongWord(0x0004), UTF8(it))) }
        sendFileReply.receiverClientPortNumber?.let { addItem(WTLD(LongWord(0x0005), LongWord(it))) }
    }
}