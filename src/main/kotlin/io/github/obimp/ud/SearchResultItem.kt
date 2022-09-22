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

package io.github.obimp.ud

/**
 * @author Alexander Krysin
 */
class SearchResultItem(
    val searchRequestResult: SearchResult,
    var accountName: String? = null,
    var nickname: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var gender: Gender? = null,
    var age: Int? = null,
    var userIsOnline: Boolean? = null,
    var isLastSearchResult: Boolean? = null,
    var totalCountOfResultsInDB: Int? = null,
    var statusPictureFlags: Int? = null,
    var transportItemID: Int? = null
)