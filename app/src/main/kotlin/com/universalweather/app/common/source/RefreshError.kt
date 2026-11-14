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

package com.universalweather.app.common.source

import android.content.Context
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.R
import com.universalweather.app.domain.source.resourceName
import com.universalweather.app.sources.SourceManager
import com.universalweather.app.ui.main.utils.RefreshErrorType

class RefreshError(
    val error: RefreshErrorType,
    val source: String? = null,
    val feature: SourceFeature? = null,
) {
    fun getSourceWithOptionalFeature(context: Context, sourceManager: SourceManager): String? {
        return if (!source.isNullOrEmpty()) {
            val sourceName = sourceManager.getSource(source)?.name ?: source
            if (feature != null) {
                context.getString(R.string.parenthesis, sourceName, context.getString(feature.resourceName))
            } else {
                sourceName
            }
        } else {
            null
        }
    }

    fun getMessage(context: Context, sourceManager: SourceManager): String {
        return if (!source.isNullOrEmpty()) {
            "${getSourceWithOptionalFeature(context, sourceManager)}${context.getString(
                R.string.colon_separator
            )}${context.getString(error.shortMessage)}"
        } else {
            context.getString(error.shortMessage)
        }
    }
}
