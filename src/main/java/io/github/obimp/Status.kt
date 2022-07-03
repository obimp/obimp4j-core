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

/**
 * Status
 * @author Alexander Krysin
 */
object Status {
    val STATUS = arrayOf(
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
    )

    const val PRES_STATUS_ONLINE: Byte = 0x00
    const val PRES_STATUS_INVISIBLE: Byte = 0x01
    const val PRES_STATUS_INVISIBLE_FOR_ALL: Byte = 0x02
    const val PRES_STATUS_FREE_FOR_CHAT: Byte = 0x03
    const val PRES_STATUS_AT_HOME: Byte = 0x04
    const val PRES_STATUS_AT_WORK: Byte = 0x05
    const val PRES_STATUS_LUNCH: Byte = 0x06
    const val PRES_STATUS_AWAY: Byte = 0x07
    const val PRES_STATUS_NOT_AVAILABLE: Byte = 0x08
    const val PRES_STATUS_OCCUPIED: Byte = 0x09
    const val PRES_STATUS_DO_NOT_DISTURB: Byte = 0x0A
}