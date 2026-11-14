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

package com.universalweather.app.remoteviews.config

import android.view.View
import android.widget.RemoteViews
import com.universalweather.app.data.location.LocationRepository
import com.universalweather.app.data.weather.WeatherRepository
import com.universalweather.app.domain.location.model.Location
import dagger.hilt.android.AndroidEntryPoint
import com.universalweather.app.R
import com.universalweather.app.remoteviews.presenters.TextWidgetIMP
import com.universalweather.app.remoteviews.presenters.TextWidgetIMP.getRemoteViews
import com.universalweather.app.sources.SourceManager
import javax.inject.Inject

/**
 * Text widget config activity.
 */
@AndroidEntryPoint
class TextWidgetConfigActivity : AbstractWidgetConfigActivity() {
    var locationNow: Location? = null
        private set

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var weatherRepository: WeatherRepository

    @Inject
    lateinit var sourceManager: SourceManager

    override suspend fun initLocations() {
        val location = locationRepository.getFirstLocation(withParameters = false)
        locationNow = location?.copy(
            weather = weatherRepository.getWeatherByLocationId(
                location.formattedId,
                withDaily = true, // isDaylight
                withHourly = false,
                withMinutely = false,
                withAlerts = true, // Custom subtitle
                withNormals = false
            )
        )
    }

    override fun initView() {
        super.initView()
        mTextColorContainer?.visibility = View.VISIBLE
        mTextSizeContainer?.visibility = View.VISIBLE
        mAlignEndContainer?.visibility = View.VISIBLE
        mHideSubtitleContainer?.visibility = View.VISIBLE
        mHideSubtitleTitle?.text = getString(R.string.widget_label_hide_header)
        mSubtitleDataContainer?.visibility = View.VISIBLE
    }

    override fun updateWidgetView() {
        TextWidgetIMP.updateWidgetView(
            this,
            locationNow,
            locationNow?.let { location ->
                sourceManager.getPollenIndexSource(
                    (location.pollenSource ?: "").ifEmpty { location.forecastSource }
                )
            }
        )
    }

    override val remoteViews: RemoteViews
        get() {
            return getRemoteViews(
                this,
                locationNow,
                textColorValueNow,
                textSize,
                alignEnd,
                hideSubtitle,
                subtitleDataValueNow,
                locationNow?.let { location ->
                    sourceManager.getPollenIndexSource(
                        (location.pollenSource ?: "").ifEmpty { location.forecastSource }
                    )
                }
            )
        }

    override val configStoreName: String
        get() {
            return getString(R.string.sp_widget_text_setting)
        }
}
