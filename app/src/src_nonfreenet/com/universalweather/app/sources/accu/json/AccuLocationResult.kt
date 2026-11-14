/*
 * This file is part of Universal Weather.
 *
 * Universal Weather is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * Universal Weather is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Universal Weather. If not, see <https://www.gnu.org/licenses/>.
 */

package com.universalweather.app.sources.accu.json

import kotlinx.serialization.Serializable

/**
 * Accu location result.
 */
@Serializable
data class AccuLocationResult(
    val Key: String,
    val Type: String?,
    val LocalizedName: String?,
    val EnglishName: String?,
    val Country: AccuLocationArea,
    val AdministrativeArea: AccuLocationArea?,
    val TimeZone: AccuLocationTimeZone,
    val GeoPosition: AccuLocationGeoPosition,
    val DataSets: List<String>?,
)
