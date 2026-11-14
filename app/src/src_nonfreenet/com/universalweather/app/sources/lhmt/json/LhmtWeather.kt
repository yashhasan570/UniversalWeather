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

package com.universalweather.app.sources.lhmt.json

import kotlinx.serialization.Serializable
import com.universalweather.app.common.serializer.DateUtcSerializer
import java.util.Date

@Serializable
data class LhmtWeather(
    val forecastTimeUtc:
    @Serializable(DateUtcSerializer::class)
    Date?,
    val observationTimeUtc:
    @Serializable(DateUtcSerializer::class)
    Date?,
    val airTemperature: Double?,
    val feelsLikeTemperature: Double?,
    val windSpeed: Double?,
    val windGust: Double?,
    val windDirection: Double?,
    val cloudCover: Double?,
    val seaLevelPressure: Double?,
    val relativeHumidity: Double?,
    val totalPrecipitation: Double?,
    val conditionCode: String?,
)
