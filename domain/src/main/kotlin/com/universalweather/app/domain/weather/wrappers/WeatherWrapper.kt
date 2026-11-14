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

import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.domain.weather.model.Alert
import com.universalweather.app.domain.weather.model.Minutely
import com.universalweather.app.domain.weather.model.Normals
import com.universalweather.app.domain.weather.reference.Month

/**
 * Wrapper very similar to the object in database.
 * Helps the transition process and computing of missing data.
 */
data class WeatherWrapper(
    val dailyForecast: List<DailyWrapper>? = null,
    val hourlyForecast: List<HourlyWrapper>? = null,
    val current: CurrentWrapper? = null,
    val airQuality: AirQualityWrapper? = null,
    val pollen: PollenWrapper? = null,
    val minutelyForecast: List<Minutely>? = null,
    val alertList: List<Alert>? = null,
    /**
     * You can get the month with Month.of(month)
     * where month is a value between 1 and 12
     */
    val normals: Map<Month, Normals>? = null,
    val failedFeatures: Map<SourceFeature, Throwable>? = null,
)
