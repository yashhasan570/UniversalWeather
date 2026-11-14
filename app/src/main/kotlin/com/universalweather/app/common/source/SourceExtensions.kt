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

package com.universalweather.app.common.source

import android.content.Context
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.BuildConfig
import com.universalweather.app.R

fun Source.getName(context: Context, feature: SourceFeature? = null, location: Location? = null): String {
    return if (this is RemovedSource) {
        context.getString(R.string.settings_weather_source_no_longer_exists, name)
    } else if (this is NonFreeNetSource && BuildConfig.FLAVOR == "freenet") {
        context.getString(R.string.settings_weather_source_non_freenet, name)
    } else if (this is ConfigurableSource && !isConfigured) {
        context.getString(R.string.settings_weather_source_not_configured, name)
    } else if (this is FeatureSource &&
        location != null &&
        feature != null &&
        (!supportedFeatures.contains(feature) || !isFeatureSupportedForLocation(location, feature))
    ) {
        context.getString(R.string.settings_weather_source_unavailable, name)
    } else {
        name
    }
}
