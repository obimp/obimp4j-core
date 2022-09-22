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

package io.github.obimp.util

import java.security.MessageDigest
import java.util.*

/**
 * @author Alexander Krysin
 */
object HashUtil {
    fun md5(data: String) = md5(data.encodeToByteArray())

    fun md5(data: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("MD5")
        md.update(data)
        return md.digest()
    }

    fun base64(data: String) = Base64.getEncoder().encode(data.encodeToByteArray())
}