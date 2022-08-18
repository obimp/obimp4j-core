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

import io.github.obimp.*
import io.github.obimp.cl.*
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.*
import io.github.obimp.data.type.Byte
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_DEL_OFFAUTH
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_SRV_BEGIN_UPDATE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_SRV_DONE_OFFAUTH
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_SRV_END_UPDATE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_SRV_ITEM_OPER
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_SRV_PARAMS_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_SRV_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_CL_SRV_VERIFY_REPLY
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_CLI_ACTIVATE
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_CLI_SET_PRES_INFO
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_PRES_CLI_SET_STATUS
import io.github.obimp.util.SystemInfoUtil
import java.time.LocalDateTime

/**
 * @author Alexander Krysin
 */
class ContactListPacketHandler {
    private var activated = false

    fun parsePacket(packet: Packet, connection: OBIMPConnection) {
        when (packet.subtype) {
            OBIMP_BEX_CL_SRV_PARAMS_REPLY -> {}
            OBIMP_BEX_CL_SRV_REPLY -> {
                if (!activated) {
                    val contactList = getContactListItems(packet.getWTLD())

                    for (cll in connection.contactListListeners) {
                        cll.onContactListLoad(contactList)
                    }

                    activated = true

                    val presInfo = Packet(OBIMP_BEX_PRES, OBIMP_BEX_PRES_CLI_SET_PRES_INFO)
                    presInfo.addWTLD(WTLD(0x0001, listOf(Word(0x0001), Word(0x0005), Word(0x0009))))
                    presInfo.addWTLD(WTLD(0x0002, Word(0x0001)))
                    presInfo.addWTLD(WTLD(0x0003, UTF8(Version.NAME)))
                    presInfo.addWTLD(WTLD(0x0004, QuadWord(parseVersion(Version.VERSION))))
                    presInfo.addWTLD(WTLD(0x0005, Word(Status.PRES_STATUS_ONLINE)))
                    presInfo.addWTLD(WTLD(0x0006, UTF8(SystemInfoUtil.getOperatingSystemTitle())))
                    connection.sendPacket(presInfo)

                    val setStatus = Packet(OBIMP_BEX_PRES, OBIMP_BEX_PRES_CLI_SET_STATUS)
                    setStatus.addWTLD(WTLD(0x0001, LongWord(0x0000)))
                    connection.sendPacket(setStatus)

                    connection.sendPacket(Packet(OBIMP_BEX_PRES, OBIMP_BEX_PRES_CLI_ACTIVATE))
                }
            }
            OBIMP_BEX_CL_SRV_VERIFY_REPLY -> {}
            OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY -> {}
            OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY -> {}
            OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY -> {}
            OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST -> {
                val accountName = packet.getWTLD().getDataType<UTF8>().value
                val reason = packet.getWTLD().getDataType<UTF8>().value
                for (cll in connection.contactListListeners) {
                    cll.onAuthRequest(accountName, reason)
                }
            }
            OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY -> {
                val accountName = packet.getWTLD().getDataType<UTF8>().value
                val replyCode = packet.getWTLD().getDataType<Word>().value
                for (cll in connection.contactListListeners) {
                    cll.onAuthReply(accountName, replyCode)
                }
            }
            OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE -> {
                val accountName = packet.getWTLD().getDataType<UTF8>().value
                val reason = packet.getWTLD().getDataType<UTF8>().value
                for (cll in connection.contactListListeners) {
                    cll.onAuthRevoke(accountName, reason)
                }
            }
            OBIMP_BEX_CL_SRV_DONE_OFFAUTH -> {
                connection.sendPacket(Packet(OBIMP_BEX_CL, OBIMP_BEX_CL_CLI_DEL_OFFAUTH))
            }
            OBIMP_BEX_CL_SRV_ITEM_OPER -> {}
            OBIMP_BEX_CL_SRV_BEGIN_UPDATE -> {}
            OBIMP_BEX_CL_SRV_END_UPDATE -> {}
        }
    }

    private fun getContactListItems(contactListData: WTLD): List<ContactListItem> {
        val contactList = mutableListOf<ContactListItem>()
        val contactListItemsCount = contactListData.getDataType<LongWord>().value

        for (i in 0 until contactListItemsCount) {
            val itemType = contactListData.getDataType<Word>().value
            val itemId = contactListData.getDataType<LongWord>().value
            val groupId = contactListData.getDataType<LongWord>().value
            val itemDataLength = contactListData.getDataType<LongWord>().value

            val itemData = WTLD(contactListData.type.toInt(), contactListData.takeBytes(itemDataLength))

            when (itemType) {
                CL_ITEM_TYPE_GROUP -> {
                    val groupName = itemData.getSTLD().getDataType<UTF8>().value
                    contactList.add(Group(itemId, groupId, groupName))
                }
                CL_ITEM_TYPE_CONTACT -> {
                    val accountName = itemData.getSTLD().getDataType<UTF8>().value
                    val contactName = itemData.getSTLD().getDataType<UTF8>().value
                    val privacyType = itemData.getSTLD().getDataType<Byte>().value

                    var authorizationFlag = false
                    var generalItemFlag = false
                    var transportItemId: Int? = null

                    while (itemData.isNotEmpty()) {
                        val stld = itemData.getSTLD()
                        when (stld.type) {
                            0x0005 -> authorizationFlag = true
                            0x0006 -> generalItemFlag = true
                            0x1001 -> transportItemId = stld.getDataType<LongWord>().value
                        }
                    }

                    contactList.add(
                        Contact(
                            itemId,
                            groupId,
                            accountName,
                            contactName,
                            privacyType,
                            authorizationFlag,
                            generalItemFlag,
                            transportItemId
                        )
                    )
                }
                CL_ITEM_TYPE_TRANSPORT -> {
                    val transportUUID = itemData.getSTLD().getDataType<UUID>().value
                    val transportAccountName = itemData.getSTLD().getDataType<UTF8>().value
                    val transportFriendlyName = itemData.getSTLD().getDataType<UTF8>().value

                    contactList.add(
                        Transport(
                            itemId,
                            groupId,
                            transportUUID,
                            transportAccountName,
                            transportFriendlyName
                        )
                    )
                }
                CL_ITEM_TYPE_NOTE -> {
                    val noteName = itemData.getSTLD().getDataType<UTF8>().value
                    val noteType = itemData.getSTLD().getDataType<Byte>().value

                    var noteText: String? = null
                    var noteDate: LocalDateTime? = null
                    var notePicture: String? = null

                    while (itemData.isNotEmpty()) {
                        val stld = itemData.getSTLD()
                        when (stld.type) {
                            0x2003 -> noteText = stld.getDataType<UTF8>().value
                            0x2004 -> noteDate = stld.getDataType<DateTime>().getDateTime()
                            0x2005 -> notePicture = stld.getDataType<OctaWord>().data.decodeToString()
                        }
                    }

                    contactList.add(Note(itemId, groupId, noteName, noteType, noteText, noteDate, notePicture))
                }
            }
        }
        return contactList
    }

    private fun parseVersion(version: String): Long {
        var versions = version.split(".").map(String::toShort)
        while (versions.size < 4) {
            versions = versions + 0
        }
        var bytes = byteArrayOf()
        for (ver in versions) {
            bytes += ver.toBytes()
        }
        return bytes.toLong()
    }

    companion object {
        const val CL_ITEM_TYPE_GROUP = 0x0001
        const val CL_ITEM_TYPE_CONTACT = 0x0002
        const val CL_ITEM_TYPE_TRANSPORT = 0x0003
        const val CL_ITEM_TYPE_NOTE = 0x0004
    }
}