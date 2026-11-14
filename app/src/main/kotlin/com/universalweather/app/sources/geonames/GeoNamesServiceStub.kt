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

package com.universalweather.app.sources.geonames

import com.universalweather.app.domain.source.SourceContinent
import com.universalweather.app.common.source.ConfigurableSource
import com.universalweather.app.common.source.HttpSource
import com.universalweather.app.common.source.LocationSearchSource
import com.universalweather.app.common.source.NonFreeNetSource

/**
 * The actual implementation is in the src_freenet and src_nonfreenet folders
 */
abstract class GeoNamesServiceStub() :
    HttpSource(),
    LocationSearchSource,
    ConfigurableSource,
    NonFreeNetSource {

    override val id = "geonames"
    override val name = "GeoNames"
    override val continent = SourceContinent.WORLDWIDE
    override val privacyPolicyUrl = ""

    override val locationSearchAttribution = "GeoNames (CC BY 4.0)"

    // No known ambiguous code
    override val knownAmbiguousCountryCodes: Array<String> = emptyArray()
}
