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

import android.view.View
import com.universalweather.app.UniversalWeather
import com.universalweather.app.common.activities.UniversalActivity
import com.universalweather.app.common.snackbar.Snackbar

object SnackbarHelper {

    fun showSnackbar(
        content: String,
        action: String? = null,
        activity: UniversalActivity? = null,
        listener: View.OnClickListener? = null,
    ) {
        if (action != null && listener == null) {
            throw RuntimeException("Must send a non null listener as parameter.")
        }
        val container = (activity ?: UniversalWeather.instance.topActivity ?: return).provideSnackbarContainer()
        Snackbar.make(container.container, content, Snackbar.LENGTH_LONG, container.cardStyle)
            .setAction(action, listener)
            .setCallback(Snackbar.Callback())
            .show()
    }
}
