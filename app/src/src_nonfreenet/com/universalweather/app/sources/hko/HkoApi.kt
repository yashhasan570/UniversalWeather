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

package com.universalweather.app.sources.hko

import io.reactivex.rxjava3.core.Observable
import com.universalweather.app.sources.hko.json.HkoCurrentResult
import com.universalweather.app.sources.hko.json.HkoLocationsResult
import com.universalweather.app.sources.hko.json.HkoNormalsResult
import com.universalweather.app.sources.hko.json.HkoOneJsonResult
import com.universalweather.app.sources.hko.json.HkoWarningResult
import com.universalweather.app.sources.hko.json.HkoWarningSummaryResult
import retrofit2.http.GET
import retrofit2.http.Path

interface HkoApi {
    // The output is in JSON despite the file extension in the URL
    @GET("PDADATA/locspc/data/gridData/{grid}_en.xml")
    fun getCurrentWeather(
        @Path("grid") grid: String = "",
    ): Observable<HkoCurrentResult>

    // The output is in JSON despite the file extension in the URL
    @GET("cis/individual_month/monthlyElement.xml")
    fun getHkoNormals(): Observable<HkoNormalsResult>

    // The output is in JSON despite the file extension in the URL
    @GET("cis/aws/individual_month/monthly_{station}.xml")
    fun getNormals(
        @Path("station") station: String,
    ): Observable<HkoNormalsResult>

    @GET("{path}json/DYN_DAT_WARNSUM.json")
    fun getWarningSummary(
        @Path("path") path: String = "",
    ): Observable<HkoWarningSummaryResult>

    @GET("{path}json/DYN_DAT_MINDS_{key}.json")
    fun getWarningText(
        @Path("path") path: String = "",
        @Path("key") key: String,
    ): Observable<HkoWarningResult>

    // The output is in JSON despite the file extension in the URL
    @GET("{path}wxinfo/json/one_json{suffix}.xml")
    fun getOneJson(
        @Path("path") path: String = "",
        @Path("suffix") suffix: String = "",
    ): Observable<HkoOneJsonResult>

    @GET("PDADATA/locspc/data/gridData/geojson/L{grid}.geojson")
    fun getLocations(
        @Path("grid") grid: String,
    ): Observable<HkoLocationsResult>
}
