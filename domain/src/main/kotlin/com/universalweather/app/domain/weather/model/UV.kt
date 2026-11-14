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

package com.universalweather.app.domain.weather.model

import java.io.Serializable
import kotlin.math.roundToInt

/**
 * UV.
 */
class UV(
    val index: Double? = null,
) : Serializable {

    val isValid: Boolean
        get() = index != null

    companion object {
        const val UV_INDEX_LOW = 3.0
        const val UV_INDEX_MIDDLE = 6.0
        const val UV_INDEX_HIGH = 8.0
        const val UV_INDEX_EXCESSIVE = 11.0

        val uvThresholds = listOf(
            0,
            UV_INDEX_LOW.roundToInt(),
            UV_INDEX_MIDDLE.roundToInt(),
            UV_INDEX_HIGH.roundToInt(),
            UV_INDEX_EXCESSIVE.roundToInt()
        )
    }
}
