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
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.R
import com.universalweather.app.common.activities.UniversalActivity
import com.universalweather.app.common.extensions.currentLocale
import com.universalweather.app.common.options.appearance.DetailScreen
import com.universalweather.app.common.utils.helpers.IntentHelper
import com.universalweather.app.domain.weather.model.getContentDescription
import com.universalweather.app.domain.weather.model.getLevel
import com.universalweather.app.domain.weather.model.getShape
import com.universalweather.app.ui.theme.resource.providers.ResourceProvider
import com.universalweather.app.unit.formatting.format
import kotlin.math.roundToInt

class UvViewHolder(parent: ViewGroup) : AbstractMainCardViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.container_main_uv, parent, false)
) {
    private val sunShapeView: ImageView = itemView.findViewById(R.id.sun_shape)
    private val uvIndexValueView: TextView = itemView.findViewById(R.id.uv_index_value)
    private val uvIndexLevelView: TextView = itemView.findViewById(R.id.uv_index_level)

    override fun onBindView(
        activity: UniversalActivity,
        location: Location,
        provider: ResourceProvider,
        listAnimationEnabled: Boolean,
        itemAnimationEnabled: Boolean,
    ) {
        super.onBindView(activity, location, provider, listAnimationEnabled, itemAnimationEnabled)

        val talkBackBuilder = StringBuilder(context.getString(R.string.uv_index))
        sunShapeView.setImageDrawable(
            AppCompatResources.getDrawable(
                context,
                location.weather!!.current?.uV?.getShape() ?: R.drawable.uv_unknown
            )
        )
        uvIndexValueView.text = location.weather!!.current?.uV?.index?.roundToInt()
            ?.format(decimals = 0, locale = context.currentLocale)
            ?: "-"
        uvIndexLevelView.text = location.weather!!.current?.uV?.getLevel(context) ?: "-"

        location.weather!!.current?.uV?.getContentDescription(context)?.let {
            talkBackBuilder.append(context.getString(R.string.colon_separator))
            talkBackBuilder.append(it)
        }

        itemView.contentDescription = talkBackBuilder.toString()
        itemView.setOnClickListener {
            IntentHelper.startDailyWeatherActivity(
                context as UniversalActivity,
                location.formattedId,
                location.weather!!.todayIndex,
                DetailScreen.TAG_UV_INDEX
            )
        }
    }
}
