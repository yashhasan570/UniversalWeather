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

import android.Manifest
import android.content.Context
import io.reactivex.rxjava3.core.Observable
import com.universalweather.app.common.extensions.hasPermission

interface LocationSource : Source {

    fun requestLocation(context: Context): Observable<LocationPositionWrapper>

    // permission.
    val permissions: Array<String>
    fun hasPermissions(context: Context): Boolean {
        val permissions = permissions
        for (p in permissions) {
            if (p == Manifest.permission.ACCESS_COARSE_LOCATION || p == Manifest.permission.ACCESS_FINE_LOCATION) {
                continue
            }
            if (!context.hasPermission(p)) {
                return false
            }
        }

        val coarseLocation = context.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        val fineLocation = context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        return coarseLocation || fineLocation
    }
}
