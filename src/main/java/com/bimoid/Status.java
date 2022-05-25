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

package com.bimoid;

/**
 * Status
 * @author Alexander Krysin
 */
public class Status {
    public static final String[] STATUS = {
            "Online",
            "Invisible",
            "Invisible for all",
            "Free for chat",
            "At home",
            "At work",
            "Lunch",
            "Away",
            "Not available",
            "Occupied",
            "Do not disturb"
    };

    public static final byte PRES_STATUS_ONLINE = 0x00;
    public static final byte PRES_STATUS_INVISIBLE = 0x01;
    public static final byte PRES_STATUS_INVISIBLE_FOR_ALL = 0x02;
    public static final byte PRES_STATUS_FREE_FOR_CHAT = 0x03;
    public static final byte PRES_STATUS_AT_HOME = 0x04;
    public static final byte PRES_STATUS_AT_WORK = 0x05;
    public static final byte PRES_STATUS_LUNCH = 0x06;
    public static final byte PRES_STATUS_AWAY = 0x07;
    public static final byte PRES_STATUS_NOT_AVAILABLE = 0x08;
    public static final byte PRES_STATUS_OCCUPIED = 0x09;
    public static final byte PRES_STATUS_DO_NOT_DISTURB = 0x0A;
}
