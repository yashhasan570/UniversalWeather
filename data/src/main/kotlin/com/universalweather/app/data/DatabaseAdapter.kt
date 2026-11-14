/*
 * This file is part of Universal Weather.
 *
 * Universal Weather is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
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

import app.cash.sqldelight.ColumnAdapter
import com.universalweather.app.domain.weather.reference.AlertSeverity
import com.universalweather.app.domain.weather.reference.WeatherCode
import com.universalweather.app.unit.distance.Distance
import com.universalweather.app.unit.distance.Distance.Companion.meters
import com.universalweather.app.unit.pollen.PollenConcentration
import com.universalweather.app.unit.pollen.PollenConcentration.Companion.perCubicMeter
import com.universalweather.app.unit.pollutant.PollutantConcentration
import com.universalweather.app.unit.pollutant.PollutantConcentration.Companion.microgramsPerCubicMeter
import com.universalweather.app.unit.precipitation.Precipitation
import com.universalweather.app.unit.precipitation.Precipitation.Companion.micrometers
import com.universalweather.app.unit.pressure.Pressure
import com.universalweather.app.unit.pressure.Pressure.Companion.pascals
import com.universalweather.app.unit.ratio.Ratio
import com.universalweather.app.unit.ratio.Ratio.Companion.permille
import com.universalweather.app.unit.speed.Speed
import com.universalweather.app.unit.speed.Speed.Companion.centimetersPerSecond
import com.universalweather.app.unit.temperature.Temperature
import com.universalweather.app.unit.temperature.Temperature.Companion.deciCelsius
import java.util.Date
import java.util.TimeZone
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

object DateColumnAdapter : ColumnAdapter<Date, Long> {
    override fun decode(databaseValue: Long): Date = Date(databaseValue)
    override fun encode(value: Date): Long = value.time
}

private const val LIST_OF_STRINGS_SEPARATOR = ", "
object StringListColumnAdapter : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String) = if (databaseValue.isEmpty()) {
        emptyList()
    } else {
        databaseValue.split(LIST_OF_STRINGS_SEPARATOR)
    }
    override fun encode(value: List<String>) = value.joinToString(
        separator = LIST_OF_STRINGS_SEPARATOR
    )
}

object TimeZoneColumnAdapter : ColumnAdapter<TimeZone, String> {
    override fun decode(databaseValue: String): TimeZone = TimeZone.getTimeZone(databaseValue)

    override fun encode(value: TimeZone): String = value.id
}

object WeatherCodeColumnAdapter : ColumnAdapter<WeatherCode, String> {
    override fun decode(databaseValue: String): WeatherCode =
        WeatherCode.getInstance(databaseValue) ?: WeatherCode.CLEAR

    override fun encode(value: WeatherCode): String = value.id
}

object AlertSeverityColumnAdapter : ColumnAdapter<AlertSeverity, Long> {
    override fun decode(databaseValue: Long): AlertSeverity = AlertSeverity.getInstance(databaseValue.toInt())

    override fun encode(value: AlertSeverity): Long = value.id.toLong()
}

object TemperatureColumnAdapter : ColumnAdapter<Temperature, Long> {
    override fun decode(databaseValue: Long): Temperature = databaseValue.deciCelsius

    override fun encode(value: Temperature): Long = value.value
}

object PrecipitationColumnAdapter : ColumnAdapter<Precipitation, Long> {
    override fun decode(databaseValue: Long): Precipitation = databaseValue.micrometers

    override fun encode(value: Precipitation): Long = value.value
}

object SpeedColumnAdapter : ColumnAdapter<Speed, Long> {
    override fun decode(databaseValue: Long): Speed = databaseValue.centimetersPerSecond

    override fun encode(value: Speed): Long = value.value
}

object DistanceColumnAdapter : ColumnAdapter<Distance, Long> {
    override fun decode(databaseValue: Long): Distance = databaseValue.meters

    override fun encode(value: Distance): Long = value.value
}

object PressureColumnAdapter : ColumnAdapter<Pressure, Long> {
    override fun decode(databaseValue: Long): Pressure = databaseValue.pascals

    override fun encode(value: Pressure): Long = value.value
}

object PollutantConcentrationColumnAdapter : ColumnAdapter<PollutantConcentration, Long> {
    override fun decode(databaseValue: Long): PollutantConcentration = databaseValue.microgramsPerCubicMeter

    override fun encode(value: PollutantConcentration): Long = value.value
}

object PollenConcentrationColumnAdapter : ColumnAdapter<PollenConcentration, Long> {
    override fun decode(databaseValue: Long): PollenConcentration = databaseValue.perCubicMeter

    override fun encode(value: PollenConcentration): Long = value.value
}

object DurationColumnAdapter : ColumnAdapter<Duration, Long> {
    override fun decode(databaseValue: Long): Duration = databaseValue.nanoseconds

    override fun encode(value: Duration): Long = value.inWholeNanoseconds
}

object RatioColumnAdapter : ColumnAdapter<Ratio, Long> {
    override fun decode(databaseValue: Long): Ratio = databaseValue.permille

    override fun encode(value: Ratio): Long = value.value
}
