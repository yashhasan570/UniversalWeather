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

package com.universalweather.app.sources.wmosevereweather

import android.content.Context
import com.universalweather.app.domain.source.SourceContinent
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.common.extensions.code
import com.universalweather.app.common.extensions.currentLocale
import com.universalweather.app.common.source.HttpSource
import com.universalweather.app.common.source.NonFreeNetSource
import com.universalweather.app.common.source.WeatherSource

/**
 * The actual implementation is in the src_freenet and src_nonfreenet folders
 */
abstract class WmoSevereWeatherServiceStub(context: Context) :
    HttpSource(),
    WeatherSource,
    NonFreeNetSource {

    override val id = "wmosevereweather"
    override val name by lazy {
        with(context.currentLocale.code) {
            when {
                // Missing arabic abbreviation for WMO
                startsWith("ar") -> "WMO مركز معلومات الطقس القاسي"
                startsWith("eo") -> "MOM Severe Weather Information Centre"
                startsWith("es") -> "OMM Centro de Información de Tiempo Severo"
                startsWith("fr") -> "OMM Centre d’Information des Phénomènes Dangereux"
                startsWith("it") -> "OMM Eventi Meteorologici Estremi"
                startsWith("ko") -> "WMO 위험기상정보센터"
                startsWith("pl") -> "WMO Centrum Informacji o Groźnych Zjawiskach Pogodowych"
                startsWith("pt") -> "OMM Centro de Informação Tempo Severo"
                startsWith("ru") -> "ВМО Информационный центр неблагоприятных погодных условий"
                equals("zh-tw") || equals("zh-hk") || equals("zh-mo") -> "世界氣象組織惡劣天氣信息中心"
                startsWith("zh") -> "世界气象组织恶劣天气信息中心"
                else -> "WMO Severe Weather Information Centre"
            }
        }
    }
    override val continent = SourceContinent.WORLDWIDE

    override val supportedFeatures = mapOf(
        SourceFeature.ALERT to "Hong Kong Observatory on behalf of WMO + 141 issuing organizations"
    )
}
