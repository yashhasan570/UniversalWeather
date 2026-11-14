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

package com.universalweather.app.common.utils.helpers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.createBitmap
import com.universalweather.app.domain.weather.reference.WeatherCode
import com.universalweather.app.ui.theme.resource.ResourceHelper
import com.universalweather.app.ui.theme.resource.providers.ResourceProvider

/**
 * Shortcuts manager.
 */
@RequiresApi(Build.VERSION_CODES.N_MR1)
object ShortcutsHelper {

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAdaptiveIcon(
        provider: ResourceProvider,
        code: WeatherCode,
        daytime: Boolean,
    ): Icon {
        return Icon.createWithAdaptiveBitmap(
            drawableToBitmap(
                ResourceHelper.getShortcutsForegroundIcon(provider, code, daytime)
            )
        )
    }

    fun getIcon(provider: ResourceProvider, code: WeatherCode, daytime: Boolean): Icon {
        return Icon.createWithBitmap(
            drawableToBitmap(
                ResourceHelper.getShortcutsIcon(provider, code, daytime)
            )
        )
    }
}
