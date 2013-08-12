/*
 * OBIMP4J - Java OBIMP Lib
 * Copyright (C) 2013 alex_xpert
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
 * Контакт
 * @author alex_xpert
 */
public class Contact extends ContactListItem {
    public String account_name;
    public String contact_name;
    public int privacy_type;
    
    public Contact(int _item_id, int _group_id, String _account_name, String _contact_name, int _privacy_type) {
        this.item_id = _item_id;
        this.group_id = _group_id;
        this.account_name = _account_name;
        this.contact_name = _contact_name;
        this.privacy_type = _privacy_type;
    }
}
