/*
 * OBIMP4J - Java OBIMP Lib
 * Copyright (C) 2013 alex_xpert
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

package com.obimp;

import com.obimp.data.structure.wTLD;
import com.obimp.data.type.BLK;
import com.obimp.data.type.LongWord;
import com.obimp.data.type.UTF8;
import com.obimp.data.type.UUID;
import com.obimp.data.type.Word;
import com.obimp.packet.Packet;
import com.obimp.packet.PacketHandler;
import java.io.ByteArrayOutputStream;

/**
 * Основной интерфейс
 * @author alex_xpert
 */
public class OBIMP {    
    public static final String VERSION = "Java OBIMP Lib (OBIMP4J) 1.0.3.7";
    
    private static volatile int msg_id = 0;
    
    public String getVersion() {
        return VERSION;
    }
    
    private static synchronized int getMsgID() {
        msg_id++;
        return msg_id;
    }
    
    /**
     * Установка/Смена статуса
     * @author alex_xpert
     * @param con
     * @param id
     * @param text 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void setStatus(OBIMPConnection con, byte stat, byte xstat, String st, String desc) {
        if(con != null) {
            try {
                Packet set_status = new Packet(0x0003, 0x0004); // OBIMP_BEX_PRES_CLI_SET_STATUS
                set_status.append(new wTLD(0x00000001, new LongWord(0, 0, 0, stat)));
                set_status.append(new wTLD(0x00000002, new UTF8(st)));
                set_status.append(new wTLD(0x00000004, new UTF8(desc)));
                set_status.append(new wTLD(0x00000005, new UUID(1, xstat)));
                con.out.write(set_status.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Отправка сообщения
     * @author alex_xpert
     * @param con
     * @param id
     * @param text 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static synchronized void sendMsg(OBIMPConnection con, String id, String text) {
        if(con != null) {
            try {
                Packet message = new Packet(0x0004, 0x0006); // OBIMP_BEX_IM_CLI_MESSAGE
                message.append(new wTLD(0x00000001, new UTF8(id)));
                message.append(new wTLD(0x00000002, new LongWord(0, 0, 0, getMsgID())));
                message.append(new wTLD(0x00000003, new LongWord(0, 0, 0, 1)));
                message.append(new wTLD(0x00000004, new BLK(text.getBytes())));
                con.out.write(message.asByteArray(con.getSeq()));
                con.out.flush();
                Thread.sleep(500);
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    public static void get_cli_info(OBIMPConnection con, String userId) {
        if(con != null) {
            try {
//                System.out.println("get_cli_info: userId="+userId);
                Packet inf = new Packet(PacketHandler.OBIMP_BEX_UD, PacketHandler.OBIMP_BEX_UD_CLI_DETAILS_REQ); //OBIMP_BEX_UD_CLI_DETAILS_REQ
                inf.append(new wTLD(0x00000001, new UTF8(userId)));
                con.out.write(inf.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public static void set_upd_info(OBIMPConnection con, String userId) {
        if(con != null) {
            try {
//                System.out.println("set_upd_info: userId="+userId);
                Packet upd_inf = new Packet(PacketHandler.OBIMP_BEX_UD, PacketHandler.OBIMP_BEX_UD_CLI_DETAILS_UPD); //OBIMP_BEX_UD_CLI_DETAILS_UPD

                upd_inf.append(new wTLD(0x00000001, new UTF8(userId)));
                upd_inf.append(new wTLD(0x00000002, new UTF8("Warik777"))); //good
                upd_inf.append(new wTLD(0x00000003, new UTF8("Alexander"))); //good
                upd_inf.append(new wTLD(0x00000004, new UTF8("Podunov"))); //good
                upd_inf.append(new wTLD(0x0005, new Word(183))); //страна
                upd_inf.append(new wTLD(0x00000006, new UTF8("Russian Federation"))); //регион/область/штат
                upd_inf.append(new wTLD(0x00000007, new UTF8("Bryansk"))); //город
                upd_inf.append(new wTLD(0x00000008, new UTF8("241902"))); //индекс
                upd_inf.append(new wTLD(0x00000009, new UTF8("address"))); //адрес
                upd_inf.append(new wTLD(0x000A, new Word(52))); //владение языками 1
                upd_inf.append(new wTLD(0x000B, new Word(19))); //владение языками 2
                upd_inf.append(new wTLD(0x0000000C, new com.obimp.data.type.Byte((byte)0x02))); //пол
//                upd_inf.append(new wTLD(0x0000000D, new DateTime( Calendar.getInstance().getTime().getTime()/1000L ))); //дата рождения
                upd_inf.append(new wTLD(0x0000000E, new UTF8("warik777.ru"))); //веб-сайт
                upd_inf.append(new wTLD(0x0000000F, new UTF8("about"))); //о себе
                upd_inf.append(new wTLD(0x00000010, new UTF8("interests: internet, gerl"))); //интересы
//                upd_inf.append(new wTLD(0x00000011, new UTF8("admin@warik777.ru"))); //электронная почта 1
//                upd_inf.append(new wTLD(0x00000012, new UTF8("san_pod@mail.ru"))); //электронная почта 2
//                upd_inf.append(new wTLD(0x00000013, new UTF8("+79201001010"))); //домашний телефон
//                upd_inf.append(new wTLD(0x00000014, new UTF8("+79201002020"))); //рабочий телефон
//                upd_inf.append(new wTLD(0x00000015, new UTF8("+79201003030"))); //сотовый телефон
//                upd_inf.append(new wTLD(0x00000016, new UTF8("+79201004040"))); //факс телефон
//                upd_inf.append(new wTLD(0x00000017, new Bool(true))); //if True then online status will not be shown in users directory
//                upd_inf.append(new wTLD(0x00000018, new UTF8("company: Warik777"))); //компания
//                upd_inf.append(new wTLD(0x00000019, new UTF8("department"))); //подразделение/отдел
                upd_inf.append(new wTLD(0x0000001A, new UTF8("position"))); //должность
//                upd_inf.append(new wTLD(0x00001001, new LongWord(0, 0, 0, 0)));

                con.out.write(upd_inf.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    public static void cliSearch(OBIMPConnection con, Object obj, int typeSearch) {
        if(con != null) {
            try {
//                System.out.println("cliSearch: userId="+userId);
                Packet search = new Packet(PacketHandler.OBIMP_BEX_UD, PacketHandler.OBIMP_BEX_UD_CLI_SEARCH); //OBIMP_BEX_UD_CLI_SEARCH
                switch(typeSearch){
                    case 1: //wTLD 0x0001: UTF8, account name
                        search.append(new wTLD(0x00000001, new UTF8(String.valueOf(obj))));
                        break;
                    case 2: //wTLD 0x0003: UTF8, nick name
                        search.append(new wTLD(0x00000003, new UTF8(String.valueOf(obj))));
                        break;
                    case 3: //wTLD 0x0004: UTF8, first name
                        search.append(new wTLD(0x00000004, new UTF8(String.valueOf(obj))));
                        break;
                    case 4: //wTLD 0x0005: UTF8, last name
                        search.append(new wTLD(0x00000005, new UTF8(String.valueOf(obj))));
                        break;
                    case 5: //wTLD 0x0009: Byte, gender (0x00 - not specified, 0x01 - female, 0x02 - male)
                        search.append(new wTLD(0x00000009, new com.obimp.data.type.Byte( Byte.parseByte(String.valueOf(obj)) )));
                        break;
                    case 6: //wTLD 0x0007: UTF8, city
                        search.append(new wTLD(0x00000007, new UTF8(String.valueOf(obj))));
                        break;
                    case 7: //wTLD 0x0006: Word, country code (can be found at the end of this document)
                        search.append(new wTLD(0x00000006, new Word( Integer.parseInt(String.valueOf(obj)) )));
                        break;
                    case 8: //wTLD 0x000C: Byte, zodiac sign code (see below)
                        search.append(new wTLD(0x0000000C, new com.obimp.data.type.Byte( Byte.parseByte(String.valueOf(obj)) )));
                        //ZODIAC_ARIES = 0x01
                        //ZODIAC_TAURUS = 0x02
                        //ZODIAC_GEMINI = 0x03
                        //ZODIAC_CANCER = 0x04
                        //ZODIAC_LEO = 0x05
                        //ZODIAC_VIRGO = 0x06
                        //ZODIAC_LIBRA = 0x07
                        //ZODIAC_SCORPIO = 0x08
                        //ZODIAC_SAGITTARIUS = 0x09
                        //ZODIAC_CAPRICORN = 0x0A
                        //ZODIAC_AQUARIUS = 0x0B
                        //ZODIAC_PISCES = 0x0C
                        break;   
                    case 9:
                        search.append(new wTLD(0x0000000A, new com.obimp.data.type.Byte( Byte.parseByte(String.valueOf(obj).split("-")[0]) ))); //wTLD 0x000A: Byte, age min (this value is starting from 1)
                        search.append(new wTLD(0x0000000B, new com.obimp.data.type.Byte( Byte.parseByte(String.valueOf(obj).split("-")[1]) ))); //wTLD 0x000B: Byte, age max
                        break;
                    case 10: //wTLD 0x0008: Word, language code (can be found at the end of this document)
                        search.append(new wTLD(0x00000008, new Word( Integer.parseInt(String.valueOf(obj)) )));
                        break;
                    case 11: //wTLD 0x000D: UTF8, interests
                        search.append(new wTLD(0x0000000D, new UTF8(String.valueOf(obj))));
                        break;
                }
                con.out.write(search.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    public static void addGroup(OBIMPConnection con, String name, int GroupID) {
        if(con != null) {
            try {
//                System.out.println("addGroup: name="+name+", id="+id);
                Packet add_group = new Packet(PacketHandler.OBIMP_BEX_CL, PacketHandler.OBIMP_BEX_CL_CLI_ADD_ITEM); //OBIMP_BEX_CL_CLI_ADD_ITEM
                add_group.append(new wTLD(0x00000001, new Word(0x0001)));
                add_group.append(new wTLD(0x00000002, new LongWord(0,0,0,GroupID)));
                add_group.append(new wTLD(0x00000003, new BLK(packGroupInfo(name)))); //array of sTLD, adding item sTLDs according item type
//                add_group.append(new wTLD(0x00000003, new UTF8(name))); //так не работает
                con.out.write(add_group.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    public static void updateGroup(OBIMPConnection con, String name, int ItemID) {
        if(con != null) {
            try {
//                System.out.println("updateGroup: name="+name+", id="+id);
                Packet upd_group = new Packet(PacketHandler.OBIMP_BEX_CL, PacketHandler.OBIMP_BEX_CL_CLI_UPD_ITEM); //OBIMP_BEX_CL_CLI_UPD_ITEM
                upd_group.append(new wTLD(0x00000001, new LongWord(0,0,0,ItemID)));
                upd_group.append(new wTLD(0x00000002, new LongWord(0,0,0,0)));
                upd_group.append(new wTLD(0x00000003, new BLK(packGroupInfo(name)))); //array of sTLD, item sTLDs according item type (optional)
//                upd_group.append(new wTLD(0x00000003, new UTF8(name))); //так не работает
                con.out.write(upd_group.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
        @SuppressWarnings("CallToThreadDumpStack")
    public static void delGroup(OBIMPConnection con, int ItemID) {
        if(con != null) {
            try {
//                System.out.println("delGroup: id="+id);
                Packet del_group = new Packet(PacketHandler.OBIMP_BEX_CL, PacketHandler.OBIMP_BEX_CL_CLI_DEL_ITEM); //OBIMP_BEX_CL_CLI_DEL_ITEM
                del_group.append(new wTLD(0x00000001, new LongWord(0,0,0,ItemID)));
                con.out.write(del_group.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    public static void cliAuthReply(OBIMPConnection con, String userId, boolean auth) {
        if(con != null) {
            try {
//                System.out.println("cliAuthReply: userId="+userId);
                Packet auth_reply = new Packet(PacketHandler.OBIMP_BEX_CL, PacketHandler.OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY); //OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY
                auth_reply.append(new wTLD(0x00000001, new UTF8(userId)));
                auth_reply.append(new wTLD(0x00000002, new Word(auth ? 0x0001 : 0x0002)));
                con.out.write(auth_reply.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public static void cliAuthRequest(OBIMPConnection con, String userId) {
        if(con != null) {
            try {
//                System.out.println("cliAuthRequest: userId="+userId);
                Packet auth_request = new Packet(PacketHandler.OBIMP_BEX_CL, PacketHandler.OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST); //OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST
                auth_request.append(new wTLD(0x00000001, new UTF8(userId)));
                auth_request.append(new wTLD(0x00000002, new UTF8("Разрешите добавить Вас в мой список контактов.")));
                con.out.write(auth_request.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    public static void cliAuthRevoke(OBIMPConnection con, String userId) {
        if(con != null) {
            try {
//                System.out.println("cliAuthRevoke: userId="+userId);
                Packet auth_request = new Packet(PacketHandler.OBIMP_BEX_CL, PacketHandler.OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE); //OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE
                auth_request.append(new wTLD(0x00000001, new UTF8(userId)));
                auth_request.append(new wTLD(0x00000002, new UTF8("Отзывалка авторизации")));
                con.out.write(auth_request.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    private static byte[] packGroupInfo(String g) {
        byte[] name = stringToByteArrayUtf8(g);
        byte[] result = new byte[4 + name.length];
        putWordBE(result, 0, 0x0001);
        putWordBE(result, 2, name.length);
        System.arraycopy(name, 0, result, 4, name.length);
        return result;
    }
    
    // Converts the specified string (val) to a byte array
    private static byte[] stringToByteArrayUtf8(String val) {
        // Write string in UTF-8 format
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            char ch;
            for (int i = 0; i < val.length(); ++i) {
                ch = val.charAt(i);
                if ((ch & 0x7F) == ch) {
                    if ((0x20 <= ch) || ('\r' == ch) || ('\n' == ch) || ('\t' == ch)) {
                        baos.write(ch);
                    }

                } else if ((ch & 0x7FF) == ch) {
                    baos.write(0xC0 | ((ch >>> 6) & 0x1F));
                    baos.write(0x80 | ((ch) & 0x3F));

                } else if ((ch & 0xFFFF) == ch) {
                    baos.write(0xE0 | ((ch >>> 12) & 0x0F));
                    baos.write(0x80 | ((ch >>> 6) & 0x3F));
                    baos.write(0x80 | ((ch) & 0x3F));
                }
            }
            return baos.toByteArray();
        } catch (Exception e) {
            // Do nothing
        }
        return null;
    }
   
    // Puts the specified word (val) into the buffer (buf) at position off using big endian byte ordering
    public static void putWordBE(byte[] buf, int off, int val) {
        buf[off]   = (byte) ((val >> 8) & 0x000000FF);
        buf[++off] = (byte) ((val)      & 0x000000FF);
    }
    
}
