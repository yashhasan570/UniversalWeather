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

package com.universalweather.app.sources.knmi

import io.reactivex.rxjava3.core.Observable
import com.universalweather.app.sources.knmi.json.KnmiWeather
import com.universalweather.app.sources.knmi.json.KnmiWeatherAlerts
import com.universalweather.app.sources.knmi.json.KnmiWeatherDetail
import com.universalweather.app.sources.knmi.json.KnmiWeatherSnapshot
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * See https://apis.developer.overheid.nl/apis/knmi-app-api/specificatie/production
 */
interface KnmiApi {
    @GET("alerts")
    fun getAlerts(): Observable<KnmiWeatherAlerts>

    @GET("weather")
    fun getWeather(
        @Query("location") location: Int,
        @Query("region") region: Int?,
    ): Observable<KnmiWeather>

    @GET("weather/detail")
    fun getWeatherDetail(
        @Query("location") location: Int,
        @Query("region") region: Int?,
        @Query("date") date: String,
    ): Observable<KnmiWeatherDetail>

    @GET("weather/snapshot")
    fun getWeatherSnapshot(
        @Query("location") location: Int,
        @Query("region") region: Int?,
    ): Observable<KnmiWeatherSnapshot>
}
