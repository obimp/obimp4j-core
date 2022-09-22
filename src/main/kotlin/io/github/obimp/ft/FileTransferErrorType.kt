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
 * @author Alexander Krysin
 */
enum class FileTransferErrorType(val code: Short) {
    TIMEOUT(0x0001),
    WRONG_UNIQ_FT_ID(0x0002),
    WRONG_FILE_NAME(0x0003),
    WRONG_RELATIVE_PATH(0x0004),
    WRONG_RESUME_POS(0x0005),
    PROXY_TRAFFIC_LIMIT(0x0006);

    companion object {
        @JvmStatic
        fun byCode(code: Short) = values().first { it.code == code }
    }
}