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

package io.github.obimp.presence

/**
 * X-Status
 * @author Alexander Krysin
 */
enum class XStatus(val uuid: String) {
    SMILE("10790001-3AE3-4779-0034-340000000001"),
    BEACH("10790001-3AE3-4779-0034-340000000002"),
    COCKTAIL("10790001-3AE3-4779-0034-340000000003"),
    LIFEBUOY("10790001-3AE3-4779-0034-340000000004"),
    CLEANING("10790001-3AE3-4779-0034-340000000005"),
    COOKING("10790001-3AE3-4779-0034-340000000006"),
    PARTY("10790001-3AE3-4779-0034-340000000007"),
    THINKING("10790001-3AE3-4779-0034-340000000008"),
    LUNCH("10790001-3AE3-4779-0034-340000000009"),
    TV("10790001-3AE3-4779-0034-34000000000A"),
    FRIENDS("10790001-3AE3-4779-0034-34000000000B"),
    COFFEE("10790001-3AE3-4779-0034-34000000000C"),
    MUSIC("10790001-3AE3-4779-0034-34000000000D"),
    BUSINESS("10790001-3AE3-4779-0034-34000000000E"),
    CAMERA("10790001-3AE3-4779-0034-34000000000F"),
    TONGUE("10790001-3AE3-4779-0034-340000000010"),
    PHONE("10790001-3AE3-4779-0034-340000000011"),
    GAMING("10790001-3AE3-4779-0034-340000000012"),
    STUDY("10790001-3AE3-4779-0034-340000000013"),
    SHOPPING("10790001-3AE3-4779-0034-340000000014"),
    CAR("10790001-3AE3-4779-0034-340000000015"),
    ILL("10790001-3AE3-4779-0034-340000000016"),
    SLEEPING("10790001-3AE3-4779-0034-340000000017"),
    BROWSING("10790001-3AE3-4779-0034-340000000018"),
    WORKING("10790001-3AE3-4779-0034-340000000019"),
    WRITING("10790001-3AE3-4779-0034-34000000001A"),
    PICNIC("10790001-3AE3-4779-0034-34000000001B"),
    SPORT("10790001-3AE3-4779-0034-34000000001C"),
    MOBILE("10790001-3AE3-4779-0034-34000000001D"),
    SAD("10790001-3AE3-4779-0034-34000000001E"),
    WC("10790001-3AE3-4779-0034-34000000001F"),
    QUESTION("10790001-3AE3-4779-0034-340000000020"),
    SOUND("10790001-3AE3-4779-0034-340000000021"),
    HEART("10790001-3AE3-4779-0034-340000000022"),
    HUNTING("10790001-3AE3-4779-0034-340000000023"),
    SEARCHING("10790001-3AE3-4779-0034-340000000024"),
    JOURNAL("10790001-3AE3-4779-0034-340000000025"),
    STAR("10790001-3AE3-4779-0034-340000000026"),
    PAINTING("10790001-3AE3-4779-0034-340000000027"),
    SHOWER("10790001-3AE3-4779-0034-340000000028"),
    NATURE("10790001-3AE3-4779-0034-340000000029"),
    IDEA("10790001-3AE3-4779-0034-34000000002A"),
    MONEY("10790001-3AE3-4779-0034-34000000002B"),
    READING("10790001-3AE3-4779-0034-34000000002C"),
    CHEMICAL("10790001-3AE3-4779-0034-34000000002D"),
    SUN("10790001-3AE3-4779-0034-34000000002E"),
    SNOW("10790001-3AE3-4779-0034-34000000002F"),
    FIXING("10790001-3AE3-4779-0034-340000000030"),
    THUMBS_UP("10790001-3AE3-4779-0034-340000000031"),
    SHOCKED("10790001-3AE3-4779-0034-340000000032"),
    PLANET("10790001-3AE3-4779-0034-340000000033"),
    DRINK("10790001-3AE3-4779-0034-340000000034"),
    ANGRY("10790001-3AE3-4779-0034-340000000035"),
    TIRED("10790001-3AE3-4779-0034-340000000036"),
    SMOKE("10790001-3AE3-4779-0034-340000000037");

    companion object {
        @JvmStatic
        fun byUUID(uuid: String) = values().first { it.uuid == uuid }
    }
}