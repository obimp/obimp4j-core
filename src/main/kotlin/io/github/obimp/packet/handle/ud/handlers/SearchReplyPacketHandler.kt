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

package io.github.obimp.packet.handle.ud.handlers

import io.github.obimp.connection.Connection
import io.github.obimp.connection.getListeners
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.structure.readDataType
import io.github.obimp.data.type.Byte
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.data.type.Word
import io.github.obimp.listener.UsersDirectoryListener
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.ud.Gender
import io.github.obimp.ud.SearchReply
import io.github.obimp.ud.SearchResult
import io.github.obimp.ud.SearchResultItem

/**
 * @author Alexander Krysin
 */
class SearchReplyPacketHandler : PacketHandler<WTLD> {
    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        val searchResult = SearchResult.byCode(packet.nextItem().readDataType<Word>().value)
        var searchResultItem: SearchResultItem? = null

        if (searchResult == SearchResult.SUCCESS) {
            searchResultItem = SearchResultItem(searchResult)

            while (packet.hasItems()) {
                val wtld = packet.nextItem()

                when (wtld.getType()) {
                    0x0002 -> searchResultItem.accountName = wtld.readDataType<UTF8>().value
                    0x0003 -> searchResultItem.nickname = wtld.readDataType<UTF8>().value
                    0x0004 -> searchResultItem.firstName = wtld.readDataType<UTF8>().value
                    0x0005 -> searchResultItem.lastName = wtld.readDataType<UTF8>().value
                    0x0006 -> searchResultItem.gender = Gender.byValue(wtld.readDataType<Byte>().value)
                    0x0007 -> searchResultItem.age = wtld.readDataType<Byte>().value.toInt()
                    0x0008 -> searchResultItem.userIsOnline = true
                    0x0009 -> searchResultItem.isLastSearchResult = true
                    0x000A -> searchResultItem.totalCountOfResultsInDB = wtld.readDataType<LongWord>().value
                    0x000B -> searchResultItem.statusPictureFlags = wtld.readDataType<LongWord>().value
                    0x1001 -> searchResultItem.transportItemID = wtld.readDataType<LongWord>().value
                }
            }
        }

        for (ml in connection.getListeners<UsersDirectoryListener>()) {
            ml.onSearch(SearchReply(searchResult, searchResultItem))
        }
    }
}