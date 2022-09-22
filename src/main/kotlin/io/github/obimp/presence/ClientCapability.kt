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

package io.github.obimp.presence

/**
 * @author Alexander Krysin
 */
enum class ClientCapability(val capability: Short) {
    MSGS_UTF8(0x0001),
    MSGS_RTF(0x0002),
    MSGS_HTML(0x0003),
    MSGS_ENCRYPT(0x0004),
    NOTIFS_TYPING(0x0005),
    AVATARS(0x0006),
    FILE_TRANSFER(0x0007),
    TRANSPORTS(0x0008),
    NOTIFS_ALARM(0x0009),
    NOTIFS_MAIL(0x000A);

    companion object {
        @JvmStatic
        fun byCapability(capability: Short) = values().first { it.capability == capability }
    }
}