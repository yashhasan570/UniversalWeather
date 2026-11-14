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

package com.universalweather.app.sources.bmd

import io.reactivex.rxjava3.core.Observable
import com.universalweather.app.sources.bmd.json.BmdForecastResult
import retrofit2.http.GET
import retrofit2.http.Query

interface BmdApi {
    @GET("upazila_forecast_recent")
    fun getDaily(
        @Query("SOURCE") source: String = "BMDWRF",
        @Query("PCODE") pCode: String,
    ): Observable<BmdForecastResult>

    @GET("upazila_forecast_steps_recent")
    fun getHourly(
        @Query("SOURCE") source: String = "BMDWRF",
        @Query("PCODE") pCode: String,
    ): Observable<BmdForecastResult>
}
