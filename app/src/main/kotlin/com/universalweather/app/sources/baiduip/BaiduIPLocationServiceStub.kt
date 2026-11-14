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

package com.universalweather.app.sources.baiduip

import android.content.Context
import com.universalweather.app.domain.source.SourceContinent
import com.universalweather.app.common.extensions.code
import com.universalweather.app.common.extensions.currentLocale
import com.universalweather.app.common.extensions.getCountryName
import com.universalweather.app.common.source.ConfigurableSource
import com.universalweather.app.common.source.HttpSource
import com.universalweather.app.common.source.LocationSource
import com.universalweather.app.common.source.NonFreeNetSource

/**
 * The actual implementation is in the src_freenet and src_nonfreenet folders
 */
abstract class BaiduIPLocationServiceStub(context: Context) :
    HttpSource(),
    LocationSource,
    ConfigurableSource,
    NonFreeNetSource {

    override val id = "baidu_ip"
    override val name by lazy {
        with(context.currentLocale.code) {
            when {
                startsWith("zh") -> "百度IP定位"
                else -> "Baidu IP location"
            }
        } +
            " (${context.currentLocale.getCountryName("CN")})"
    }
    override val continent = SourceContinent.ASIA

    override fun hasPermissions(context: Context) = true

    override val permissions: Array<String> = emptyArray()
}
