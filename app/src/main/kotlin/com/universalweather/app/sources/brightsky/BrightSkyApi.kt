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

package com.universalweather.app.sources.brightsky

import io.reactivex.rxjava3.core.Observable
import com.universalweather.app.sources.brightsky.json.BrightSkyAlertsResult
import com.universalweather.app.sources.brightsky.json.BrightSkyCurrentWeatherResult
import com.universalweather.app.sources.brightsky.json.BrightSkyRadarResult
import com.universalweather.app.sources.brightsky.json.BrightSkyWeatherResult
import retrofit2.http.GET
import retrofit2.http.Query

interface BrightSkyApi {
    @GET("weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("date") date: String,
        @Query("last_date") lastDate: String,
    ): Observable<BrightSkyWeatherResult>

    @GET("current_weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Observable<BrightSkyCurrentWeatherResult>

    @GET("alerts")
    fun getAlerts(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Observable<BrightSkyAlertsResult>

    @GET("radar")
    fun getRadar(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("distance") distance: Int,
        @Query("format") format: String,
        @Query("last_date") lastDate: String,
    ): Observable<BrightSkyRadarResult>
}
