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

import androidx.annotation.StringRes
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.source.SourceContinent
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.R
import com.universalweather.app.domain.source.resourceName

interface Source {
    /**
     * Id for the source. Must be unique.
     */
    val id: String

    /**
     * Name of the source.
     */
    val name: String

    /**
     * How this source should be grouped:
     * - Recommended
     * - Worldwide
     * - Continent
     */
    @StringRes
    fun getGroup(location: Location, feature: SourceFeature): Int {
        return if (this is FeatureSource &&
            getFeaturePriorityForLocation(location, feature) >= 0
        ) {
            R.string.weather_source_recommended
        } else if (this is HttpSource) {
            continent.resourceName
        } else {
            SourceContinent.WORLDWIDE.resourceName
        }
    }
}
