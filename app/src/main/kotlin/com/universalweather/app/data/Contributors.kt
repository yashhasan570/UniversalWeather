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

package com.universalweather.app.data

import androidx.annotation.StringRes
import com.universalweather.app.R

class ContributorItem(
    val name: String,
    val github: String? = null,
    val weblate: String? = null,
    val mail: String? = null,
    val url: String? = null,
    @StringRes val contribution: Int? = null,
) {
    val link = when {
        !github.isNullOrEmpty() -> "https://github.com/$github"
        !weblate.isNullOrEmpty() -> "https://hosted.weblate.org/user/$weblate/"
        !mail.isNullOrEmpty() -> "mailto:$mail"
        !url.isNullOrEmpty() -> url
        else -> ""
    }
}

class TranslatorItem(
    val lang: Array<String> = emptyArray(),
    val name: String,
    val github: String? = null,
    val weblate: String? = null,
    val mail: String? = null,
    val url: String? = null,
)

val appContributors: Array<ContributorItem> = arrayOf()

// Please keep them ordered by the main language translated so that we can easily sort translators by % contributed
// Here, we want to sort by language code, which is a different order than in Language.kt
// If you significantly contributed more than other translators, and you would like to appear
// first in the list, please open a GitHub issue
val appTranslators = arrayOf<TranslatorItem>()
