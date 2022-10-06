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

package io.github.obimp.transports

/**
 * Transport state
 * @author Alexander Krysin
 */
enum class TransportState(val code: Short) {
    LOGGEDIN(0x0000),
    LOGGEDOFF(0x0001),
    STATUS_CHANGED(0x0002),
    CON_FAILED(0x0003),
    ACCOUNT_INVALID(0x0004),
    SERVICE_TEMP_UNAVAILABLE(0x0005),
    WRONG_PASSWORD(0x0006),
    INVALID_LOGIN(0x0007),
    OTHER_PLACE_LOGIN(0x0008),
    CANT_LOGIN_TRY_LATER(0x0009),
    SRV_PAUSED(0x000A),
    SRV_RESUMED(0x000B),
    SRV_MIGRATED(0x000C);

    companion object {
        @JvmStatic
        fun byCode(code: Short) = values().first { it.code == code }
    }
}