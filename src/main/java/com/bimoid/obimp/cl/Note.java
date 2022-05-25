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

package com.bimoid.obimp.cl;

/**
 * Note
 * @author Alexander Krysin
 */
public class Note extends ContactListItem {
    public String noteName;
    public int noteType;
    public String noteText;
    
    public Note(String noteName, int noteType, String noteText) {
        this.noteName = noteName;
        this.noteType = noteType;
        this.noteText = noteText;
    }
}
