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

package com.universalweather.app.ui.settings.compose

sealed class SettingsScreenRouter(val route: String) {
    object Root : SettingsScreenRouter("com.universalweather.app.ui.settings.root")
    object BackgroundUpdates : SettingsScreenRouter("com.universalweather.app.ui.settings.background")
    object Location : SettingsScreenRouter("com.universalweather.app.ui.settings.location")
    object WeatherProviders : SettingsScreenRouter("com.universalweather.app.ui.settings.providers")
    object Appearance : SettingsScreenRouter("com.universalweather.app.ui.settings.appearance")
    object MainScreen : SettingsScreenRouter("com.universalweather.app.ui.settings.main")
    object Notifications : SettingsScreenRouter("com.universalweather.app.ui.settings.notifications")
    object Unit : SettingsScreenRouter("com.universalweather.app.ui.settings.unit")
    object Widgets : SettingsScreenRouter("com.universalweather.app.ui.settings.widgets")
    object Debug : SettingsScreenRouter("com.universalweather.app.ui.settings.debug")
}
