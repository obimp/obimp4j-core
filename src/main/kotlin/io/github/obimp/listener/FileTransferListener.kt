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

package io.github.obimp.listener

import io.github.obimp.ft.*
import java.util.*

/**
 * File transfer listener
 * @author Alexander Krysin
 */
interface FileTransferListener : EventListener {
    /**
     * File transfer parameters callback
     * @param fileTransferParameters File transfer parameters
     */
    fun onFileTransferParameters(fileTransferParameters: FileTransferParameters)

    /**
     * Send file request callback
     * @param sendFileRequest Send file request
     */
    fun onSendFileRequest(sendFileRequest: SendFileRequest)

    /**
     * Send file reply callback
     * @param sendFileReply Send file reply
     */
    fun onSendFileReply(sendFileReply: SendFileReply)

    /**
     * Control callback
     * @param fileTransferControlMessage File transfer control message
     */
    fun onControl(fileTransferControlMessage: FileTransferControlMessage)

    /**
     * Error callback
     * @param fileTransferError File transfer error
     */
    fun onError(fileTransferError: FileTransferErrorType)

    /**
     * Hello callback
     */
    fun onHello(accountName: String, fileTransferID: Long)

    /**
     * File callback
     * @param fileTransferFileInfo File transfer file info
     */
    fun onFile(fileTransferFileInfo: FileTransferFileInfo)

    /**
     * File reply callback
     * @param fileTransferFileReply File transfer file reply
     */
    fun onFileReply(fileTransferFileReply: FileTransferFileReply)

    /**
     * File data callback
     * @param fileData File data
     */
    fun onFileData(fileData: FileTransferFileData)
}