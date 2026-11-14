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

package com.universalweather.app.sources.recosante

import android.content.Context
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.source.SourceContinent
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.domain.weather.wrappers.WeatherWrapper
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Observable
import com.universalweather.app.common.exceptions.SourceNotInstalledException
import com.universalweather.app.common.extensions.currentLocale
import com.universalweather.app.common.extensions.getCountryName
import com.universalweather.app.common.source.HttpSource
import com.universalweather.app.common.source.RemovedSource
import com.universalweather.app.common.source.WeatherSource
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_MEDIUM
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_NONE
import javax.inject.Inject

/**
 * Recosanté pollen service.
 */
class RecosanteService @Inject constructor(
    @ApplicationContext context: Context,
) : HttpSource(), WeatherSource, RemovedSource {

    override val id = "recosante"
    override val name = "Recosanté (${context.currentLocale.getCountryName("FR")})"
    override val continent = SourceContinent.EUROPE
    override val privacyPolicyUrl = "https://recosante.beta.gouv.fr/donnees-personnelles/"

    override val supportedFeatures = mapOf(
        SourceFeature.POLLEN to "Recosanté • Atmo France"
    )
    override val attributionLinks = mapOf(
        "Recosanté" to "https://recosante.beta.gouv.fr/",
        "Atmo France" to "https://www.atmo-france.org/"
    )

    override fun isFeatureSupportedForLocation(
        location: Location,
        feature: SourceFeature,
    ): Boolean {
        return !location.countryCode.isNullOrEmpty() && location.countryCode.equals("FR", ignoreCase = true)
    }

    /**
     * Medium priority because it only has index, not concentrations
     */
    override fun getFeaturePriorityForLocation(
        location: Location,
        feature: SourceFeature,
    ): Int {
        return when {
            isFeatureSupportedForLocation(location, feature) -> PRIORITY_MEDIUM
            else -> PRIORITY_NONE
        }
    }

    override fun requestWeather(
        context: Context,
        location: Location,
        requestedFeatures: List<SourceFeature>,
    ): Observable<WeatherWrapper> {
        throw SourceNotInstalledException()
    }
}
