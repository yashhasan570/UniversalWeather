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
import androidx.core.graphics.ColorUtils
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.R
import com.universalweather.app.common.activities.UniversalActivity
import com.universalweather.app.common.extensions.formatMeasure
import com.universalweather.app.common.extensions.formatValue
import com.universalweather.app.common.extensions.getThemeColor
import com.universalweather.app.common.options.appearance.DetailScreen
import com.universalweather.app.common.utils.helpers.IntentHelper
import com.universalweather.app.domain.settings.SettingsManager
import com.universalweather.app.ui.common.widgets.ArcProgress
import com.universalweather.app.ui.theme.resource.providers.ResourceProvider
import com.universalweather.app.unit.formatting.UnitWidth

class PressureViewHolder(parent: ViewGroup) : AbstractMainCardViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.container_main_pressure, parent, false)
) {
    private val pressureValueView: TextView = itemView.findViewById(R.id.pressure_value)
    private val pressureUnitView: TextView = itemView.findViewById(R.id.pressure_unit)
    private val pressureProgress: ArcProgress = itemView.findViewById(R.id.pressure_progress)
    private var mPressure = 963f
    private var mEnable = false

    override fun onBindView(
        activity: UniversalActivity,
        location: Location,
        provider: ResourceProvider,
        listAnimationEnabled: Boolean,
        itemAnimationEnabled: Boolean,
    ) {
        super.onBindView(activity, location, provider, listAnimationEnabled, itemAnimationEnabled)

        val talkBackBuilder = StringBuilder(context.getString(R.string.pressure))
        location.weather!!.current?.pressure?.let {
            val pressureUnit = SettingsManager.getInstance(context).getPressureUnit(context)
            mPressure = it.inHectopascals.toFloat()
            mEnable = true
            pressureProgress.apply {
                progress = mPressure.minus(963f)
                pressureValueView.text = it.formatValue(context)
            }
            val pressureColor = context.getThemeColor(androidx.appcompat.R.attr.colorPrimary)
            pressureProgress.apply {
                setProgressColor(pressureColor)
                setArcBackgroundColor(ColorUtils.setAlphaComponent(pressureColor, (255 * 0.1).toInt()))
                max = 100f
            }

            pressureUnitView.text = pressureUnit.getNominativeUnit(context)
            talkBackBuilder.append(context.getString(R.string.colon_separator))
            talkBackBuilder.append(it.formatMeasure(context, unitWidth = UnitWidth.LONG))
        }

        itemView.contentDescription = talkBackBuilder.toString()
        itemView.setOnClickListener {
            IntentHelper.startDailyWeatherActivity(
                context as UniversalActivity,
                location.formattedId,
                location.weather!!.todayIndex,
                DetailScreen.TAG_PRESSURE
            )
        }
    }
}
