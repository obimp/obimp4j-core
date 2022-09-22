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

package io.github.obimp.ud

/**
 * @author Alexander Krysin
 */
enum class UpdateDetailsResult(val code: Short) {
    SUCCESS(0x0000),
    BAD_REQUEST(0x0001),
    SERVICE_TEMP_UNAVAILABLE(0x0002),
    NOT_ALLOWED(0x0003);

    companion object {
        @JvmStatic
        fun byCode(code: Short) = values().first { it.code == code }
    }
}