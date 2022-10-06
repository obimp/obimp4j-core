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
 * Transport option flag
 * @author Alexander Krysin
 */
enum class TransportOptionFlag(val value: Int) {
    CHECK(0x00000001),
    EDIT(0x00000002),
    COMBO(0x00000004),
    LINK(0x00000008),
    CHANGE(0x00010000),
    READ_ONLY(0x00020000),
    DISABLED(0x00040000);

    companion object {
        @JvmStatic
        fun byValue(value: Int) = values().first { it.value == value }
    }
}