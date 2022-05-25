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

package com.obimp;

/**
 * X-Status
 * @author Alexander Krysin
 */
public class XStatus {
    public static final String[] X_Status = {
            "Not", "Smile", "Beach", "Cocktail", "Lifebuoy", "Cleaning", "Cooking", "Party", "Thinking", "Lunch", "TV",
            "Friends", "Coffee", "Music", "Business", "Camera", "Tongue", "Phone", "Gaming", "Study", "Shopping", "Car",
            "Ill", "Sleeping", "Browsing", "Working", "Writing", "Picnic", "Sport", "Mobile", "Sad", "WC", "Question",
            "Sound", "Heart", "Hunting", "Searching", "Journal", "Star", "Painting", "Shower", "Nature", "Idea",
            "Money", "Reading", "Chemical", "Sun", "Snow", "Fixing", "Thumbs up", "Shocked", "Planet", "Drink", "Angry",
            "Tired", "Smoke"
    };

    public static final byte NO = 0x00; // No X-Status
    
    public static final byte SMILE = 0x01; // smile
    public static final byte BEACH = 0x02; // beach
    public static final byte COCKTAIL = 0x03; // cocktail
    public static final byte LIFEBUOY = 0x04; // lifebuoy
    public static final byte CLEANING = 0x05; // cleaning
    public static final byte COOKING = 0x06; // cooking
    public static final byte PARTY = 0x07; // party
    public static final byte THINKING = 0x08; // thinking
    public static final byte LUNCH = 0x09; // lunch
    public static final byte TV = 0x0A; // TV
    public static final byte FRIENDS = 0x0B; // friends
    public static final byte COFFEE = 0x0C; // coffee
    public static final byte MUSIC = 0x0D; // music
    public static final byte BUSINESS = 0x0E; // business
    public static final byte CAMERA = 0x0F; // camera
    public static final byte TONGUE = 0x10; // tongue
    public static final byte PHONE = 0x11; // phone
    public static final byte GAMING = 0x12; // gaming
    public static final byte STUDY = 0x13; // study
    public static final byte SHOPPING = 0x14; // shopping
    public static final byte CAR = 0x15; // car
    public static final byte ILL = 0x16; // ill
    public static final byte SLEEPING = 0x17; // sleeping
    public static final byte BROWSING = 0x18; // browsing
    public static final byte WORKING = 0x19; // working
    public static final byte WRITING = 0x1A; // writing
    public static final byte PICNIC = 0x1B; // picnic
    public static final byte SPORT = 0x1C; // sport
    public static final byte MOBILE = 0x1D; // mobile
    public static final byte SAD = 0x1E; // sad
    public static final byte WC = 0x1F; // WC
    public static final byte QUESTION = 0x20; // question
    public static final byte SOUND = 0x21; // sound
    public static final byte HEART = 0x22; // heart
    public static final byte HUNTING = 0x23; // hunting
    public static final byte SEARCHING = 0x24; // searching
    public static final byte JOURNAL = 0x25; // journal
    public static final byte STAR = 0x26; // star
    public static final byte PAINTING = 0x27; // painting
    public static final byte SHOWER = 0x28; // shower
    public static final byte NATURE = 0x29; // nature
    public static final byte IDEA = 0x2A; // idea
    public static final byte MONEY = 0x2B; // money
    public static final byte READING = 0x2C; // reading
    public static final byte CHEMICAL = 0x2D; // chemical
    public static final byte SUN = 0x2E; // sun
    public static final byte SNOW = 0x2F; // snow
    public static final byte FIXING = 0x30; // fixing
    public static final byte THUMBS_UP = 0x31; // thumbs up
    public static final byte SHOCKED = 0x32; // shocked
    public static final byte PLANET = 0x33; // planet
    public static final byte DRINK = 0x34; // drink
    public static final byte ANGRY = 0x35; // angry
    public static final byte TIRED = 0x36; // tired
    public static final byte SMOKE = 0x37; // smoke
}
