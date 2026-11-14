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

package com.universalweather.app.sources.atmo

import io.reactivex.rxjava3.core.Observable
import com.universalweather.app.sources.atmo.json.AtmoPointResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * API
 */
interface AtmoApi {

    @GET("point?with_list=true")
    fun getPointDetails(
        @Header("api_token") headerApiToken: String?,
        @Header("User-Agent") userAgent: String,
        @Query("api_token") queryApiToken: String?,
        @Query("x") longitude: Double,
        @Query("y") latitude: Double,
        @Query("datetime_echeance") datetimeEcheance: String,
    ): Observable<AtmoPointResult>
}
