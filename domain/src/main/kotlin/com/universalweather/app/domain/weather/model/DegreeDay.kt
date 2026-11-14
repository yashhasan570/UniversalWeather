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
 * Degree Day
 */
class DegreeDay(
    val heating: com.universalweather.app.unit.temperature.Temperature? = null,
    val cooling: com.universalweather.app.unit.temperature.Temperature? = null,
) : Serializable {

    val isValid = (heating != null && heating.value > 0L) || (cooling != null && cooling.value > 0L)
}
