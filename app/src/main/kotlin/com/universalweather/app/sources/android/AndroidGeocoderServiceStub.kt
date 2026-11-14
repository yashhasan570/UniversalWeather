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

package com.universalweather.app.sources.android

import android.location.Geocoder
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.common.source.NonFreeNetSource
import com.universalweather.app.common.source.ReverseGeocodingSource

/**
 * The actual implementation is in the src_freenet and src_nonfreenet folders
 */
abstract class AndroidGeocoderServiceStub() :
    ReverseGeocodingSource,
    NonFreeNetSource {

    override val id = "nativegeocoder"
    override val name = "Android"

    override val supportedFeatures = mapOf(
        SourceFeature.REVERSE_GEOCODING to name
    )

    override fun isFeatureSupportedForLocation(location: Location, feature: SourceFeature): Boolean {
        return Geocoder.isPresent()
    }

    // Each device can implement their own geocoder, so better be safe
    override val knownAmbiguousCountryCodes: Array<String>? = null
}
