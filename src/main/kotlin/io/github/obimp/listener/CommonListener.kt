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

import io.github.obimp.common.*
import java.util.*

/**
 * Common listener
 * @author Alexander Krysin
 */
interface CommonListener : EventListener {
    /**
     * Connect error callback
     */
    fun onConnectError()

    /**
     * Hello error callback
     * @param helloError Hello error
     */
    fun onHelloError(helloError: HelloError)

    /**
     * Login error callback
     * @param loginError Login error
     */
    fun onLoginError(loginError: LoginError)

    /**
     * Success connect callback
     */
    fun onConnect()

    /**
     * Disconnect callback
     * @param disconnectReason Disconnect reason
     */
    fun onDisconnect(disconnectReason: DisconnectReason)

    /**
     * Disconnect by server callback
     * @param byeReason Bye reason
     */
    fun onDisconnectByServer(byeReason: ByeReason)

    /**
     * Registration result callback
     * @param registrationResult Registration result
     */
    fun onRegistrationResult(registrationResult: RegistrationResult)
}