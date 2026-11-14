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

package com.universalweather.app.sources.bmkg.json

import kotlinx.serialization.Serializable

@Serializable
data class BmkgLocationResult(
    val adm1: String?,
    val adm2: String?,
    val adm3: String?,
    val adm4: String?,
    val provinsi: String?,
    val kotkab: String?,
    val kecamatan: String?,
    val desa: String?,
    val distance: Double?,
)
