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

package io.github.obimp.packet.handle.cl.handlers

import io.github.obimp.cl.*
import io.github.obimp.connection.Connection
import io.github.obimp.connection.getListeners
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.structure.readDataType
import io.github.obimp.data.type.*
import io.github.obimp.data.type.Byte
import io.github.obimp.listener.ContactListListener
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_PRES
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.createEmptyPacket
import io.github.obimp.packet.handle.presence.PresencePacketHandler.Companion.OBIMP_BEX_PRES_CLI_ACTIVATE
import io.github.obimp.packet.handle.presence.PresencePacketHandler.Companion.OBIMP_BEX_PRES_CLI_SET_PRES_INFO
import io.github.obimp.packet.handle.presence.PresencePacketHandler.Companion.OBIMP_BEX_PRES_CLI_SET_STATUS
import io.github.obimp.presence.ClientCapability
import io.github.obimp.presence.ClientType
import io.github.obimp.presence.Language
import io.github.obimp.presence.Status
import io.github.obimp.util.LibVersion
import io.github.obimp.util.SystemInfoUtil
import java.nio.ByteBuffer
import java.time.LocalDateTime

/**
 * @author Alexander Krysin
 */
class ReplyPacketHandler : PacketHandler<WTLD> {
    private var activated = false

    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        if (!activated) {
            val contactList = getContactListItems(packet.nextItem())

            for (cll in connection.getListeners<ContactListListener>()) {
                cll.onContactListLoaded(contactList)
            }

            activated = true

            val presInfo = createEmptyPacket(OBIMP_BEX_PRES, OBIMP_BEX_PRES_CLI_SET_PRES_INFO)
            presInfo.body.content.add(
                WTLD(LongWord(0x0001)).apply {
                    data.addAll(
                        listOf(
                            Word(ClientCapability.MSGS_UTF8.capability),
                            Word(ClientCapability.MSGS_RTF.capability),
                            Word(ClientCapability.MSGS_HTML.capability),
                            Word(ClientCapability.MSGS_ENCRYPT.capability),
                            Word(ClientCapability.NOTIFS_TYPING.capability),
                            Word(ClientCapability.AVATARS.capability),
                            Word(ClientCapability.FILE_TRANSFER.capability),
                            Word(ClientCapability.TRANSPORTS.capability),
                            Word(ClientCapability.NOTIFS_ALARM.capability),
                            Word(ClientCapability.NOTIFS_MAIL.capability)
                        )
                    )
                }
            )
            presInfo.body.content.add(WTLD(LongWord(0x0002)).apply { data.add(Word(ClientType.USER.value)) })
            presInfo.body.content.add(WTLD(LongWord(0x0003)).apply { data.add(UTF8(LibVersion.NAME)) })
            presInfo.body.content.add(WTLD(LongWord(0x0004)).apply { data.add(VersionQuadWord(LibVersion.VERSION)) })
            presInfo.body.content.add(WTLD(LongWord(0x0005)).apply { data.add(Word(Language.RUSSIAN.code)) })
            presInfo.body.content.add(WTLD(LongWord(0x0006)).apply { data.add(UTF8(SystemInfoUtil.getOperatingSystemTitle())) })
            connection.connectionListener.sendPacket(presInfo)

            val setStatus = createEmptyPacket(OBIMP_BEX_PRES, OBIMP_BEX_PRES_CLI_SET_STATUS)
            setStatus.body.content.add(WTLD(LongWord(0x0001)).apply { data.add(LongWord(Status.FREE_FOR_CHAT.value)) })
            connection.connectionListener.sendPacket(setStatus)

            connection.connectionListener.sendPacket(createEmptyPacket(OBIMP_BEX_PRES, OBIMP_BEX_PRES_CLI_ACTIVATE))
        }
    }

    private fun getContactListItems(contactListData: WTLD): List<ContactListItem> {
        val contactList = mutableListOf<ContactListItem>()
        val contactListItemsCount = contactListData.readDataType<LongWord>().value
        val contactListItemsData = contactListData.readDataType<BLK>().value

        for (i in 0 until contactListItemsCount) {
            val itemType = ContactListItemType.byType(contactListItemsData.short)
            val itemId = contactListItemsData.int
            val groupId = contactListItemsData.int
            val itemDataLength = contactListItemsData.int

            val itemData = WTLD(contactListData.type).apply {
                val itemData = ByteArray(itemDataLength)
                contactListItemsData.get(itemData)
                this.buffer = ByteBuffer.wrap(itemData)
            }

            when (itemType) {
                ContactListItemType.GROUP -> {
                    val groupName = itemData.readSTLD().readDataType<UTF8>().value
                    contactList.add(Group(itemId, groupId, groupName))
                }

                ContactListItemType.CONTACT -> {
                    val accountName = itemData.readSTLD().readDataType<UTF8>().value
                    val contactName = itemData.readSTLD().readDataType<UTF8>().value
                    val privacyType = PrivacyType.byValue(itemData.readSTLD().readDataType<Byte>().value)

                    var authorizationFlag = false
                    var generalItemFlag = false
                    var transportItemId: Int? = null

                    while (itemData.hasItems()) {
                        val stld = itemData.readSTLD()
                        when (stld.getType()) {
                            0x0005 -> authorizationFlag = true
                            0x0006 -> generalItemFlag = true
                            0x1001 -> transportItemId = stld.readDataType<LongWord>().value
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

                ContactListItemType.TRANSPORT -> {
                    val transportUUID = itemData.readSTLD().readDataType<UUID>().value
                    val transportAccountName = itemData.readSTLD().readDataType<UTF8>().value
                    val transportFriendlyName = itemData.readSTLD().readDataType<UTF8>().value

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

                ContactListItemType.NOTE -> {
                    val noteName = itemData.readSTLD().readDataType<UTF8>().value
                    val noteType = NoteType.byValue(itemData.readSTLD().readDataType<Byte>().value)

                    var noteText: String? = null
                    var noteDate: LocalDateTime? = null
                    var notePicture: ByteArray? = null

                    while (itemData.hasItems()) {
                        val stld = itemData.readSTLD()
                        when (stld.getType()) {
                            0x2003 -> noteText = stld.readDataType<UTF8>().value
                            0x2004 -> noteDate = stld.readDataType<DateTime>().value
                            0x2005 -> notePicture = stld.readDataType<OctaWord>().value.array()
                        }
                    }

                    contactList.add(Note(itemId, groupId, noteName, noteType, noteText, noteDate, notePicture))
                }
            }
        }

        return contactList
    }
}