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

package com.universalweather.app.domain.weather.model

import com.universalweather.app.domain.weather.wrappers.TemperatureWrapper
import java.io.Serializable

/**
 * Temperature.
 */
data class Temperature(
    val temperature: com.universalweather.app.unit.temperature.Temperature? = null,
    val sourceFeelsLike: com.universalweather.app.unit.temperature.Temperature? = null,
    val computedApparent: com.universalweather.app.unit.temperature.Temperature? = null,
    val computedWindChill: com.universalweather.app.unit.temperature.Temperature? = null,
    val computedHumidex: com.universalweather.app.unit.temperature.Temperature? = null,
) : Serializable {

    val feelsLikeTemperature: com.universalweather.app.unit.temperature.Temperature? = sourceFeelsLike
        ?: computedApparent
        ?: computedWindChill
        ?: computedHumidex

    fun toTemperatureWrapper() = TemperatureWrapper(
        temperature = this.temperature,
        feelsLike = this.sourceFeelsLike
    )
}
