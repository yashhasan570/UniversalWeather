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

package com.universalweather.app.sources.bmkg

import io.reactivex.rxjava3.core.Observable
import com.universalweather.app.sources.bmkg.json.BmkgCurrentResult
import com.universalweather.app.sources.bmkg.json.BmkgForecastResult
import com.universalweather.app.sources.bmkg.json.BmkgIbfResult
import com.universalweather.app.sources.bmkg.json.BmkgLocationResult
import com.universalweather.app.sources.bmkg.json.BmkgWarningResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BmkgApi {
    @GET("api/df/v1/adm/coord")
    fun getLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Observable<BmkgLocationResult>

    @GET("api/presentwx/coord")
    fun getCurrent(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Observable<BmkgCurrentResult>

    @GET("api/df/v1/forecast/coord")
    fun getForecast(
        @Header("Referer") referer: String,
        @Header("X-API-KEY") apiKey: String,
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
    ): Observable<BmkgForecastResult>

    @GET("api/v1/public/weather/warning")
    fun getWarning(
        @Header("x-public-token") publicToken: String,
        @Query("lat") lat: Double,
        @Query("long") lon: Double,
    ): Observable<BmkgWarningResult>

    @GET("api/v1/public/weather/warning/ibf")
    fun getIbf(
        @Header("x-public-token") publicToken: String,
        @Query("lat") lat: Double,
        @Query("long") lon: Double,
        @Query("day") day: Int,
    ): Observable<BmkgIbfResult>
}
