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

import java.io.Serializable

/**
 * Precipitation.
 */
data class Precipitation(
    val total: com.universalweather.app.unit.precipitation.Precipitation? = null,
    val thunderstorm: com.universalweather.app.unit.precipitation.Precipitation? = null,
    val rain: com.universalweather.app.unit.precipitation.Precipitation? = null,
    val snow: com.universalweather.app.unit.precipitation.Precipitation? = null,
    val ice: com.universalweather.app.unit.precipitation.Precipitation? = null,
) : Serializable {

    companion object {
        // Based on India Meteorological Department day values (divided by two for half days)
        // https://mausam.imd.gov.in/imd_latest/contents/pdf/forecasting_sop.pdf
        const val PRECIPITATION_HALF_DAY_VERY_LIGHT = 1.25
        const val PRECIPITATION_HALF_DAY_LIGHT = 7.75
        const val PRECIPITATION_HALF_DAY_MEDIUM = 32.25
        const val PRECIPITATION_HALF_DAY_HEAVY = 57.75
        const val PRECIPITATION_HALF_DAY_RAINSTORM = 102.25

        // Chapter 9.3.1 - Nowcasting
        const val PRECIPITATION_HOURLY_LIGHT = 5.0
        const val PRECIPITATION_HOURLY_MEDIUM = 10.0
        const val PRECIPITATION_HOURLY_HEAVY = 15.0
        const val PRECIPITATION_HOURLY_RAINSTORM = 20.0
    }
}
