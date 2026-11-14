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

package com.universalweather.app.sources.china

import android.content.Context
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.source.SourceContinent
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.common.extensions.currentLocale
import com.universalweather.app.common.extensions.getCountryName
import com.universalweather.app.common.source.HttpSource
import com.universalweather.app.common.source.LocationParametersSource
import com.universalweather.app.common.source.LocationSearchSource
import com.universalweather.app.common.source.NonFreeNetSource
import com.universalweather.app.common.source.ReverseGeocodingSource
import com.universalweather.app.common.source.WeatherSource
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_HIGHEST
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_NONE

abstract class ChinaServiceStub(context: Context) :
    HttpSource(),
    WeatherSource,
    LocationSearchSource,
    ReverseGeocodingSource,
    LocationParametersSource,
    NonFreeNetSource {

    override val id = "china"
    override val name = context.currentLocale.getCountryName("CN")
    override val continent = SourceContinent.ASIA

    override val locationSearchAttribution = "北京天气、彩云天气、中国环境监测总站"

    protected val weatherAttribution = "北京天气、彩云天气、中国环境监测总站"
    override val supportedFeatures = mapOf(
        SourceFeature.FORECAST to weatherAttribution,
        SourceFeature.CURRENT to weatherAttribution,
        SourceFeature.AIR_QUALITY to weatherAttribution,
        SourceFeature.MINUTELY to weatherAttribution,
        SourceFeature.ALERT to weatherAttribution,
        SourceFeature.REVERSE_GEOCODING to name
    )
    override val attributionLinks = mapOf(
        "彩云天气" to "https://caiyunapp.com/",
        "中国环境监测总站" to "https://www.cnemc.cn/"
    )

    override fun isFeatureSupportedForLocation(
        location: Location,
        feature: SourceFeature,
    ): Boolean {
        return location.countryCode.equals("CN", ignoreCase = true)
    }

    override fun getFeaturePriorityForLocation(
        location: Location,
        feature: SourceFeature,
    ): Int {
        return when {
            isFeatureSupportedForLocation(location, feature) -> PRIORITY_HIGHEST
            else -> PRIORITY_NONE
        }
    }

    // Only supports its own country
    override val knownAmbiguousCountryCodes: Array<String>? = null
}
