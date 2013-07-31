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
import com.obimp.data.type.Word;
import com.obimp.packet.Packet;

/**
 * Основной интерфейс
 * @author alex_xpert
 */
public class OBIMP {    
    public static final String VERSION = "Java OBIMP Lib (OBIMP4J) 1.0.2.2";
    
    private static int msg_id;
    
    public String getVersion() {
        return VERSION;
    }
    
    public static void sendMsg(OBIMPConnection con, String id, String text) {
        if(con != null) {
            try {
                Packet message = new Packet(0x0004, 0x0006); // OBIMP_BEX_IM_CLI_MESSAGE
                message.append(new wTLD(0x00000001, new UTF8(id)));
                message.append(new wTLD(0x00000002, new LongWord(0, 0, 0, msg_id)));
                message.append(new wTLD(0x00000003, new LongWord(0, 0, 0, 1)));
                message.append(new wTLD(0x00000004, new BLK(text.getBytes())));
                con.out.write(message.asByteArray(con.seq));
                con.out.flush();
                msg_id++;
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
    public static void set_upd_info(OBIMPConnection con, String userId) {
        if(con != null) {
            try {
               System.out.println("set_upd_info: userId="+userId);
                Packet upd_inf = new Packet(0x0005, 0x0005); //Пакет изменения личной информации
               
                upd_inf.append(new wTLD(0x0001, new UTF8(userId)));
                upd_inf.append(new wTLD(0x0002, new UTF8("Шуня"))); //good
                upd_inf.append(new wTLD(0x0003, new UTF8("Александр"))); //good
                upd_inf.append(new wTLD(0x0004, new UTF8("Крысин"))); //good
                upd_inf.append(new wTLD(0x0005, new Word(183))); //страна
                upd_inf.append(new wTLD(0x0006, new UTF8("Оренбуржье"))); //регион/область/штат
                upd_inf.append(new wTLD(0x0007, new UTF8("Бугуруслан"))); //город
                upd_inf.append(new wTLD(0x0008, new UTF8("461630"))); //индекс
                upd_inf.append(new wTLD(0x0009, new UTF8("Центр города"))); //адрес
                upd_inf.append(new wTLD(0x000A, new Word(52))); //владение языками 1
                upd_inf.append(new wTLD(0x000B, new Word(19))); //владение языками 2
                upd_inf.append(new wTLD(0x000C, new com.obimp.data.type.Byte((byte)0x02))); //пол
//                upd_inf.append(new wTLD(0x0000000D, new DateTime( Calendar.getInstance().getTime().getTime()/1000L ))); //дата рождения
                upd_inf.append(new wTLD(0x000E, new UTF8("worldjb.ru"))); //веб-сайт
                upd_inf.append(new wTLD(0x000F, new UTF8("й"))); //о себе
                upd_inf.append(new wTLD(0x0010, new UTF8("ж"))); //интересы
//                upd_inf.append(new wTLD(0x00000011, new UTF8("admin@warik777.ru"))); //электронная почта 1
//                upd_inf.append(new wTLD(0x00000012, new UTF8("san_pod@mail.ru"))); //электронная почта 2
//                upd_inf.append(new wTLD(0x00000013, new UTF8("+79201001010"))); //домашний телефон
//                upd_inf.append(new wTLD(0x00000014, new UTF8("+79201002020"))); //рабочий телефон
//                upd_inf.append(new wTLD(0x00000015, new UTF8("+79201003030"))); //сотовый телефон
//                upd_inf.append(new wTLD(0x00000016, new UTF8("+79201004040"))); //факс телефон
//                upd_inf.append(new wTLD(0x00000017, new Bool(true))); //if True then online status will not be shown in users directory
//                upd_inf.append(new wTLD(0x00000018, new UTF8("company: Warik777"))); //компания
//                upd_inf.append(new wTLD(0x00000019, new UTF8("department"))); //подразделение/отдел
//                upd_inf.append(new wTLD(0x001A, new UTF8("position"))); //должность
//                upd_inf.append(new wTLD(0x00001001, new LongWord(0, 0, 0, 0)));
               
                con.out.write(upd_inf.asByteArray(con.seq));
                con.out.flush();
                con.seq++;
            } catch(Exception ex) {
                System.out.println("Error:\n");
                ex.printStackTrace();
            }
        }
    }
    
}
