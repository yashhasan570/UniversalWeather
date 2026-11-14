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

package com.universalweather.app.sources.lvgmc

import io.reactivex.rxjava3.core.Observable
import com.universalweather.app.sources.lvgmc.json.LvgmcAirQualityLocationResult
import com.universalweather.app.sources.lvgmc.json.LvgmcAirQualityResult
import com.universalweather.app.sources.lvgmc.json.LvgmcCurrentLocation
import com.universalweather.app.sources.lvgmc.json.LvgmcCurrentResult
import com.universalweather.app.sources.lvgmc.json.LvgmcForecastResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LvgmcApi {
    @GET("data/weather_points_forecast")
    fun getForecastLocations(
        @Query("laiks") laiks: String,
        @Query("bounds") bounds: String,
    ): Observable<List<LvgmcForecastResult>>

    @GET("data/weather_monitoring_points")
    fun getCurrentLocations(): Observable<List<LvgmcCurrentLocation>>

    @GET("data/na_atmosfera_stacijas")
    fun getAirQualityLocations(): Observable<List<LvgmcAirQualityLocationResult>>

    @GET("data/weather_forecast_for_location_{scope}")
    fun getForecast(
        @Path("scope") scope: String,
        @Query("punkts") punkts: String,
    ): Observable<List<LvgmcForecastResult>>

    @GET("data/weather_monitoring_data")
    fun getCurrent(): Observable<List<LvgmcCurrentResult>>

    @GET("data/gaisa_kvalitate_envista_batch")
    fun getAirQuality(
        @Query("stacija_id") station: String,
        @Query("no_datums") fromDate: String,
    ): Observable<List<LvgmcAirQualityResult>>
}
