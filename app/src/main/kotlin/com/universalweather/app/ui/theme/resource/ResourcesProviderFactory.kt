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

package com.universalweather.app.ui.theme.resource

import android.content.Context
import com.universalweather.app.UniversalWeather
import com.universalweather.app.domain.settings.SettingsManager
import com.universalweather.app.ui.theme.resource.providers.ChronusResourceProvider
import com.universalweather.app.ui.theme.resource.providers.DefaultResourceProvider
import com.universalweather.app.ui.theme.resource.providers.IconPackResourcesProvider
import com.universalweather.app.ui.theme.resource.providers.ResourceProvider

object ResourcesProviderFactory {
    val newInstance: ResourceProvider
        get() = getNewInstance(SettingsManager.getInstance(UniversalWeather.instance).iconProvider)

    fun getNewInstance(packageName: String?): ResourceProvider {
        val context: Context = UniversalWeather.instance
        val defaultProvider = DefaultResourceProvider()
        if (packageName == null || DefaultResourceProvider.isDefaultIconProvider(packageName)) {
            return defaultProvider
        }
        if (IconPackResourcesProvider.isIconPackIconProvider(context, packageName)) {
            return IconPackResourcesProvider(context, packageName, defaultProvider)
        }
        return if (ChronusResourceProvider.isChronusIconProvider(context, packageName)) {
            ChronusResourceProvider(context, packageName, defaultProvider)
        } else {
            IconPackResourcesProvider(
                context,
                packageName,
                defaultProvider
            )
        }
    }

    fun getProviderList(context: Context): List<ResourceProvider> {
        val providerList = mutableListOf<ResourceProvider>()
        val defaultProvider = DefaultResourceProvider()
        providerList.add(defaultProvider)

        // Universal Weather + Geometric Weather icon providers
        providerList.addAll(IconPackResourcesProvider.getProviderList(context, defaultProvider))

        // chronus icon pack.
        val chronusIconPackList = ChronusResourceProvider.getProviderList(context, defaultProvider).toMutableList()
        for (i in chronusIconPackList.indices.reversed()) {
            for (resourceProvider in providerList) {
                if (chronusIconPackList[i] == resourceProvider) {
                    chronusIconPackList.removeAt(i)
                    break
                }
            }
        }
        providerList.addAll(chronusIconPackList)
        return providerList
    }
}
