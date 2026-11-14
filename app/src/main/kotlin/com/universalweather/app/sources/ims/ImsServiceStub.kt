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

package com.universalweather.app.sources.ims

import android.content.Context
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.source.SourceContinent
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.common.extensions.code
import com.universalweather.app.common.extensions.currentLocale
import com.universalweather.app.common.extensions.getCountryName
import com.universalweather.app.common.source.HttpSource
import com.universalweather.app.common.source.LocationParametersSource
import com.universalweather.app.common.source.NonFreeNetSource
import com.universalweather.app.common.source.ReverseGeocodingSource
import com.universalweather.app.common.source.WeatherSource
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_HIGHEST
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_NONE

/**
 * The actual implementation is in the src_freenet and src_nonfreenet folders
 */
abstract class ImsServiceStub(context: Context) :
    HttpSource(),
    WeatherSource,
    ReverseGeocodingSource,
    LocationParametersSource,
    NonFreeNetSource {

    override val id = "ims"
    override val name by lazy {
        with(context.currentLocale.code) {
            when {
                startsWith("ar") -> "خدمة الأرصاد الجوية الإسرائيلية"
                startsWith("he") || startsWith("iw") -> "השירות המטאורולוגי הישראלי"
                else -> "IMS (${context.currentLocale.getCountryName("IL")})"
            }
        }
    }
    override val continent = SourceContinent.ASIA

    protected val weatherAttribution by lazy {
        with(context.currentLocale.code) {
            when {
                startsWith("ar") -> "خدمة الأرصاد الجوية الإسرائيلية"
                startsWith("he") || startsWith("iw") -> "השירות המטאורולוגי הישראלי"
                else -> "Israel Meteorological Service"
            }
        }
    }
    override val supportedFeatures = mapOf(
        SourceFeature.FORECAST to weatherAttribution,
        SourceFeature.CURRENT to weatherAttribution,
        SourceFeature.ALERT to weatherAttribution,
        SourceFeature.REVERSE_GEOCODING to weatherAttribution
    )

    override fun isFeatureSupportedForLocation(
        location: Location,
        feature: SourceFeature,
    ): Boolean {
        // Israel + West Bank + Gaza Strip
        return location.countryCode.equals("IL", ignoreCase = true) ||
            location.countryCode.equals("PS", ignoreCase = true)
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

    override val knownAmbiguousCountryCodes: Array<String> = arrayOf("IL")
}
