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
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Observable
import com.universalweather.app.common.exceptions.NonFreeNetSourceException
import com.universalweather.app.common.preference.Preference
import com.universalweather.app.common.source.LocationPositionWrapper
import javax.inject.Inject

class BaiduIPLocationService @Inject constructor(
    @ApplicationContext context: Context,
) : BaiduIPLocationServiceStub(context) {

    override val privacyPolicyUrl = ""

    override val isConfigured = true
    override val isRestricted = false

    override fun requestLocation(context: Context): Observable<LocationPositionWrapper> {
        throw NonFreeNetSourceException()
    }

    override fun getPreferences(context: Context): List<Preference> = emptyList()
}
