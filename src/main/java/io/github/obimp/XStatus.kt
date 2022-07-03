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

package io.github.obimp

/**
 * X-Status
 * @author Alexander Krysin
 */
object XStatus {
    @JvmStatic
    val X_STATUS = arrayOf(
        "Not", "Smile", "Beach", "Cocktail", "Lifebuoy", "Cleaning", "Cooking", "Party", "Thinking", "Lunch", "TV",
        "Friends", "Coffee", "Music", "Business", "Camera", "Tongue", "Phone", "Gaming", "Study", "Shopping", "Car",
        "Ill", "Sleeping", "Browsing", "Working", "Writing", "Picnic", "Sport", "Mobile", "Sad", "WC", "Question",
        "Sound", "Heart", "Hunting", "Searching", "Journal", "Star", "Painting", "Shower", "Nature", "Idea",
        "Money", "Reading", "Chemical", "Sun", "Snow", "Fixing", "Thumbs up", "Shocked", "Planet", "Drink", "Angry",
        "Tired", "Smoke"
    )

    const val NO: Byte = 0x00 // No X-Status

    const val SMILE: Byte = 0x01 // smile
    const val BEACH: Byte = 0x02 // beach
    const val COCKTAIL: Byte = 0x03 // cocktail
    const val LIFEBUOY: Byte = 0x04 // lifebuoy
    const val CLEANING: Byte = 0x05 // cleaning
    const val COOKING: Byte = 0x06 // cooking
    const val PARTY: Byte = 0x07 // party
    const val THINKING: Byte = 0x08 // thinking
    const val LUNCH: Byte = 0x09 // lunch
    const val TV: Byte = 0x0A // TV
    const val FRIENDS: Byte = 0x0B // friends
    const val COFFEE: Byte = 0x0C // coffee
    const val MUSIC: Byte = 0x0D // music
    const val BUSINESS: Byte = 0x0E // business
    const val CAMERA: Byte = 0x0F // camera
    const val TONGUE: Byte = 0x10 // tongue
    const val PHONE: Byte = 0x11 // phone
    const val GAMING: Byte = 0x12 // gaming
    const val STUDY: Byte = 0x13 // study
    const val SHOPPING: Byte = 0x14 // shopping
    const val CAR: Byte = 0x15 // car
    const val ILL: Byte = 0x16 // ill
    const val SLEEPING: Byte = 0x17 // sleeping
    const val BROWSING: Byte = 0x18 // browsing
    const val WORKING: Byte = 0x19 // working
    const val WRITING: Byte = 0x1A // writing
    const val PICNIC: Byte = 0x1B // picnic
    const val SPORT: Byte = 0x1C // sport
    const val MOBILE: Byte = 0x1D // mobile
    const val SAD: Byte = 0x1E // sad
    const val WC: Byte = 0x1F // WC
    const val QUESTION: Byte = 0x20 // question
    const val SOUND: Byte = 0x21 // sound
    const val HEART: Byte = 0x22 // heart
    const val HUNTING: Byte = 0x23 // hunting
    const val SEARCHING: Byte = 0x24 // searching
    const val JOURNAL: Byte = 0x25 // journal
    const val STAR: Byte = 0x26 // star
    const val PAINTING: Byte = 0x27 // painting
    const val SHOWER: Byte = 0x28 // shower
    const val NATURE: Byte = 0x29 // nature
    const val IDEA: Byte = 0x2A // idea
    const val MONEY: Byte = 0x2B // money
    const val READING: Byte = 0x2C // reading
    const val CHEMICAL: Byte = 0x2D // chemical
    const val SUN: Byte = 0x2E // sun
    const val SNOW: Byte = 0x2F // snow
    const val FIXING: Byte = 0x30 // fixing
    const val THUMBS_UP: Byte = 0x31 // thumbs up
    const val SHOCKED: Byte = 0x32 // shocked
    const val PLANET: Byte = 0x33 // planet
    const val DRINK: Byte = 0x34 // drink
    const val ANGRY: Byte = 0x35 // angry
    const val TIRED: Byte = 0x36 // tired
    const val SMOKE: Byte = 0x37 // smoke
}