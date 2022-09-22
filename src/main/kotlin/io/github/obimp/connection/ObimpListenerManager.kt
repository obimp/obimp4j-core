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

package io.github.obimp.connection

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.superclasses

/**
 * @author Alexander Krysin
 */
class ObimpListenerManager : ListenerManager {
    private val listeners = mutableMapOf<KClass<*>, MutableList<out EventListener>>()

    override fun <T : EventListener> addListener(listener: T) {
        val listenerClass = listener::class.superclasses.first { it.isSubclassOf(EventListener::class) }
        listeners.merge(listenerClass, mutableListOf(listener)) { currentValue, addedValue ->
            (currentValue + addedValue) as MutableList<out EventListener>
        }
    }

    override fun <T : EventListener> removeListener(listener: T) {
        val listenerClass = listener::class.superclasses.first { it.isSubclassOf(EventListener::class) }
        listeners[listenerClass]?.removeIf(listener::equals)
    }

    override fun <T : EventListener> getListeners(type: KClass<T>): List<T> {
        listeners[type]?.let { currentListeners ->
            var listeners = mutableListOf<T>()
            currentListeners.forEach {
                @Suppress("UNCHECKED_CAST")
                listeners = (listeners + (it as T)) as MutableList<T>
            }
            return listeners
        }
        return listOf()
    }
}