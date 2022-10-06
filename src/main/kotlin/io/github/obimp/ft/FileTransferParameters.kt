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
 * File transfer parameters
 * @author Alexander Krysin
 */
class FileTransferParameters(
    /** Maximal UTF-8 encoded account name length */
    val maxAccountNameLength: Int,
    /** Maximal UTF-8 encoded host / IP length */
    val maxHostOrIPLength: Int,
    /** Maximal UTF-8 encoded file name length */
    val maxFileNameLength: Int,
    /** Maximal UTF-8 encoded file path length */
    val maxFilePathLength: Int,
    /** File transfer support is enabled */
    val fileTransferSupportEnabled: Boolean,
    /** Proxied file transfer support is enabled (optional) */
    val proxiedFileTransferSupportEnabled: Boolean? = null,
    /** File proxy server host / IP (optional) */
    val fileProxyServerHostOrIP: String? = null,
    /** File proxy server port number (optional) */
    val fileProxyServerPortNumber: Int? = null
)