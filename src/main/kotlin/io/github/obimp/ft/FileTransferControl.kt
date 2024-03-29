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

package io.github.obimp.ft

/**
 * File transfer control code
 * @author Alexander Krysin
 */
enum class FileTransferControl(val code: Short) {
    CANCEL(0x0001),
    DIRECT_FAILED(0x0002),
    DIRECT_FAILED_TRY_REVERSE(0x0003),
    DIRECT_FAILED_TRY_PROXY(0x0004),
    PROXY_FAILED(0x0005),
    READY(0x0006);

    companion object {
        @JvmStatic
        fun byCode(code: Short) = values().first { it.code == code }
    }
}