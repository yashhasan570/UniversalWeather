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

package com.universalweather.app.ui.main.adapters.trend.hourly

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.weather.model.Precipitation
import com.universalweather.app.R
import com.universalweather.app.common.activities.UniversalActivity
import com.universalweather.app.common.extensions.formatMeasure
import com.universalweather.app.common.extensions.formatValue
import com.universalweather.app.common.extensions.getThemeColor
import com.universalweather.app.common.options.appearance.DetailScreen
import com.universalweather.app.domain.weather.model.getHourlyPrecipitationColor
import com.universalweather.app.ui.common.widgets.trend.TrendRecyclerView
import com.universalweather.app.ui.common.widgets.trend.chart.PolylineAndHistogramView
import com.universalweather.app.ui.theme.ThemeManager
import com.universalweather.app.ui.theme.resource.ResourceHelper
import com.universalweather.app.ui.theme.resource.providers.ResourceProvider
import com.universalweather.app.ui.theme.weatherView.WeatherViewController
import com.universalweather.app.unit.formatting.UnitWidth
import com.universalweather.app.unit.precipitation.Precipitation.Companion.millimeters

/**
 * Hourly precipitation adapter.
 */
class HourlyPrecipitationAdapter(
    activity: UniversalActivity,
    location: Location,
    provider: ResourceProvider,
) : AbsHourlyTrendAdapter(activity, location) {
    private val mResourceProvider: ResourceProvider = provider
    private var mHighestPrecipitation = Precipitation.PRECIPITATION_HOURLY_HEAVY.millimeters.inMicrometers.toFloat()

    inner class ViewHolder(itemView: View) : AbsHourlyTrendAdapter.ViewHolder(itemView) {
        private val mPolylineAndHistogramView = PolylineAndHistogramView(itemView.context)

        init {
            hourlyItem.chartItemView = mPolylineAndHistogramView
        }

        fun onBindView(activity: UniversalActivity, location: Location, position: Int) {
            val talkBackBuilder = StringBuilder(activity.getString(R.string.tag_precipitation))
            super.onBindView(activity, location, talkBackBuilder, position)
            val weather = location.weather!!
            val hourly = weather.nextHourlyForecast[position]

            hourlyItem.setIconDrawable(
                hourly.weatherCode?.let {
                    ResourceHelper.getWeatherIcon(mResourceProvider, it, hourly.isDaylight)
                },
                missingIconVisibility = View.INVISIBLE
            )

            val precipitation = hourly.precipitation?.total
            if (precipitation != null && precipitation.value > 0) {
                talkBackBuilder.append(activity.getString(com.universalweather.app.unit.R.string.locale_separator))
                    .append(precipitation.formatMeasure(activity, unitWidth = UnitWidth.LONG))
            } else {
                talkBackBuilder.append(activity.getString(com.universalweather.app.unit.R.string.locale_separator))
                    .append(activity.getString(R.string.precipitation_none))
            }
            mPolylineAndHistogramView.setData(
                null, null,
                null, null,
                null, null,
                precipitation?.value?.toFloat() ?: 0f,
                precipitation?.formatValue(activity),
                mHighestPrecipitation,
                0f
            )
            mPolylineAndHistogramView.setLineColors(
                hourly.precipitation?.getHourlyPrecipitationColor(activity) ?: Color.TRANSPARENT,
                hourly.precipitation?.getHourlyPrecipitationColor(activity) ?: Color.TRANSPARENT,
                activity.getThemeColor(com.google.android.material.R.attr.colorOutline)
            )

            val themeColors = ThemeManager
                .getInstance(itemView.context)
                .weatherThemeDelegate
                .getThemeColors(
                    itemView.context,
                    WeatherViewController.getWeatherKind(location),
                    WeatherViewController.isDaylight(location)
                )
            val lightTheme = ThemeManager.isLightTheme(itemView.context, location)
            mPolylineAndHistogramView.setShadowColors(
                themeColors[if (lightTheme) 1 else 2],
                themeColors[2],
                lightTheme
            )
            mPolylineAndHistogramView.setTextColors(
                activity.getThemeColor(R.attr.colorTitleText),
                activity.getThemeColor(R.attr.colorBodyText),
                activity.getThemeColor(R.attr.colorTitleText)
            )
            mPolylineAndHistogramView.setHistogramAlpha(if (lightTheme) 1f else 0.5f)
            hourlyItem.contentDescription = talkBackBuilder.toString()
            hourlyItem.setOnClickListener {
                onItemClicked(activity, location, bindingAdapterPosition, DetailScreen.TAG_PRECIPITATION)
            }
        }
    }

    init {
        location.weather!!.nextHourlyForecast
            .mapNotNull { it.precipitation?.total }
            .maxOrNull()
            ?.let {
                if (it.value > mHighestPrecipitation) {
                    mHighestPrecipitation = it.value.toFloat()
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trend_hourly, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbsHourlyTrendAdapter.ViewHolder, position: Int) {
        (holder as ViewHolder).onBindView(activity, location, position)
    }

    override fun getItemCount(): Int {
        return location.weather!!.nextHourlyForecast.size
    }

    override fun isValid(location: Location) = location.weather!!.nextHourlyForecast.any {
        it.precipitation?.total != null
    }

    override fun getDisplayName(context: Context): String {
        return context.getString(R.string.tag_precipitation)
    }

    override fun bindBackgroundForHost(host: TrendRecyclerView) {
        val keyLineList = mutableListOf<TrendRecyclerView.KeyLine>()
        /*keyLineList.add(
            TrendRecyclerView.KeyLine(
                Precipitation.PRECIPITATION_HOURLY_LIGHT.millimeters.inMicrometers.toFloat(),
                activity.getString(R.string.precipitation_intensity_light),
                Precipitation.PRECIPITATION_HOURLY_LIGHT.millimeters.formatValue(activity),
                TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
            )
        )
        keyLineList.add(
            TrendRecyclerView.KeyLine(
                Precipitation.PRECIPITATION_HOURLY_HEAVY.millimeters.inMicrometers.toFloat(),
                activity.getString(R.string.precipitation_intensity_heavy),
                Precipitation.PRECIPITATION_HOURLY_HEAVY.millimeters.formatValue(activity),
                TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
            )
        )*/
        host.setData(keyLineList, mHighestPrecipitation, 0f)
    }
}
