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

package com.obimp.cl;

/**
 * Contact
 * @author Alexander Krysin
 */
public class Contact extends ContactListItem {
    public String accountName;
    public String contactName;
    public int privacyType;
    public String authorization;
    
    public Contact(int itemId, int groupId, String accountName, String contactName, int privacyType, String auth) {
        this.itemId = itemId;
        this.groupId = groupId;
        this.accountName = accountName;
        this.contactName = contactName;
        this.privacyType = privacyType;
        this.authorization = auth;
    }
}
