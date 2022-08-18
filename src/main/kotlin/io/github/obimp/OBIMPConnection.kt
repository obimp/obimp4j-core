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
import io.github.obimp.packet.Packet
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM
import io.github.obimp.packet.Packet.Companion.OBIMP_BEX_COM_CLI_HELLO
import io.github.obimp.packet.PacketListener
import io.github.obimp.tls.ObimpTlsClient
import kotlinx.coroutines.*
import org.bouncycastle.tls.TlsClientProtocol
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import kotlin.concurrent.thread

/**
 * OBIMP Connection
 * @author Alexander Krysin
 */
class OBIMPConnection(
    private val server: String = "bimoid.net",
    private val port: Int = 7023,
    private val secure: Boolean = false
) : ListenerManager() {
    private val connection = SocketChannel.open()
    private val selectionKey: SelectionKey
    private lateinit var tlsClientProtocol: TlsClientProtocol
    private lateinit var packetListener: PacketListener
    private var sequence = 0

    init {
        connection.configureBlocking(false)
        selectionKey = connection.register(selector, SelectionKey.OP_CONNECT or SelectionKey.OP_READ)
        selectionKey.attach(
            Runnable {
                val bytes = mutableListOf<Byte>()
                val buffer = ByteBuffer.allocate(1)
                if (selectionKey.isConnectable) {
                    connection.finishConnect()
                    if (secure) {
                        connectTls()
                    }
                }
                if (selectionKey.isReadable) {
                    while (connection.read(buffer) > 0) {
                        bytes.add(buffer[0])
                        buffer.clear()
                    }
                    if (bytes.size > 0) {
                        if (secure) {
                            tlsClientProtocol.offerInput(bytes.toByteArray())
                        } else {
                            packetListener.handlePacket(bytes)
                        }
                        bytes.clear()
                    }
                }
            }
        )

    }

    private fun connectTls() {
        tlsClientProtocol = TlsClientProtocol()
        thread {
            var available: Int
            var buffer: ByteArray
            while (connection.isConnected) {
                available = tlsClientProtocol.availableInputBytes
                if (available > 0) {
                    buffer = ByteArray(available)
                    tlsClientProtocol.readInput(buffer, 0, available)
                    packetListener.handlePacket(buffer.toMutableList())
                }
                available = tlsClientProtocol.availableOutputBytes
                if (available > 0) {
                    buffer = ByteArray(available)
                    tlsClientProtocol.readOutput(buffer, 0, available)
                    connection.write(ByteBuffer.wrap(buffer))
                }
            }
        }
        tlsClientProtocol.connect(ObimpTlsClient(server))
    }

    fun connect() {
        if (!selectorLoop.isAlive) {
            selectorLoop.start()
        }
        connection.connect(InetSocketAddress(server, port))
    }

    fun login(username: String, password: String) {
        if (secure) {
            while (!this::tlsClientProtocol.isInitialized || !tlsClientProtocol.isConnected) {
                Thread.sleep(50)
            }
        }
        packetListener = PacketListener(this, username, password)
        val hello = Packet(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_HELLO)
        hello.addWTLD(WTLD(0x0001, UTF8(username)))
        sendPacket(hello)
    }

    fun sendPacket(packet: Packet) {
        while (!connection.isConnected) {
            Thread.sleep(50)
        }
        packet.setSequence(sequence++)
        val bytes = packet.toBytes()
        if (secure) {
            tlsClientProtocol.writeApplicationData(bytes, 0, bytes.size)
        } else {
            connection.write(ByteBuffer.wrap(bytes))
        }
    }

    fun disconnect() {
        selectionKey.cancel()
        if (selector.keys().isEmpty()) {
            selector.close()
        }
        if (secure) {
            tlsClientProtocol.close()
        }
        connection.close()
        sequence = 0
        for (connectionListener in connectionListeners) {
            connectionListener.onLogout(0x0000)
        }
    }

    companion object {
        private val selector = Selector.open()
        private var selectorLoop = thread(start = false) {
            while (selector.isOpen) {
                selector.select()
                for (selectionKey in selector.selectedKeys()) {
                    (selectionKey.attachment() as Runnable).run()
                }
            }
        }
    }
}