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
    
    /**
     * Обновление инфо о себе
     * @author Warik777
     * @param con
     * @param userId 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void set_upd_info(OBIMPConnection con, String userId) {
        if(con != null) {
            try {
               System.out.println("set_upd_info: userId="+userId);
                Packet upd_inf = new Packet(0x0005, 0x0005); // Пакет изменения личной информации
               
                upd_inf.append(new wTLD(0x0001, new UTF8(userId)));
                upd_inf.append(new wTLD(0x0002, new UTF8("Шуня"))); // good
                upd_inf.append(new wTLD(0x0003, new UTF8("Александр"))); // good
                upd_inf.append(new wTLD(0x0004, new UTF8("Крысин"))); // good
                upd_inf.append(new wTLD(0x0005, new Word(183))); // страна
                upd_inf.append(new wTLD(0x0006, new UTF8("Оренбуржье"))); // регион/область/штат
                upd_inf.append(new wTLD(0x0007, new UTF8("Бугуруслан"))); // город
                upd_inf.append(new wTLD(0x0008, new UTF8("461630"))); // индекс
                upd_inf.append(new wTLD(0x0009, new UTF8("Центр города"))); // адрес
                upd_inf.append(new wTLD(0x000A, new Word(52))); // владение языками 1
                upd_inf.append(new wTLD(0x000B, new Word(19))); // владение языками 2
                upd_inf.append(new wTLD(0x000C, new com.obimp.data.type.Byte((byte)0x02))); // пол
                //upd_inf.append(new wTLD(0x0000000D, new DateTime( Calendar.getInstance().getTime().getTime()/1000L ))); // дата рождения
                upd_inf.append(new wTLD(0x000E, new UTF8("worldjb.ru"))); // веб-сайт
                upd_inf.append(new wTLD(0x000F, new UTF8("й"))); // о себе
                upd_inf.append(new wTLD(0x0010, new UTF8("ж"))); // интересы
                //upd_inf.append(new wTLD(0x00000011, new UTF8("admin@warik777.ru"))); // электронная почта 1
                //upd_inf.append(new wTLD(0x00000012, new UTF8("san_pod@mail.ru"))); // электронная почта 2
                //upd_inf.append(new wTLD(0x00000013, new UTF8("+79201001010"))); // домашний телефон
                //upd_inf.append(new wTLD(0x00000014, new UTF8("+79201002020"))); // рабочий телефон
                //upd_inf.append(new wTLD(0x00000015, new UTF8("+79201003030"))); // сотовый телефон
                //upd_inf.append(new wTLD(0x00000016, new UTF8("+79201004040"))); // факс телефон
                //upd_inf.append(new wTLD(0x00000017, new Bool(true))); // if True then online status will not be shown in users directory
                //upd_inf.append(new wTLD(0x00000018, new UTF8("company: Warik777"))); // компания
                //upd_inf.append(new wTLD(0x00000019, new UTF8("department"))); // подразделение/отдел
                //upd_inf.append(new wTLD(0x001A, new UTF8("position"))); // должность
                //upd_inf.append(new wTLD(0x00001001, new LongWord(0, 0, 0, 0)));
               
                con.out.write(upd_inf.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Добавление группы в КЛ
     * @author Warik777
     * @param con
     * @param name
     * @param id 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void addGroup(OBIMPConnection con, String name, int id) {
        if(con != null) {
            try {
                //System.out.println("addGroup: name="+name+", id="+id);
                Packet add_group = new Packet(0x0002, 0x0007); // Пакет добавления группы
                add_group.append(new wTLD(0x00000001, new Word(0x0001)));
                add_group.append(new wTLD(0x00000002, new LongWord(0,0,0,id)));
                //add_group.append(new wTLD(0x00000003, new BLK(packGroupInfo(name))));
                add_group.append(new wTLD(0x00000003, new UTF8(name)));
                con.out.write(add_group.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Обновление группы в КЛ
     * @author Warik777
     * @param con
     * @param name
     * @param id 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void updateGroup(OBIMPConnection con, String name, int id) {
        if(con != null) {
            try {
                 //System.out.println("updateGroup: name="+name+", id="+id);
                Packet upd_group = new Packet(0x0002, 0x000B); // Пакет обновление группы
                upd_group.append(new wTLD(0x00000001, new LongWord(0,0,0,id)));
                upd_group.append(new wTLD(0x00000002, new LongWord(0,0,0,0)));
                //upd_group.append(new wTLD(0x00000003, new BLK(packGroupInfo(name))));
                upd_group.append(new wTLD(0x00000003, new UTF8(name)));
                con.out.write(upd_group.asByteArray(con.getSeq()));
                con.out.flush();
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Пакет авторизации (авторизовать/отклонить)
     * @author Warik777
     * @param con
     * @param userId
     * @param auth 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void sendAuthReply(OBIMPConnection con, String userId, boolean auth) {
        if(con != null) {
            try {
               //System.out.println("sendAuthReply: userId="+userId);
                Packet auth_reply = new Packet(0x0002, 0x000E); // Пакет авторизации (авторизовать/отклонить)
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

    /**
     * Запрос авторизации
     * @author Warik777
     * @param con
     * @param userId 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void sendAuthRequest(OBIMPConnection con, String userId) {
        if(con != null) {
            try {
               //System.out.println("sendAuthRequest: userId="+userId);
                Packet auth_request = new Packet(0x0002, 0x000D); // Пакет запроса авторизации
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
    
    
    
}
