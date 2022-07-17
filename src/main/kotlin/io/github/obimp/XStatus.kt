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

    const val NO = 0x00 // No X-Status

    const val SMILE = 0x01 // smile
    const val BEACH = 0x02 // beach
    const val COCKTAIL = 0x03 // cocktail
    const val LIFEBUOY = 0x04 // lifebuoy
    const val CLEANING = 0x05 // cleaning
    const val COOKING = 0x06 // cooking
    const val PARTY = 0x07 // party
    const val THINKING = 0x08 // thinking
    const val LUNCH = 0x09 // lunch
    const val TV = 0x0A // TV
    const val FRIENDS = 0x0B // friends
    const val COFFEE = 0x0C // coffee
    const val MUSIC = 0x0D // music
    const val BUSINESS = 0x0E // business
    const val CAMERA = 0x0F // camera
    const val TONGUE = 0x10 // tongue
    const val PHONE = 0x11 // phone
    const val GAMING = 0x12 // gaming
    const val STUDY = 0x13 // study
    const val SHOPPING = 0x14 // shopping
    const val CAR = 0x15 // car
    const val ILL = 0x16 // ill
    const val SLEEPING = 0x17 // sleeping
    const val BROWSING = 0x18 // browsing
    const val WORKING = 0x19 // working
    const val WRITING = 0x1A // writing
    const val PICNIC = 0x1B // picnic
    const val SPORT = 0x1C // sport
    const val MOBILE = 0x1D // mobile
    const val SAD = 0x1E // sad
    const val WC = 0x1F // WC
    const val QUESTION = 0x20 // question
    const val SOUND = 0x21 // sound
    const val HEART = 0x22 // heart
    const val HUNTING = 0x23 // hunting
    const val SEARCHING = 0x24 // searching
    const val JOURNAL = 0x25 // journal
    const val STAR = 0x26 // star
    const val PAINTING = 0x27 // painting
    const val SHOWER = 0x28 // shower
    const val NATURE = 0x29 // nature
    const val IDEA = 0x2A // idea
    const val MONEY = 0x2B // money
    const val READING = 0x2C // reading
    const val CHEMICAL = 0x2D // chemical
    const val SUN = 0x2E // sun
    const val SNOW = 0x2F // snow
    const val FIXING = 0x30 // fixing
    const val THUMBS_UP = 0x31 // thumbs up
    const val SHOCKED = 0x32 // shocked
    const val PLANET = 0x33 // planet
    const val DRINK = 0x34 // drink
    const val ANGRY = 0x35 // angry
    const val TIRED = 0x36 // tired
    const val SMOKE = 0x37 // smoke
}