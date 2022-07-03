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
import io.github.obimp.data.type.*
import io.github.obimp.meta.UserInfo
import io.github.obimp.packet.Packet
import io.github.obimp.packet.PacketHandler
import java.io.ByteArrayOutputStream

/**
 * OBIMP API
 * @author Alexander Krysin
 */
class OBIMP {
    companion object {
        const val VERSION = "OBIMP4J 0.1.0-alpha"
        var msg_id = 1
            get() = field++

        /**
         * Set status
         * @param con Connection
         * @param stat Status
         * @param xstat X-Status
         * @param st Status message
         * @param desc X-Status message
         */
        fun setStatus(con: OBIMPConnection, stat: Byte, xstat: Byte, st: String, desc: String) {
            if (con != null) {
                try {
                    val set_status = Packet(0x0003, 0x0004) // OBIMP_BEX_PRES_CLI_SET_STATUS
                    set_status.append(WTLD(0x00000001, LongWord(0, 0, 0, stat.toInt())))
                    set_status.append(WTLD(0x00000002, UTF8(st)))
                    set_status.append(WTLD(0x00000004, UTF8(desc)))
                    set_status.append(WTLD(0x00000005, UUID(1, xstat)))
                    con.out.write(set_status.asByteArray(con.seq))
                    con.out.flush()
                } catch (ex: Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        /**
         * Send message
         * @param con Connection
         * @param id Contact ID
         * @param text Message text
         */
        fun sendMsg(con: OBIMPConnection, id: String, text: String) {
            if (con != null) {
                try {
                    val message = Packet(0x0004, 0x0006) // OBIMP_BEX_IM_CLI_MESSAGE
                    message.append(WTLD(0x00000001, UTF8(id)))
                    message.append(WTLD(0x00000002, LongWord(0, 0, 0, msg_id)))
                    message.append(WTLD(0x00000003, LongWord(0, 0, 0, 1)))
                    message.append(WTLD(0x00000004, BLK(stringToByteArrayUtf8(text)!!)))
                    //con.out.write(message.asByteArray(con.getSeq()));
                    //con.out.flush();
                    con.send(message)
                } catch (ex: java.lang.Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        fun getCliInfo(con: OBIMPConnection, userId: String) {
            if (con != null) {
                try {
//                System.out.println("get_cli_info: userId="+userId);
                    val inf = Packet(
                        PacketHandler.OBIMP_BEX_UD,
                        PacketHandler.OBIMP_BEX_UD_CLI_DETAILS_REQ
                    ) //OBIMP_BEX_UD_CLI_DETAILS_REQ
                    inf.append(WTLD(0x00000001, UTF8(userId)))
                    con.out.write(inf.asByteArray(con.seq))
                    con.out.flush()
                } catch (ex: java.lang.Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        fun setUpdInfo(con: OBIMPConnection, info: UserInfo) {
            if (con != null) {
                try {
//                System.out.println("set_upd_info: userId="+userId);
                    val upd_inf = Packet(
                        PacketHandler.OBIMP_BEX_UD,
                        PacketHandler.OBIMP_BEX_UD_CLI_DETAILS_UPD
                    ) //OBIMP_BEX_UD_CLI_DETAILS_UPD
                    upd_inf.append(WTLD(0x00000001, UTF8(info.accountName)))
                    if (info.nickname != null) upd_inf.append(WTLD(0x00000002, UTF8(info.nickname))) //good
                    if (info.firstName != null) upd_inf.append(WTLD(0x00000003, UTF8(info.firstName))) //good
                    if (info.lastName != null) upd_inf.append(WTLD(0x00000004, UTF8(info.lastName))) //good
                    if (info.countryCode != -1) upd_inf.append(WTLD(0x0005, Word(info.countryCode))) //страна
                    if (info.regionState != null) upd_inf.append(
                        WTLD(
                            0x00000006,
                            UTF8(info.regionState)
                        )
                    ) //регион/область/штат
                    if (info.city != null) upd_inf.append(WTLD(0x00000007, UTF8(info.city))) //город
                    if (info.zipCode != null) upd_inf.append(WTLD(0x00000008, UTF8(info.zipCode))) //индекс
                    if (info.address != null) upd_inf.append(WTLD(0x00000009, UTF8(info.address))) //адрес
                    if (info.languageCode != -1) upd_inf.append(
                        WTLD(
                            0x000A,
                            Word(info.languageCode)
                        )
                    ) //владение языками 1
                    if (info.additionalLanguageCode != -1) upd_inf.append(
                        WTLD(
                            0x000B,
                            Word(info.additionalLanguageCode)
                        )
                    ) //владение языками 2
                    if (info.gender.toInt() != -1) {
                        upd_inf.append(
                            WTLD(
                                0x0000000C,
                                Byte(if (info.gender.toInt() != 0x01 && info.gender.toInt() != 0x02) 0x00 else info.gender)
                            )
                        ) //пол
                    }
                    //                upd_inf.append(new WTLD(0x0000000D, new DateTime( info.getBirthday() ))); //дата рождения
                    if (info.homepage != null) upd_inf.append(WTLD(0x0000000E, UTF8(info.homepage))) //веб-сайт
                    if (info.about != null) upd_inf.append(WTLD(0x0000000F, UTF8(info.about))) //о себе
                    if (info.interests != null) upd_inf.append(WTLD(0x00000010, UTF8(info.interests))) //интересы

//                if(info.getEmail()!= null) upd_inf.append(new WTLD(0x00000011, new UTF8(info.getEmail()))); //электронная почта 1

//                if(info.getAdditionalEmail()!= null) upd_inf.append(new WTLD(0x00000012, new UTF8(info.getAdditionalEmail()))); //электронная почта 2

//                if(info.getHomePhone()!= null) upd_inf.append(new WTLD(0x00000013, new UTF8(info.getHomePhone()))); //домашний телефон

//                if(info.getWorkPhone()!= null) upd_inf.append(new WTLD(0x00000014, new UTF8(info.getWorkPhone()))); //рабочий телефон

//                if(info.getCellularPhone()!= null) upd_inf.append(new WTLD(0x00000015, new UTF8(info.getCellularPhone()))); //сотовый телефон

//                if(info.getFaxNumber()!= null) upd_inf.append(new WTLD(0x00000016, new UTF8(info.getFaxNumber()))); //факс телефон

//                upd_inf.append(new WTLD(0x00000017, new Bool(info.getOnlineStatus()))); //if True then online status will not be shown in users directory

//                if(homeAddress!= null)upd_inf.append(new WTLD(0x00000018, new UTF8(info.getCompany()))); //компания

//                if(info.getDivisionDepartment()!= null) upd_inf.append(new WTLD(0x00000019, new UTF8(info.getDivisionDepartment()))); //подразделение/отдел
                    if (info.position != null) upd_inf.append(WTLD(0x0000001A, UTF8(info.position))) //должность

//                upd_inf.append(new WTLD(0x00001001, new LongWord(0, 0, 0, 0)));
                    con.send(upd_inf)
                } catch (ex: java.lang.Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        fun cliSearch(con: OBIMPConnection, obj: Any, typeSearch: Int) {
            if (con != null) {
                try {
//                System.out.println("cliSearch: userId="+userId);
                    val search = Packet(
                        PacketHandler.OBIMP_BEX_UD,
                        PacketHandler.OBIMP_BEX_UD_CLI_SEARCH
                    ) //OBIMP_BEX_UD_CLI_SEARCH
                    when (typeSearch) {
                        1 -> search.append(WTLD(0x00000001, UTF8(obj.toString())))
                        2 -> search.append(WTLD(0x00000003, UTF8(obj.toString())))
                        3 -> search.append(WTLD(0x00000004, UTF8(obj.toString())))
                        4 -> search.append(WTLD(0x00000005, UTF8(obj.toString())))
                        5 -> search.append(WTLD(0x00000009, Byte(obj.toString().toByte())))
                        6 -> search.append(WTLD(0x00000007, UTF8(obj.toString())))
                        7 -> search.append(WTLD(0x00000006, Word(obj.toString().toInt())))
                        8 -> search.append(WTLD(0x0000000C, Byte(obj.toString().toByte())))
                        9 -> {
                            search.append(
                                WTLD(
                                    0x0000000A,
                                    Byte(obj.toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()[0].toByte()))) //wTLD 0x000A: Byte, age min (this value is starting from 1)
                            search.append(
                                WTLD(
                                    0x0000000B,
                                    Byte(obj.toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()[1].toByte()))) //wTLD 0x000B: Byte, age max
                        }
                        10 -> search.append(WTLD(0x00000008, Word(obj.toString().toInt())))
                        11 -> search.append(WTLD(0x0000000D, UTF8(obj.toString())))
                    }
                    con.out.write(search.asByteArray(con.seq))
                    con.out.flush()
                } catch (ex: java.lang.Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        fun addGroup(con: OBIMPConnection, name: String, GroupID: Int) {
            if (con != null) {
                try {
//                System.out.println("addGroup: name="+name+", id="+id);
                    val add_group = Packet(
                        PacketHandler.OBIMP_BEX_CL,
                        PacketHandler.OBIMP_BEX_CL_CLI_ADD_ITEM
                    ) //OBIMP_BEX_CL_CLI_ADD_ITEM
                    add_group.append(WTLD(0x00000001, Word(0x0001)))
                    add_group.append(WTLD(0x00000002, LongWord(0, 0, 0, GroupID)))
                    add_group.append(
                        WTLD(
                            0x00000003,
                            BLK(OBIMP.packGroupInfo(name))
                        )
                    ) //array of sTLD, adding item sTLDs according item type
                    //                add_group.append(new WTLD(0x00000003, new UTF8(name))); //так не работает
                    con.out.write(add_group.asByteArray(con.seq))
                    con.out.flush()
                } catch (ex: java.lang.Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        fun updateGroup(con: OBIMPConnection, name: String, ItemID: Int) {
            if (con != null) {
                try {
//                System.out.println("updateGroup: name="+name+", id="+id);
                    val upd_group = Packet(
                        PacketHandler.OBIMP_BEX_CL,
                        PacketHandler.OBIMP_BEX_CL_CLI_UPD_ITEM
                    ) //OBIMP_BEX_CL_CLI_UPD_ITEM
                    upd_group.append(WTLD(0x00000001, LongWord(0, 0, 0, ItemID)))
                    upd_group.append(WTLD(0x00000002, LongWord(0, 0, 0, 0)))
                    upd_group.append(
                        WTLD(
                            0x00000003,
                            BLK(OBIMP.packGroupInfo(name))
                        )
                    ) //array of sTLD, item sTLDs according item type (optional)
                    //                upd_group.append(new WTLD(0x00000003, new UTF8(name))); //так не работает
                    con.out.write(upd_group.asByteArray(con.seq))
                    con.out.flush()
                } catch (ex: java.lang.Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        fun delGroup(con: OBIMPConnection, ItemID: Int) {
            if (con != null) {
                try {
//                System.out.println("delGroup: id="+id);
                    val del_group = Packet(
                        PacketHandler.OBIMP_BEX_CL,
                        PacketHandler.OBIMP_BEX_CL_CLI_DEL_ITEM
                    ) //OBIMP_BEX_CL_CLI_DEL_ITEM
                    del_group.append(WTLD(0x00000001, LongWord(0, 0, 0, ItemID)))
                    con.out.write(del_group.asByteArray(con.seq))
                    con.out.flush()
                } catch (ex: java.lang.Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        fun cliAuthReply(con: OBIMPConnection, userId: String, auth: Boolean) {
            if (con != null) {
                try {
//                System.out.println("cliAuthReply: userId="+userId);
                    val auth_reply = Packet(
                        PacketHandler.OBIMP_BEX_CL,
                        PacketHandler.OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY
                    ) //OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY
                    auth_reply.append(WTLD(0x00000001, UTF8(userId)))
                    auth_reply.append(WTLD(0x00000002, Word(if (auth) 0x0001 else 0x0002)))
                    con.out.write(auth_reply.asByteArray(con.seq))
                    con.out.flush()
                } catch (ex: java.lang.Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        fun cliAuthRequest(con: OBIMPConnection, userId: String) {
            if (con != null) {
                try {
//                System.out.println("cliAuthRequest: userId="+userId);
                    val auth_request = Packet(
                        PacketHandler.OBIMP_BEX_CL,
                        PacketHandler.OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST
                    ) //OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST
                    auth_request.append(WTLD(0x00000001, UTF8(userId)))
                    auth_request.append(WTLD(0x00000002, UTF8("Разрешите добавить Вас в мой список контактов.")))
                    con.out.write(auth_request.asByteArray(con.seq))
                    con.out.flush()
                } catch (ex: java.lang.Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        fun cliAuthRevoke(con: OBIMPConnection, userId: String) {
            if (con != null) {
                try {
//                System.out.println("cliAuthRevoke: userId="+userId);
                    val auth_request = Packet(
                        PacketHandler.OBIMP_BEX_CL,
                        PacketHandler.OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE
                    ) //OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE
                    auth_request.append(WTLD(0x00000001, UTF8(userId)))
                    auth_request.append(WTLD(0x00000002, UTF8("Отзывалка авторизации")))
                    con.out.write(auth_request.asByteArray(con.seq))
                    con.out.flush()
                } catch (ex: java.lang.Exception) {
                    println("Error:\n")
                    ex.printStackTrace()
                }
            }
        }

        private fun packGroupInfo(g: String): ByteArray {
            val name: ByteArray = stringToByteArrayUtf8(g)!!
            val result = ByteArray(4 + name.size)
            OBIMP.putWordBE(result, 0, 0x0001)
            OBIMP.putWordBE(result, 2, name.size)
            System.arraycopy(name, 0, result, 4, name.size)
            return result
        }

        private fun stringToByteArrayUtf8(value: String): ByteArray? {
            // Write string in UTF-8 format

            // Write string in UTF-8 format
            try {
                val baos = ByteArrayOutputStream()
                var ch: Char
                for (i in 0 until value.length) {
                    ch = value.get(i)
                    if (ch.code and 0x7F == ch.code) {
                        if (0x20 <= ch.code || '\r' == ch || '\n' == ch || '\t' == ch) {
                            baos.write(ch.code)
                        }
                    } else if (ch.code and 0x7FF == ch.code) {
                        baos.write(0xC0 or (ch.code ushr 6 and 0x1F))
                        baos.write(0x80 or (ch.code and 0x3F))
                    } else if (ch.code and 0xFFFF == ch.code) {
                        baos.write(0xE0 or (ch.code ushr 12 and 0x0F))
                        baos.write(0x80 or (ch.code ushr 6 and 0x3F))
                        baos.write(0x80 or (ch.code and 0x3F))
                    }
                }
                return baos.toByteArray()
            } catch (e: java.lang.Exception) {
                // Do nothing
            }
            return null
        }

        fun putWordBE(buf: ByteArray, off: Int, value: Int) {
            var off = off
            buf[off] = (value shr 8 and 0x000000FF).toByte()
            buf[++off] = (value and 0x000000FF).toByte()
        }
    }
}