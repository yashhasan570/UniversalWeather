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

package com.universalweather.app.sources.gadgetbridge.json

import kotlinx.serialization.Serializable

@Serializable
data class GadgetbridgeHourlyForecast(
    val timestamp: Int? = null,
    val temp: Int? = null,
    val conditionCode: Int? = null,
    val humidity: Int? = null,
    val windSpeed: Float? = null,
    val windDirection: Int? = null,
    val uvIndex: Float? = null,
    val precipProbability: Int? = null,
    val dewPoint: Int? = null,
    val pressure: Float? = null,
    val cloudCover: Int? = null,
)
