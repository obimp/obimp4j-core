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

package io.github.obimp

import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.UTF8
import io.github.obimp.listener.*
import io.github.obimp.packet.Packet
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM_CLI_HELLO
import io.github.obimp.packet.PacketListener
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

/**
 * OBIMP Connection
 * @author Alexander Krysin
 */
class OBIMPConnection(
    private val server: String = "",
    private val username: String = "",
    private val password: String = "",
    private val clientName: String = Version.NAME,
    private val clientVersion: String = Version.VERSION
) {
    private lateinit var connection: Socket

    private lateinit var input: DataInputStream
    private lateinit var output: DataOutputStream

    private lateinit var packetListener: PacketListener

    private var seq = 0
        get() = field++

    private var lastPacketSendTime = System.currentTimeMillis()

    val connectionListeners = mutableListOf<ConnectionListener>()
    val messageListeners = mutableListOf<MessageListener>()
    val userStatusListeners = mutableListOf<UserStatusListener>()
    val metaInfoListeners = mutableListOf<MetaInfoListener>()
    val contactListListeners = mutableListOf<ContactListListener>()
    val userAvatarsListeners = mutableListOf<UserAvatarsListener>()

    var connected = false

    fun addConnectionListener(cl: ConnectionListener) = connectionListeners.add(cl)

    fun addMessageListener(ml: MessageListener) = messageListeners.add(ml)

    fun addUserStatusListener(usl: UserStatusListener) = userStatusListeners.add(usl)

    fun addMetaInfoListener(ui: MetaInfoListener) = metaInfoListeners.add(ui)

    fun addContactListListener(cll: ContactListListener) = contactListListeners.add(cll)

    fun addUserAvatarListener(ual: UserAvatarsListener) = userAvatarsListeners.add(ual)

    fun removeConnectionListener(cl: ConnectionListener) = connectionListeners.remove(cl)

    fun removeMessageListener(ml: MessageListener) = messageListeners.remove(ml)

    fun removeUserStatusListener(usl: UserStatusListener) = userStatusListeners.remove(usl)

    fun removeMetaInfoListener(ui: MetaInfoListener) = metaInfoListeners.remove(ui)

    fun removeContactListListener(cll: ContactListListener) = contactListListeners.remove(cll)

    fun removeUserAvatarsListener(ual: UserAvatarsListener) = userAvatarsListeners.remove(ual)

    fun connect() {
        connected = true
        connection = Socket(server, 7023)
        input = DataInputStream(connection.getInputStream())
        output = DataOutputStream(connection.getOutputStream())
        packetListener = PacketListener(input, this, username, password, clientName, clientVersion)
        Thread(packetListener).start()
        val hello = Packet(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_HELLO)
        hello.addWTLD(WTLD(0x0001, UTF8(username)))
        send(hello)
    }

    fun send(packet: Packet) {
        packet.setSequence(seq)
        output.write(packet.toBytes())
        lastPacketSendTime = System.currentTimeMillis()
    }

    fun disconnect() {
        connection.close()
        connected = false
        seq = 0
        for (cl in connectionListeners) {
            cl.onLogout(0x0000)
        }
    }
}