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

package com.universalweather.app.domain.weather.wrappers

import com.universalweather.app.domain.weather.model.Pollen
import java.util.Date

data class PollenWrapper(
    /**
     * Current will be used as "Today" if dailyForecast and hourlyForecast are empty
     */
    val current: Pollen? = null,
    val dailyForecast: Map<Date, Pollen>? = null,
    val hourlyForecast: Map<Date, Pollen>? = null,
)
