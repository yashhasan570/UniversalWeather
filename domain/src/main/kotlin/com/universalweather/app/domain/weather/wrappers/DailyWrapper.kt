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

import com.universalweather.app.domain.weather.model.AirQuality
import com.universalweather.app.domain.weather.model.Daily
import com.universalweather.app.domain.weather.model.DailyCloudCover
import com.universalweather.app.domain.weather.model.DailyDewPoint
import com.universalweather.app.domain.weather.model.DailyPressure
import com.universalweather.app.domain.weather.model.DailyRelativeHumidity
import com.universalweather.app.domain.weather.model.DailyVisibility
import com.universalweather.app.domain.weather.model.DegreeDay
import com.universalweather.app.domain.weather.model.Pollen
import com.universalweather.app.domain.weather.model.UV
import java.util.Date
import kotlin.time.Duration

/**
 * Daily wrapper
 */
data class DailyWrapper(
    val date: Date,
    val day: HalfDayWrapper? = null,
    val night: HalfDayWrapper? = null,
    val degreeDay: DegreeDay? = null,
    val uV: UV? = null,
    val sunshineDuration: Duration? = null,
    val relativeHumidity: DailyRelativeHumidity? = null,
    val dewPoint: DailyDewPoint? = null,
    val pressure: DailyPressure? = null,
    val cloudCover: DailyCloudCover? = null,
    val visibility: DailyVisibility? = null,
) {
    fun toDaily(
        airQuality: AirQuality? = null,
        pollen: Pollen? = null,
    ) = Daily(
        date = this.date,
        day = this.day?.toHalfDay(),
        night = this.night?.toHalfDay(),
        degreeDay = this.degreeDay,
        airQuality = airQuality,
        pollen = pollen,
        uV = this.uV,
        sunshineDuration = this.sunshineDuration,
        relativeHumidity = this.relativeHumidity,
        dewPoint = this.dewPoint,
        pressure = this.pressure,
        cloudCover = this.cloudCover,
        visibility = this.visibility
    )
}
