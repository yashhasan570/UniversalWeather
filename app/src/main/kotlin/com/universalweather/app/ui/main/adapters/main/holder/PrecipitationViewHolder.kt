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

package com.universalweather.app.ui.main.adapters.main.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.R
import com.universalweather.app.common.activities.UniversalActivity
import com.universalweather.app.common.extensions.areBlocksSquished
import com.universalweather.app.common.extensions.formatMeasure
import com.universalweather.app.common.extensions.toCalendarWithTimeZone
import com.universalweather.app.common.options.appearance.DetailScreen
import com.universalweather.app.common.utils.UnitUtils
import com.universalweather.app.common.utils.helpers.IntentHelper
import com.universalweather.app.ui.theme.resource.providers.ResourceProvider
import com.universalweather.app.unit.formatting.UnitWidth
import java.util.Calendar
import java.util.Date

class PrecipitationViewHolder(parent: ViewGroup) : AbstractMainCardViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.container_main_precipitation, parent, false)
) {
    private val precipitationValueView: TextView = itemView.findViewById(R.id.precipitation_value)
    private val precipitationAmountView: TextView = itemView.findViewById(R.id.precipitation_amount)

    override fun onBindView(
        activity: UniversalActivity,
        location: Location,
        provider: ResourceProvider,
        listAnimationEnabled: Boolean,
        itemAnimationEnabled: Boolean,
    ) {
        super.onBindView(activity, location, provider, listAnimationEnabled, itemAnimationEnabled)

        val talkBackBuilder = StringBuilder(context.getString(R.string.precipitation))
        location.weather?.let { weather ->
            val cal = Date().toCalendarWithTimeZone(location.timeZone)
            val currentHour = cal[Calendar.HOUR_OF_DAY]

            // Early morning
            val precipitation = if (currentHour < 5) {
                weather.dailyForecast.getOrElse(weather.todayIndex!!.minus(1)) { null }?.night?.precipitation
            } else if (currentHour < 17) {
                weather.today?.day?.precipitation
            } else {
                weather.today?.night?.precipitation
            }

            val isDay = currentHour in 5..16

            var isSnow = false
            precipitationAmountView.text = if ((precipitation?.rain?.value ?: 0) > 0 &&
                (precipitation?.snow?.value ?: 0) > 0
            ) {
                context.getString(
                    if (isDay) R.string.precipitation_total_day else R.string.precipitation_total_night
                )
            } else if ((precipitation?.snow?.value ?: 0) > 0) {
                context.getString(
                    if ((precipitation?.snow ?: 0.0) == (precipitation?.total ?: 0.0)) {
                        isSnow = true
                        if (isDay) R.string.precipitation_snow_total_day else R.string.precipitation_snow_total_night
                    } else {
                        if (isDay) R.string.precipitation_total_day else R.string.precipitation_total_night
                    }
                )
            } else if ((precipitation?.rain?.value ?: 0) > 0) {
                context.getString(
                    if ((precipitation?.rain ?: 0.0) == (precipitation?.total ?: 0.0)) {
                        if (isDay) R.string.precipitation_rain_total_day else R.string.precipitation_rain_total_night
                    } else {
                        if (isDay) R.string.precipitation_total_day else R.string.precipitation_total_night
                    }
                )
            } else {
                context.getString(
                    if (isDay) R.string.precipitation_total_day else R.string.precipitation_total_night
                )
            }
            precipitationAmountView.maxLines = if (itemView.context.areBlocksSquished) 2 else 3

            precipitation?.total?.let { total ->
                precipitationValueView.text = UnitUtils.formatUnitsHalfSize(total.formatMeasure(context))

                talkBackBuilder.append(context.getString(R.string.colon_separator))
                talkBackBuilder.append(precipitationAmountView.text)
                talkBackBuilder.append(context.getString(R.string.colon_separator))
                talkBackBuilder.append(total.formatMeasure(context, unitWidth = UnitWidth.LONG))
            } ?: run {
                precipitationValueView.text = "-"
            }
        }

        itemView.contentDescription = talkBackBuilder.toString()
        itemView.setOnClickListener {
            IntentHelper.startDailyWeatherActivity(
                context as UniversalActivity,
                location.formattedId,
                location.weather!!.todayIndex,
                DetailScreen.TAG_PRECIPITATION
            )
        }
    }
}
