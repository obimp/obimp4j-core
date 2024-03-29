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

package io.github.obimp.im

import java.nio.ByteBuffer
import java.time.LocalDateTime

/**
 * Incoming message
 * @author Alexander Krysin
 */
class IncomingMessage(
    override val accountName: String,
    override val uniqueMessageID: Int,
    override val messageType: MessageType,
    override val messageData: ByteBuffer,
    override val requestDeliveryReport: Boolean? = null,
    override val encryptionType: EncryptionType? = null,
    /** Offline message flag (optional) */
    val offlineMessage: Boolean,
    /** Offline message time (optional) */
    val offlineMessageTime: LocalDateTime?,
    /** System message flag (optional) */
    val systemMessage: Boolean,
    /** System message popup position (optional) */
    val systemMessagePopupPosition: SystemMessagePopupPosition?,
    /** Multiple message flag (optional) */
    val multipleMessage: Boolean,
    override val transportItemID: Int? = null
) : Message