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

package com.universalweather.app.domain.weather.reference

enum class AlertSeverity(val id: Int) {
    EXTREME(4),
    SEVERE(3),
    MODERATE(2),
    MINOR(1),
    UNKNOWN(0),
    ;

    companion object {
        fun getInstance(
            value: Int?,
        ): AlertSeverity = AlertSeverity.entries.firstOrNull {
            it.id == value
        } ?: UNKNOWN
    }
}
