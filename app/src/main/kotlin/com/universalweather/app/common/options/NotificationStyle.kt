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

package com.universalweather.app.common.options

import android.content.Context
import com.universalweather.app.R
import com.universalweather.app.common.utils.UnitUtils

enum class NotificationStyle(
    override val id: String,
) : BaseEnum {

    NATIVE("native"),
    CITIES("cities"),
    DAILY("daily"),
    HOURLY("hourly"),
    ;

    companion object {

        fun getInstance(
            value: String,
        ) = entries.firstOrNull {
            it.id == value
        } ?: DAILY
    }

    override val valueArrayId = R.array.notification_style_values
    override val nameArrayId = R.array.notification_styles

    override fun getName(context: Context) = UnitUtils.getName(context, this)
}
