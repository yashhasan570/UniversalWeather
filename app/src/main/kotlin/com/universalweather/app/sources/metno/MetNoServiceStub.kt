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

package com.universalweather.app.sources.metno

import android.content.Context
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.source.SourceContinent
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.UniversalWeather
import com.universalweather.app.BuildConfig
import com.universalweather.app.common.extensions.code
import com.universalweather.app.common.extensions.currentLocale
import com.universalweather.app.common.source.HttpSource
import com.universalweather.app.common.source.NonFreeNetSource
import com.universalweather.app.common.source.WeatherSource
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_HIGH
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_HIGHEST
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_NONE

/**
 * The actual implementation is in the src_freenet and src_nonfreenet folders
 */
abstract class MetNoServiceStub(context: Context) :
    HttpSource(),
    WeatherSource,
    NonFreeNetSource {

    override val id = "metno"
    override val name by lazy {
        with(context.currentLocale.code) {
            when {
                startsWith("no") -> "Meteorologisk institutt"
                else -> "MET Norway"
            }
        }
    }
    override val continent = SourceContinent.WORLDWIDE // The only exception here. It's a source commonly used worldwide

    private val weatherAttribution = "MET Norway (NLOD / CC BY 4.0)"
    override val supportedFeatures = mapOf(
        SourceFeature.FORECAST to weatherAttribution,
        SourceFeature.CURRENT to weatherAttribution,
        SourceFeature.MINUTELY to weatherAttribution,
        SourceFeature.AIR_QUALITY to weatherAttribution,
        SourceFeature.ALERT to weatherAttribution
    )

    override fun isFeatureSupportedForLocation(
        location: Location,
        feature: SourceFeature,
    ): Boolean {
        // This source needs to know how to contact the app maintainers
        // Make sure the app was compiled with the matching property in gradle.properties if failing here
        if (BuildConfig.REPORT_ISSUE.isEmpty() || UniversalWeather.instance.userAgent.isEmpty()) return false

        return when (feature) {
            // Nowcast only for Norway, Sweden, Finland and Denmark
            // Covered area is slightly larger as per https://api.met.no/doc/nowcast/datamodel
            // but safer to limit to guaranteed countries
            SourceFeature.CURRENT, SourceFeature.MINUTELY -> !location.countryCode.isNullOrEmpty() &&
                arrayOf("NO", "SE", "FI", "DK").any {
                    it.equals(location.countryCode, ignoreCase = true)
                }

            // Air quality only for Norway
            SourceFeature.AIR_QUALITY -> !location.countryCode.isNullOrEmpty() &&
                location.countryCode.equals("NO", ignoreCase = true)

            // Alerts only for Norway and Svalbard & Jan Mayen
            SourceFeature.ALERT -> !location.countryCode.isNullOrEmpty() &&
                arrayOf("NO", "SJ").any {
                    it.equals(location.countryCode, ignoreCase = true)
                }

            SourceFeature.FORECAST -> true

            else -> false
        }
    }

    /**
     * Highest priority for Norway
     * High priority for Svalbard & Jan Mayen, Sweden, Finland, Denmark
     */
    override fun getFeaturePriorityForLocation(
        location: Location,
        feature: SourceFeature,
    ): Int {
        return when {
            location.countryCode.equals("NO", ignoreCase = true) -> PRIORITY_HIGHEST
            arrayOf("SJ", "SE", "FI", "DK").any { it.equals(location.countryCode, ignoreCase = true) } &&
                isFeatureSupportedForLocation(location, feature) -> PRIORITY_HIGH
            else -> PRIORITY_NONE
        }
    }
}
