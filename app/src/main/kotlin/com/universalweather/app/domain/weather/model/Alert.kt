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
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.weather.model.Alert
import com.universalweather.app.common.extensions.getFormattedFullDayAndMonth
import com.universalweather.app.common.extensions.getFormattedMediumDayAndMonth
import com.universalweather.app.common.extensions.getFormattedTime
import com.universalweather.app.common.extensions.is12Hour

fun Alert.getFormattedDates(
    location: Location,
    context: Context,
    full: Boolean = false,
): String {
    val builder = StringBuilder()
    startDate?.let { startDate ->
        val startDateDay = if (full) {
            startDate.getFormattedFullDayAndMonth(location, context)
        } else {
            startDate.getFormattedMediumDayAndMonth(location, context)
        }
        builder.append(startDateDay)
            .append(context.getString(com.universalweather.app.unit.R.string.locale_separator))
            .append(startDate.getFormattedTime(location, context, context.is12Hour))
        endDate?.let { endDate ->
            builder.append(" — ")
            val endDateDay = if (full) {
                startDate.getFormattedFullDayAndMonth(location, context)
            } else {
                endDate.getFormattedMediumDayAndMonth(location, context)
            }
            if (startDateDay != endDateDay) {
                builder.append(endDateDay).append(context.getString(com.universalweather.app.unit.R.string.locale_separator))
            }
            builder.append(endDate.getFormattedTime(location, context, context.is12Hour))
        }
    }
    return builder.toString()
}
