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

package com.universalweather.app.sources.polleninfo

import android.content.Context
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.source.SourceContinent
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.R
import com.universalweather.app.common.extensions.currentLocale
import com.universalweather.app.common.extensions.getCountryName
import com.universalweather.app.common.source.ConfigurableSource
import com.universalweather.app.common.source.HttpSource
import com.universalweather.app.common.source.NonFreeNetSource
import com.universalweather.app.common.source.PollenIndexSource
import com.universalweather.app.common.source.WeatherSource
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_HIGH
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_NONE

/**
 * The actual implementation is in the src_freenet and src_nonfreenet folders
 */
abstract class PollenInfoServiceStub(context: Context) :
    HttpSource(),
    WeatherSource,
    PollenIndexSource,
    ConfigurableSource,
    NonFreeNetSource {

    override val id = "polleninfo"
    private val countryName = context.currentLocale.getCountryName("AT")
    override val name = "Pollen Information Service".let {
        if (it.contains(countryName)) {
            it
        } else {
            "$it ($countryName)"
        }
    }
    override val continent = SourceContinent.EUROPE

    private val weatherAttribution = "Österreichischer Polleninformationsdienst (Creative Commons Attribution 4.0)"
    override val supportedFeatures = mapOf(
        SourceFeature.POLLEN to weatherAttribution
    )
    override val attributionLinks = mapOf(
        "Österreichischer Polleninformationsdienst" to "https://www.polleninformation.at/"
    )

    override val pollenLabels = R.array.pollen_polleninfo_levels
    override val pollenColors = R.array.pollen_polleninfo_level_colors

    override fun isFeatureSupportedForLocation(
        location: Location,
        feature: SourceFeature,
    ): Boolean {
        return SUPPORTED_COUNTRY_CODES.any { it.equals(location.countryCode, ignoreCase = true) }
    }

    override fun getFeaturePriorityForLocation(
        location: Location,
        feature: SourceFeature,
    ): Int {
        return when {
            feature == SourceFeature.POLLEN && location.countryCode.equals("AT") -> PRIORITY_HIGH
            else -> PRIORITY_NONE
        }
    }

    // Makes the caching 1 hour, so that we respect the fair use policy
    override val isRestricted = true

    companion object {
        private val SUPPORTED_COUNTRY_CODES: List<String> = listOf(
            "AT", // Austria
            "CH", // Switzerland
            "DE", // Germany
            "ES", // Spain
            "FR", // France
            "GB", // United Kingdom
            "IT", // Italy
            "LT", // Lithuania
            "LV", // Latvia
            "PL", // Poland
            "SE", // Sweden
            "TR", // Turkey
            "UA" // Ukraine
        )
    }
}
