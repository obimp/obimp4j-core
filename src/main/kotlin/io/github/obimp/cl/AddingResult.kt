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

package io.github.obimp.cl

/**
 * Adding result code
 * @author Alexander Krysin
 */
enum class AddingResult(val code: Short) {
    SUCCESS(0x0000),
    ERROR_WRONG_ITEM_TYPE(0x0001),
    ERROR_WRONG_PARENT_GROUP(0x0002),
    ERROR_NAME_LEN_LIMIT(0x0003),
    ERROR_WRONG_NAME(0x0004),
    ERROR_ITEM_ALREADY_EXISTS(0x0005),
    ERROR_ITEM_LIMIT_REACHED(0x0006),
    ERROR_BAD_REQUEST(0x0007),
    ERROR_BAD_ITEM_STLD(0x0008),
    ERROR_NOT_ALLOWED(0x0009);

    companion object {
        @JvmStatic
        fun byCode(code: Short) = values().first { it.code == code }
    }
}