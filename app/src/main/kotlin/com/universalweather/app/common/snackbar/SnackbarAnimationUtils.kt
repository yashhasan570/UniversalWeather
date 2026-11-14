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

package com.universalweather.app.common.snackbar

import android.animation.Animator
import android.animation.AnimatorSet
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.universalweather.app.common.extensions.FLOATING_DECELERATE_INTERPOLATOR
import com.universalweather.app.common.extensions.getFloatingOvershotEnterAnimators

object SnackbarAnimationUtils : AnimationUtils() {
    val FAST_OUT_SLOW_IN_INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()

    fun getEnterAnimator(view: View, cardStyle: Boolean): Animator {
        view.translationY = view.height.toFloat()
        view.scaleX = if (cardStyle) 1.1f else 1f
        view.scaleY = if (cardStyle) 1.1f else 1f
        val animators = view.getFloatingOvershotEnterAnimators()
        if (!cardStyle) {
            animators[0].interpolator = FLOATING_DECELERATE_INTERPOLATOR
        }
        return AnimatorSet().apply {
            playTogether(animators[0], animators[1], animators[2])
        }
    }
}
