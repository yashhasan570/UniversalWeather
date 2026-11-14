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

package com.universalweather.app.ui.common.widgets.insets

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.updatePadding
import com.google.android.material.appbar.AppBarLayout
import com.universalweather.app.common.extensions.doOnApplyWindowInsets
import com.universalweather.app.common.extensions.getThemeColor
import com.universalweather.app.common.utils.ColorUtils

class FitSystemBarAppBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppBarLayout(context, attrs, defStyleAttr) {

    init {
        this.doOnApplyWindowInsets { view, insets ->
            view.updatePadding(
                top = insets.top,
                left = insets.left,
                right = insets.right
            )
        }
    }

    fun injectDefaultSurfaceTintColor() {
        setBackgroundColor(
            ColorUtils.getWidgetSurfaceColor(
                6f,
                context.getThemeColor(androidx.appcompat.R.attr.colorPrimary),
                context.getThemeColor(com.google.android.material.R.attr.colorSurface)
            )
        )
    }
}
