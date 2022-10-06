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
 * Send file request
 * @author Alexander Krysin
 */
class SendFileRequest(
    /** Account name of file(s) receiver / sender */
    val accountName: String,
    /** Unique file transfer ID */
    val uniqueFileTransferID: Long,
    /** Files count */
    val filesCount: Int,
    /** File size or total size of the all files if more than one */
    val totalSize: Long,
    /** File name or file name of the first file if more than one */
    val firstFileName: String,
    /** Client's IP address of sender (for direct file transfer connection) */
    val senderClientIP: String? = null,
    /** Client's port number of sender that is listening (for direct file transfer connection) */
    val senderClientPortNumber: Int? = null,
    /** File proxy server host / IP (for proxied file transfer connection) */
    val proxyServerHostOrIP: String? = null,
    /** File proxy server port number (for proxied file transfer connection) */
    val proxyServerPortNumber: Int? = null
)