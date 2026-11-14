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

package com.universalweather.app.ui.theme.resource.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.AnyRes

object ResourceUtils {
    @AnyRes
    fun getResId(context: Context, resName: String, type: String): Int {
        return try {
            context.resources.getIdentifier(resName, type, context.packageName)
        } catch (ignored: Exception) {
            0
        }
    }

    fun getDrawableUri(pkgName: String, resType: String, resName: String): Uri {
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(pkgName)
            .appendPath(resType)
            .appendPath(resName)
            .build()
    }
}
