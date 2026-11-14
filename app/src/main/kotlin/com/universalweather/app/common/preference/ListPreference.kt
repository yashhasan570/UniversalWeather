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

package com.universalweather.app.common.preference

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

class ListPreference(
    @StringRes override val titleId: Int,
    @ArrayRes val valueArrayId: Int,
    @ArrayRes val nameArrayId: Int,
    val selectedKey: String,
    val onValueChanged: (String) -> Unit,
) : Preference
