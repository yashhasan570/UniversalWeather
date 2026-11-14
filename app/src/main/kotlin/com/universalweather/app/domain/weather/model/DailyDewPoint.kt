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

import android.content.Context
import com.universalweather.app.domain.weather.model.DailyDewPoint
import com.universalweather.app.R
import com.universalweather.app.common.extensions.formatMeasure
import com.universalweather.app.common.extensions.formatValue
import com.universalweather.app.unit.formatting.UnitWidth
import com.universalweather.app.unit.temperature.TemperatureUnit

fun DailyDewPoint.getRangeSummary(context: Context, temperatureUnit: TemperatureUnit): String? {
    return if (min == null || max == null) {
        null
    } else if (min == max) {
        max!!.formatMeasure(context, temperatureUnit, unitWidth = UnitWidth.NARROW)
    } else {
        context.getString(
            R.string.dew_point_from_to_number,
            min!!.formatValue(context, temperatureUnit),
            max!!.formatMeasure(context, temperatureUnit, unitWidth = UnitWidth.NARROW)
        )
    }
}

fun DailyDewPoint.getRangeContentDescriptionSummary(context: Context, temperatureUnit: TemperatureUnit): String? {
    return if (min == null || max == null) {
        null
    } else if (min == max) {
        max!!.formatMeasure(context, temperatureUnit, unitWidth = UnitWidth.LONG)
    } else {
        context.getString(
            R.string.dew_point_from_to_number,
            min!!.formatValue(context, temperatureUnit),
            max!!.formatMeasure(context, temperatureUnit, unitWidth = UnitWidth.LONG)
        )
    }
}
