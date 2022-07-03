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
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.QuadWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.data.type.Word
import io.github.obimp.listener.*
import io.github.obimp.packet.Packet
import io.github.obimp.packet.PacketHandler
import io.github.obimp.packet.PacketListener
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.*

/**
 * OBIMP Connection
 * @author Alexander Krysin
 */
class OBIMPConnection(val server: String = "", val username: String = "", val password: String = "") {
    private lateinit var con: Socket

    private lateinit var `in`: DataInputStream

    lateinit var out: DataOutputStream

    private lateinit var listener: PacketListener

    private lateinit var t: Thread

    var seq = 0
        get() = field++

    val con_list: Vector<ConnectionListener> = Vector()
    val msg_list: Vector<MessageListener> = Vector()
    val stat_list: Vector<UserStatusListener> = Vector()
    val user_info: Vector<MetaInfoListener> = Vector()
    val cl_list: Vector<ContactListListener> = Vector()

    var connected = false

    fun addConnectionListener(cl: ConnectionListener) {
        con_list.add(cl)
    }

    fun addMessageListener(ml: MessageListener) {
        msg_list.add(ml)
    }

    fun addUserStatusListener(usl: UserStatusListener) {
        stat_list.add(usl)
    }

    fun addMetaInfoListener(ui: MetaInfoListener) {
        println("MetaInfoListener " + ui.javaClass.name + " has been added")
        user_info.add(ui)
    }

    fun addContactListListener(cll: ContactListListener) {
        cl_list.add(cll)
    }

    fun removeConnectionListener(cl: ConnectionListener): Boolean {
        println("ConnectionListener " + cl.javaClass.name + " has been removed")
        return con_list.remove(cl)
    }

    fun removeMessageListener(ml: MessageListener): Boolean {
        println("MessageListener " + ml.javaClass.name + " has been removed")
        return msg_list.remove(ml)
    }

    fun removeUserStatusListener(usl: UserStatusListener): Boolean {
        println("UserStatusListener " + usl.javaClass.name + " has been removed")
        return stat_list.remove(usl)
    }

    fun removeMetaInfoListener(ui: MetaInfoListener): Boolean {
        println("MetaInfoListener " + ui.javaClass.name + " has been removed")
        return user_info.remove(ui)
    }

    fun removeContactListListener(cll: ContactListListener): Boolean {
        println("ContactListListener " + cll.javaClass.name + " has been removed")
        return cl_list.remove(cll)
    }

    fun connect() {
        try {
            connected = true
            con = Socket(server, 7023)
            `in` = DataInputStream(con.getInputStream())
            out = DataOutputStream(con.getOutputStream())
            listener = PacketListener(con, `in`, this, username, password)
            t = Thread(listener)
            try {
                t.start()
                val hello = Packet(0x0001, 0x0001) // OBIMP_BEX_COM_CLI_HELLO
                hello.append(WTLD(0x00000001, UTF8(username)))
                send(hello)
                while (!PacketHandler.logged) {
                    Thread.sleep(100)
                }
                //                Packet login = new Packet(0x0001, 0x0003); // OBIMP_BEX_COM_CLI_LOGIN
//                login.append(new wTLD(0x00000001, new UTF8(username)));
//                login.append(new wTLD(0x00000002, new OctaWord(hash)));
//                out.write(login.asByteArray(getSeq()));
//                out.flush();
                send(Packet(0x0002, 0x0001)) // OBIMP_BEX_CL_CLI_PARAMS
                send(Packet(0x0003, 0x0001)) // OBIMP_BEX_PRES_CLI_PARAMS
                send(Packet(0x0004, 0x0001)) // OBIMP_BEX_IM_CLI_PARAMS
                send(Packet(0x0005, 0x0001)) // OBIMP_BEX_UD_CLI_PARAMS
                send(Packet(0x0006, 0x0001)) // OBIMP_BEX_UA_CLI_PARAMS
                send(Packet(0x0007, 0x0001)) // OBIMP_BEX_FT_CLI_PARAMS
                send(Packet(0x0008, 0x0001)) // OBIMP_BEX_TP_CLI_PARAMS
                send(Packet(0x0003, 0x0008)) // OBIMP_BEX_PRES_CLI_REQ_PRES_INFO
                send(Packet(0x0002, 0x0003)) // OBIMP_BEX_CL_CLI_REQUEST
                send(Packet(0x0002, 0x0005)) // OBIMP_BEX_CL_CLI_VERIFY
                val pres_info = Packet(0x0003, 0x0003) // OBIMP_BEX_PRES_CLI_SET_PRES_INFO
                pres_info.append(WTLD(0x00000001, arrayOf(Word(0x0001), Word(0x0002))))
                pres_info.append(WTLD(0x00000002, Word(0x0001)))
                pres_info.append(WTLD(0x00000003, UTF8("Java OBIMP Lib (OBIMP4J)")))
                pres_info.append(WTLD(0x00000004, QuadWord(0, 0, 0, 1, 0, 0, 0, 1)))
                pres_info.append(WTLD(0x00000005, Word(0x0052)))
                pres_info.append(
                    WTLD(
                        0x00000006, UTF8(
                            System.getProperty("os.name") + " " +
                                    if (System.getProperty("os.arch").contains("64")) "x64" else "x86"
                        )
                    )
                ) //операционная система
                send(pres_info)
                val set_status = Packet(0x0003, 0x0004) // OBIMP_BEX_PRES_CLI_SET_STATUS
                set_status.append(WTLD(0x00000001, LongWord(0, 0, 0, Status.PRES_STATUS_ONLINE.toInt())))
                //set_status.append(new wTLD(0x00000002, new UTF8("Работает!")));
                //set_status.append(new wTLD(0x00000004, new UTF8("Моя библиотека работает!")));
                //set_status.append(new wTLD(0x00000005, new UUID(1, XStatus.COFFEE)));
                send(set_status)
                send(Packet(0x0003, 0x0005)) // OBIMP_BEX_PRES_CLI_ACTIVATE
                val ud_details = Packet(0x0005, 0x0003) // OBIMP_BEX_UD_CLI_DETAILS_REQ
                ud_details.append(WTLD(0x0001, UTF8(username)))
                send(ud_details)
                send(Packet(0x0004, 0x0003)) // OBIMP_BEX_IM_CLI_REQ_OFFLINE
                send(Packet(0x0004, 0x0005)) // OBIMP_BEX_IM_CLI_DEL_OFFLINE
            } catch (ex: Exception) {
                println("Error:\n")
                ex.printStackTrace()
            }
        } catch (ex: Exception) {
            println("Error:\n")
            ex.printStackTrace()
        }
    }

    fun send(packet: Packet) {
        try {
            for (b in packet.asByteArray(0)) print("$b ")
            println()
            out.write(packet.asByteArray(seq))
        } catch (ex: java.lang.Exception) {
            println("Error:$ex")
        }
    }

    fun sendPong() {
        try {
            val pong = Packet(0x0001, 0x0007) // OBIMP_BEX_COM_CLI_SRV_KEEPALIVE_PONG
            out.write(pong.asByteArray(seq))
        } catch (ex: java.lang.Exception) {
            println("Error:$ex")
        }
    }

    fun disconnect() {
        try {
            connected = false
            con.close()
            seq = 0
            for (cl in con_list) {
                cl.onLogout("USER_DISCONNECTED")
            }
            /*con = null
            `in` = null
            out = null
            listener = null
            t = null*/
        } catch (ex: java.lang.Exception) {
            println("Error:$ex")
        }
    }

    companion object {
        var debug = false
    }
}