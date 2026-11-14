/*
 * This file is part of Universal Weather.
 *
 * Universal Weather is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License.
 *
 * Universal Weather is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Universal Weather. If not, see <https://www.gnu.org/licenses/>.
 */

package com.universalweather.app.background.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.universalweather.app.data.location.LocationRepository
import com.universalweather.app.data.weather.WeatherRepository
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.source.SourceFeature
import com.universalweather.app.domain.weather.model.AirQuality
import com.universalweather.app.domain.weather.model.Alert
import com.universalweather.app.domain.weather.model.Current
import com.universalweather.app.domain.weather.model.Daily
import com.universalweather.app.domain.weather.model.HalfDay
import com.universalweather.app.domain.weather.model.Hourly
import com.universalweather.app.domain.weather.model.Minutely
import com.universalweather.app.domain.weather.model.Normals
import com.universalweather.app.domain.weather.model.Pollen
import com.universalweather.app.domain.weather.model.PrecipitationDuration
import com.universalweather.app.domain.weather.model.PrecipitationProbability
import com.universalweather.app.domain.weather.model.UV
import com.universalweather.app.domain.weather.model.Wind
import com.universalweather.app.domain.weather.reference.Month
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import com.universalweather.app.BuildConfig
import com.universalweather.app.R
import com.universalweather.app.common.extensions.getBeaufortScaleColor
import com.universalweather.app.common.extensions.getBeaufortScaleStrength
import com.universalweather.app.common.extensions.getCloudCoverDescription
import com.universalweather.app.common.extensions.getVisibilityDescription
import com.universalweather.app.common.extensions.gzipCompress
import com.universalweather.app.common.extensions.roundDecimals
import com.universalweather.app.common.source.HttpSource
import com.universalweather.app.common.source.PollenIndexSource
import com.universalweather.app.datasharing.json.UniversalAirQuality
import com.universalweather.app.datasharing.json.UniversalAlert
import com.universalweather.app.datasharing.json.UniversalBulletin
import com.universalweather.app.datasharing.json.UniversalCurrent
import com.universalweather.app.datasharing.json.UniversalDaily
import com.universalweather.app.datasharing.json.UniversalDailyUnit
import com.universalweather.app.datasharing.json.UniversalDegreeDay
import com.universalweather.app.datasharing.json.UniversalHalfDay
import com.universalweather.app.datasharing.json.UniversalHourly
import com.universalweather.app.datasharing.json.UniversalMinutely
import com.universalweather.app.datasharing.json.UniversalNormals
import com.universalweather.app.datasharing.json.UniversalPollen
import com.universalweather.app.datasharing.json.UniversalPollenLevel
import com.universalweather.app.datasharing.json.UniversalPollutant
import com.universalweather.app.datasharing.json.UniversalPrecipitation
import com.universalweather.app.datasharing.json.UniversalPrecipitationDuration
import com.universalweather.app.datasharing.json.UniversalPrecipitationProbability
import com.universalweather.app.datasharing.json.UniversalSource
import com.universalweather.app.datasharing.json.UniversalTemperature
import com.universalweather.app.datasharing.json.UniversalUnit
import com.universalweather.app.datasharing.json.UniversalWeather
import com.universalweather.app.datasharing.json.UniversalWind
import com.universalweather.app.datasharing.provider.ProviderLocation
import com.universalweather.app.datasharing.provider.ProviderUri
import com.universalweather.app.datasharing.provider.ProviderVersion
import com.universalweather.app.domain.settings.SettingsManager
import com.universalweather.app.domain.source.resourceName
import com.universalweather.app.domain.weather.index.PollutantIndex
import com.universalweather.app.domain.weather.model.getColor
import com.universalweather.app.domain.weather.model.getConcentration
import com.universalweather.app.domain.weather.model.getIndex
import com.universalweather.app.domain.weather.model.getIndexName
import com.universalweather.app.domain.weather.model.getLevel
import com.universalweather.app.domain.weather.model.getMinutelyDescription
import com.universalweather.app.domain.weather.model.getMinutelyTitle
import com.universalweather.app.domain.weather.model.getName
import com.universalweather.app.domain.weather.model.getUVColor
import com.universalweather.app.domain.weather.model.validPollens
import com.universalweather.app.domain.weather.model.validPollutants
import com.universalweather.app.sources.SourceManager
import com.universalweather.app.unit.distance.Distance
import com.universalweather.app.unit.distance.DistanceUnit
import com.universalweather.app.unit.pollen.PollenConcentrationUnit
import com.universalweather.app.unit.precipitation.Precipitation
import com.universalweather.app.unit.precipitation.PrecipitationUnit
import com.universalweather.app.unit.pressure.Pressure
import com.universalweather.app.unit.pressure.PressureUnit
import com.universalweather.app.unit.ratio.Ratio
import com.universalweather.app.unit.speed.Speed
import com.universalweather.app.unit.speed.SpeedUnit
import com.universalweather.app.unit.temperature.Temperature
import com.universalweather.app.unit.temperature.TemperatureUnit
import kotlin.time.Duration

class WeatherContentProvider : ContentProvider() {

    private val hexFormat: HexFormat = HexFormat {
        upperCase = false
        number {
            prefix = "#"
            minLength = 6
            removeLeadingZeros = true
        }
    }

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface LocationRepositoryContentProviderEntryPoint {
        fun locationRepository(): LocationRepository
    }

    private fun getLocationRepository(appContext: Context): LocationRepository {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            LocationRepositoryContentProviderEntryPoint::class.java
        )
        return hiltEntryPoint.locationRepository()
    }

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface WeatherRepositoryContentProviderEntryPoint {
        fun weatherRepository(): WeatherRepository
    }

    private fun getWeatherRepository(appContext: Context): WeatherRepository {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            WeatherRepositoryContentProviderEntryPoint::class.java
        )
        return hiltEntryPoint.weatherRepository()
    }

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface SourceManagerContentProviderEntryPoint {
        fun sourceManager(): SourceManager
    }

    private fun getSourceManager(appContext: Context): SourceManager {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            SourceManagerContentProviderEntryPoint::class.java
        )
        return hiltEntryPoint.sourceManager()
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
    ): Cursor? {
        if (!isAllowedPackage()) {
            Log.w(TAG, "Content provider is disabled for this app")
            return null
        }

        return when (uriMatcher.match(uri)) {
            ProviderUri.VERSION_CODE -> queryVersion()
            ProviderUri.LOCATIONS_CODE -> queryLocations(
                limit = uri.getQueryParameter("limit")?.toIntOrNull()
            )
            ProviderUri.WEATHER_CODE -> queryWeather(
                selection = selection,
                withDailyQuery = uri.getQueryParameter("withDaily"),
                withHourlyQuery = uri.getQueryParameter("withHourly"),
                withMinutelyQuery = uri.getQueryParameter("withMinutely"),
                withAlertsQuery = uri.getQueryParameter("withAlerts"),
                withNormalsQuery = uri.getQueryParameter("withNormals"),
                temperatureUnitQuery = uri.getQueryParameter("temperatureUnit"),
                precipitationUnitQuery = uri.getQueryParameter("precipitationUnit"),
                speedUnitQuery = uri.getQueryParameter("speedUnit"),
                distanceUnitQuery = uri.getQueryParameter("distanceUnit"),
                pressureUnitQuery = uri.getQueryParameter("pressureUnit")
            )
            else -> {
                Log.w(TAG, "Unrecognized URI $uri")
                null
            }
        }
    }

    private fun isAllowedPackage(): Boolean {
        // Can check callingPackage here
        return true
    }

    private fun queryVersion(): Cursor {
        val columns = arrayOf(ProviderVersion.COLUMN_MAJOR, ProviderVersion.COLUMN_MINOR)
        val matrixCursor = MatrixCursor(columns).apply {
            addRow(arrayOf(ProviderVersion.MAJOR, ProviderVersion.MINOR))
        }
        return matrixCursor
    }

    private fun queryLocations(
        limit: Int?,
    ): Cursor {
        val columns = arrayOf(
            ProviderLocation.COLUMN_ID,
            ProviderLocation.COLUMN_LATITUDE,
            ProviderLocation.COLUMN_LONGITUDE,
            ProviderLocation.COLUMN_IS_CURRENT_POSITION,
            ProviderLocation.COLUMN_TIMEZONE,
            ProviderLocation.COLUMN_CUSTOM_NAME,
            ProviderLocation.COLUMN_COUNTRY,
            ProviderLocation.COLUMN_COUNTRY_CODE,
            ProviderLocation.COLUMN_ADMIN1,
            ProviderLocation.COLUMN_ADMIN1_CODE,
            ProviderLocation.COLUMN_ADMIN2,
            ProviderLocation.COLUMN_ADMIN2_CODE,
            ProviderLocation.COLUMN_ADMIN3,
            ProviderLocation.COLUMN_ADMIN3_CODE,
            ProviderLocation.COLUMN_ADMIN4,
            ProviderLocation.COLUMN_ADMIN4_CODE,
            ProviderLocation.COLUMN_CITY,
            ProviderLocation.COLUMN_DISTRICT,
            ProviderLocation.COLUMN_WEATHER
        )
        val locations = runBlocking {
            if (limit != null && limit > 0) {
                getLocationRepository(context!!).getXLocations(limit, withParameters = false)
            } else {
                getLocationRepository(context!!).getAllLocations(withParameters = false)
            }
        }
        val matrixCursor = MatrixCursor(columns).apply {
            locations.forEach { location ->
                addRow(
                    arrayOf<Any?>(
                        location.formattedId,
                        location.latitude,
                        location.longitude,
                        if (location.isCurrentPosition) 1 else 0,
                        location.timeZone.id,
                        location.customName,
                        location.country,
                        location.countryCode,
                        location.admin1,
                        location.admin1Code,
                        location.admin2,
                        location.admin2Code,
                        location.admin3,
                        location.admin3Code,
                        location.admin4,
                        location.admin4Code,
                        location.city,
                        location.district,
                        null
                    )
                )
            }
        }
        return matrixCursor
    }

    private fun queryWeather(
        selection: String?,
        withDailyQuery: String?,
        withHourlyQuery: String?,
        withMinutelyQuery: String?,
        withAlertsQuery: String?,
        withNormalsQuery: String?,
        temperatureUnitQuery: String?,
        precipitationUnitQuery: String?,
        speedUnitQuery: String?,
        distanceUnitQuery: String?,
        pressureUnitQuery: String?,
    ): Cursor {
        val columns = arrayOf(
            ProviderLocation.COLUMN_ID,
            ProviderLocation.COLUMN_LATITUDE,
            ProviderLocation.COLUMN_LONGITUDE,
            ProviderLocation.COLUMN_IS_CURRENT_POSITION,
            ProviderLocation.COLUMN_TIMEZONE,
            ProviderLocation.COLUMN_CUSTOM_NAME,
            ProviderLocation.COLUMN_COUNTRY,
            ProviderLocation.COLUMN_COUNTRY_CODE,
            ProviderLocation.COLUMN_ADMIN1,
            ProviderLocation.COLUMN_ADMIN1_CODE,
            ProviderLocation.COLUMN_ADMIN2,
            ProviderLocation.COLUMN_ADMIN2_CODE,
            ProviderLocation.COLUMN_ADMIN3,
            ProviderLocation.COLUMN_ADMIN3_CODE,
            ProviderLocation.COLUMN_ADMIN4,
            ProviderLocation.COLUMN_ADMIN4_CODE,
            ProviderLocation.COLUMN_CITY,
            ProviderLocation.COLUMN_DISTRICT,
            ProviderLocation.COLUMN_WEATHER
        )
        val matrixCursor = MatrixCursor(columns)
        if (selection == null) return matrixCursor

        val regex = Regex("id *= *(-?[0-9.]+)&(-?[0-9.]+)&([a-z]+)")
        val matching = regex.find(selection)
        val locationId = if (matching == null) {
            val regex2 = Regex("id *= *${Location.CURRENT_POSITION_ID}")
            val matching2 = regex2.find(selection)
            if (matching2 == null) {
                return matrixCursor
            }
            Location.CURRENT_POSITION_ID
        } else {
            "${matching.groups[1]!!.value}&${matching.groups[2]!!.value}&${matching.groups[3]!!.value}"
        }

        val location = runBlocking {
            getLocationRepository(context!!)
                .getLocation(locationId, withParameters = false)
                ?.copy(
                    weather = getWeatherRepository(context!!)
                        .getWeatherByLocationId(
                            locationId,
                            withDaily = !withDailyQuery.equals("false", ignoreCase = true),
                            withHourly = !withHourlyQuery.equals("false", ignoreCase = true),
                            withMinutely = !withMinutelyQuery.equals("false", ignoreCase = true),
                            withAlerts = !withAlertsQuery.equals("false", ignoreCase = true),
                            withNormals = !withNormalsQuery.equals("false", ignoreCase = true)
                        )
                )
        }
        if (location != null) {
            val pollenIndexSource = location.pollenSource?.let {
                getSourceManager(context!!).getPollenIndexSource(it)
            }
            matrixCursor.addRow(
                arrayOf<Any?>(
                    location.formattedId,
                    location.latitude,
                    location.longitude,
                    if (location.isCurrentPosition) 1 else 0,
                    location.timeZone.id,
                    location.customName,
                    location.country,
                    location.countryCode,
                    location.admin1,
                    location.admin1Code,
                    location.admin2,
                    location.admin2Code,
                    location.admin3,
                    location.admin3Code,
                    location.admin4,
                    location.admin4Code,
                    location.city,
                    location.district,
                    Json.encodeToString(
                        getWeatherData(
                            location,
                            pollenIndexSource,
                            temperatureUnitQuery,
                            precipitationUnitQuery,
                            speedUnitQuery,
                            distanceUnitQuery,
                            pressureUnitQuery
                        )
                    ).gzipCompress()
                )
            )
        }
        return matrixCursor
    }

    private fun getWeatherData(
        location: Location,
        pollenIndexSource: PollenIndexSource?,
        temperatureUnitQuery: String?,
        precipitationUnitQuery: String?,
        speedUnitQuery: String?,
        distanceUnitQuery: String?,
        pressureUnitQuery: String?,
    ): UniversalWeather? {
        val settings = SettingsManager.getInstance(context!!)
        val temperatureUnit = temperatureUnitQuery?.let { TemperatureUnit.getUnit(it) }
            ?: settings.getTemperatureUnit(context!!)
        val precipitationUnit = precipitationUnitQuery?.let { PrecipitationUnit.getUnit(it) }
            ?: settings.getPrecipitationUnit(context!!)
        val snowfallUnit = precipitationUnitQuery?.let { PrecipitationUnit.getUnit(it) }
            ?: settings.getPrecipitationUnit(context!!)
        val speedUnit = speedUnitQuery?.let { SpeedUnit.getUnit(it) }
            ?: settings.getSpeedUnit(context!!)
        val distanceUnit = distanceUnitQuery?.let { DistanceUnit.getUnit(it) }
            ?: settings.getDistanceUnit(context!!)
        val pressureUnit = pressureUnitQuery?.let { PressureUnit.getUnit(it) }
            ?: settings.getPressureUnit(context!!)

        return location.weather?.let { weather ->
            UniversalWeather(
                refreshTime = weather.base.refreshTime?.time,
                bulletin = UniversalBulletin(
                    weekly = weather.current?.dailyForecast,
                    nextHours = weather.current?.hourlyForecast,
                    nowcastingHeadline = weather.getMinutelyTitle(context!!),
                    nowcastingDescription = weather.getMinutelyDescription(context!!, location)
                ),
                current = getCurrent(
                    weather.current,
                    temperatureUnit,
                    speedUnit,
                    distanceUnit,
                    pressureUnit
                ),
                daily = getDaily(
                    weather.dailyForecast,
                    temperatureUnit,
                    precipitationUnit,
                    snowfallUnit,
                    speedUnit,
                    distanceUnit,
                    pressureUnit,
                    pollenIndexSource
                ),
                hourly = getHourly(
                    weather.hourlyForecast,
                    temperatureUnit,
                    precipitationUnit,
                    snowfallUnit,
                    speedUnit,
                    distanceUnit,
                    pressureUnit
                ),
                minutely = getMinutely(
                    weather.minutelyForecast,
                    precipitationUnit
                ),
                alerts = getAlerts(
                    weather.alertList
                ),
                normals = getNormals(
                    weather.normals,
                    temperatureUnit
                ),
                sources = getSources(location),
                pollenLevels = pollenIndexSource?.let { pis ->
                    context!!.resources.getStringArray(pis.pollenLabels).map {
                        UniversalPollenLevel(it)
                    }
                } ?: context!!.resources.getStringArray(R.array.pollen_levels).map {
                    UniversalPollenLevel(it)
                }
            )
        }
    }

    private fun getCurrent(
        current: Current?,
        temperatureUnit: TemperatureUnit,
        speedUnit: SpeedUnit,
        distanceUnit: DistanceUnit,
        pressureUnit: PressureUnit,
    ): UniversalCurrent? {
        return current?.let { cur ->
            UniversalCurrent(
                weatherText = cur.weatherText,
                weatherCode = cur.weatherCode?.id,
                temperature = getTemperature(cur.temperature, temperatureUnit),
                wind = getWind(cur.wind, speedUnit),
                uV = getUV(cur.uV),
                airQuality = getAirQuality(cur.airQuality),
                relativeHumidity = getPercentUnit(cur.relativeHumidity),
                dewPoint = getTemperatureUnit(cur.dewPoint, temperatureUnit),
                pressure = getPressureUnit(cur.pressure, pressureUnit),
                cloudCover = getPercentUnit(cur.cloudCover, cur.cloudCover?.getCloudCoverDescription(context!!)),
                visibility = getDistanceUnit(cur.visibility, distanceUnit),
                ceiling = getDistanceUnit(cur.ceiling, distanceUnit)
            )
        }
    }

    private fun getDaily(
        daily: List<Daily>?,
        temperatureUnit: TemperatureUnit,
        precipitationUnit: PrecipitationUnit,
        snowfallUnit: PrecipitationUnit,
        speedUnit: SpeedUnit,
        distanceUnit: DistanceUnit,
        pressureUnit: PressureUnit,
        pollenIndexSource: PollenIndexSource?,
    ): List<UniversalDaily>? {
        return daily?.map { day ->
            UniversalDaily(
                date = day.date.time,
                day = getHalfDay(
                    day.day,
                    temperatureUnit,
                    precipitationUnit,
                    snowfallUnit,
                    speedUnit
                ),
                night = getHalfDay(
                    day.night,
                    temperatureUnit,
                    precipitationUnit,
                    snowfallUnit,
                    speedUnit
                ),
                degreeDay = day.degreeDay?.let {
                    UniversalDegreeDay(
                        heating = getDegreeDayTemperatureUnit(
                            it.heating,
                            temperatureUnit
                        ),
                        cooling = getDegreeDayTemperatureUnit(
                            it.cooling,
                            temperatureUnit
                        )
                    )
                },
                /*sun = getAstro(day.sun),
                twilight = getAstro(day.twilight),
                moon = getAstro(day.moon),
                moonPhase = day.moonPhase?.let {
                    UniversalMoonPhase(
                        angle = it.angle,
                        description = it.getDescription(context!!)
                    )
                },*/
                airQuality = getAirQuality(day.airQuality),
                pollen = getPollen(day.pollen, pollenIndexSource),
                uV = getUV(day.uV),
                sunshineDuration = getDurationUnit(day.sunshineDuration),
                relativeHumidity = UniversalDailyUnit(
                    avg = getPercentUnit(day.relativeHumidity?.average),
                    max = getPercentUnit(day.relativeHumidity?.max),
                    min = getPercentUnit(day.relativeHumidity?.min),
                    summary = null
                ),
                dewPoint = UniversalDailyUnit(
                    avg = getTemperatureUnit(day.dewPoint?.average, temperatureUnit),
                    max = getTemperatureUnit(day.dewPoint?.max, temperatureUnit),
                    min = getTemperatureUnit(day.dewPoint?.min, temperatureUnit),
                    summary = null
                ),
                pressure = UniversalDailyUnit(
                    avg = getPressureUnit(day.pressure?.average, pressureUnit),
                    max = getPressureUnit(day.pressure?.max, pressureUnit),
                    min = getPressureUnit(day.pressure?.min, pressureUnit),
                    summary = null
                ),
                cloudCover = UniversalDailyUnit(
                    avg = getPercentUnit(
                        day.cloudCover?.average,
                        day.cloudCover?.average?.getCloudCoverDescription(context!!)
                    ),
                    max = getPercentUnit(
                        day.cloudCover?.max,
                        day.cloudCover?.max?.getCloudCoverDescription(context!!)
                    ),
                    min = getPercentUnit(
                        day.cloudCover?.min,
                        day.cloudCover?.min?.getCloudCoverDescription(context!!)
                    ),
                    summary = null
                ),
                visibility = UniversalDailyUnit(
                    avg = getDistanceUnit(day.visibility?.average, distanceUnit),
                    max = getDistanceUnit(day.visibility?.max, distanceUnit),
                    min = getDistanceUnit(day.visibility?.min, distanceUnit),
                    summary = null
                )
            )
        }
    }

    private fun getHourly(
        hourly: List<Hourly>?,
        temperatureUnit: TemperatureUnit,
        precipitationUnit: PrecipitationUnit,
        snowfallUnit: PrecipitationUnit,
        speedUnit: SpeedUnit,
        distanceUnit: DistanceUnit,
        pressureUnit: PressureUnit,
    ): List<UniversalHourly>? {
        return hourly?.map { hour ->
            UniversalHourly(
                date = hour.date.time,
                isDaylight = hour.isDaylight,
                weatherText = hour.weatherText,
                weatherCode = hour.weatherCode?.id,
                temperature = getTemperature(hour.temperature, temperatureUnit),
                precipitation = getPrecipitation(hour.precipitation, precipitationUnit, snowfallUnit),
                precipitationProbability = getPrecipitationProbability(hour.precipitationProbability),
                wind = getWind(hour.wind, speedUnit),
                airQuality = getAirQuality(hour.airQuality),
                uV = getUV(hour.uV),
                relativeHumidity = getPercentUnit(hour.relativeHumidity),
                dewPoint = getTemperatureUnit(hour.dewPoint, temperatureUnit),
                pressure = getPressureUnit(hour.pressure, pressureUnit),
                cloudCover = getPercentUnit(hour.cloudCover, hour.cloudCover?.getCloudCoverDescription(context!!)),
                visibility = getDistanceUnit(hour.visibility, distanceUnit)
            )
        }
    }

    private fun getMinutely(
        minutely: List<Minutely>?,
        precipitationUnit: PrecipitationUnit,
    ): List<UniversalMinutely>? {
        return minutely?.map { minute ->
            UniversalMinutely(
                date = minute.date.time,
                minuteInterval = minute.minuteInterval,
                precipitationIntensity = getPrecipitationUnit(
                    minute.precipitationIntensity,
                    precipitationUnit
                )
            )
        }
    }

    private fun getNormals(
        normals: Map<Month, Normals>?,
        temperatureUnit: TemperatureUnit,
    ): Map<Int, UniversalNormals>? {
        return normals?.entries?.associate {
            it.key.value to UniversalNormals(
                daytimeTemperature = getTemperatureUnit(it.value.daytimeTemperature, temperatureUnit),
                nighttimeTemperature = getTemperatureUnit(it.value.nighttimeTemperature, temperatureUnit)
            )
        }
    }

    private fun getSources(
        location: Location,
    ): Map<String, UniversalSource>? {
        return mapOf(
            SourceFeature.FORECAST to location.forecastSource,
            SourceFeature.CURRENT to location.currentSource,
            SourceFeature.AIR_QUALITY to location.airQualitySource,
            SourceFeature.POLLEN to location.pollenSource,
            SourceFeature.MINUTELY to location.minutelySource,
            SourceFeature.ALERT to location.alertSource,
            SourceFeature.NORMALS to location.normalsSource,
            SourceFeature.REVERSE_GEOCODING to location.reverseGeocodingSource
        ).filter { !it.value.isNullOrEmpty() }.mapNotNull {
            getSourceManager(context!!).getFeatureSource(it.value!!)?.let { source ->
                if (source.supportedFeatures.containsKey(it.key)) {
                    it.key.id to UniversalSource(
                        type = context!!.getString(it.key.resourceName),
                        text = source.supportedFeatures[it.key],
                        links = if (source is HttpSource) {
                            source.attributionLinks.filter { link ->
                                source.supportedFeatures[it.key]?.contains(link.key) == true
                            }
                        } else {
                            null
                        }
                    )
                } else {
                    null
                }
            }
        }.toMap()
    }

    /*private fun getAstro(astro: Astro?): UniversalAstro? {
        return astro?.let {
            UniversalAstro(
                riseDate = it.riseDate?.time,
                setDate = it.setDate?.time
            )
        }
    }*/

    private fun getHalfDay(
        halfDay: HalfDay?,
        temperatureUnit: TemperatureUnit,
        precipitationUnit: PrecipitationUnit,
        snowfallUnit: PrecipitationUnit,
        speedUnit: SpeedUnit,
    ): UniversalHalfDay? {
        return halfDay?.let { hd ->
            UniversalHalfDay(
                weatherCode = hd.weatherCode?.id,
                weatherText = hd.weatherText,
                weatherSummary = hd.weatherSummary,
                temperature = getTemperature(hd.temperature, temperatureUnit),
                precipitation = getPrecipitation(hd.precipitation, precipitationUnit, snowfallUnit),
                precipitationProbability = getPrecipitationProbability(hd.precipitationProbability),
                precipitationDuration = getPrecipitationDuration(hd.precipitationDuration),
                wind = getWind(hd.wind, speedUnit)
            )
        }
    }

    private fun getTemperature(
        temperature: com.universalweather.app.domain.weather.model.Temperature?,
        temperatureUnit: TemperatureUnit,
    ): UniversalTemperature? {
        return temperature?.let {
            UniversalTemperature(
                temperature = getTemperatureUnit(it.temperature, temperatureUnit),
                sourceFeelsLike = getTemperatureUnit(it.sourceFeelsLike, temperatureUnit),
                computedApparent = getTemperatureUnit(it.computedApparent, temperatureUnit),
                computedWindChill = getTemperatureUnit(it.computedWindChill, temperatureUnit),
                computedHumidex = getTemperatureUnit(it.computedHumidex, temperatureUnit)
            )
        }
    }

    private fun getTemperatureUnit(
        temperature: Temperature?,
        temperatureUnit: TemperatureUnit,
    ): UniversalUnit? {
        return temperature?.let {
            UniversalUnit(
                value = it.toDouble(temperatureUnit).roundDecimals(temperatureUnit.decimals.long),
                unit = temperatureUnit.id
            )
        }
    }

    private fun getDegreeDayTemperatureUnit(
        temperature: Temperature?,
        temperatureUnit: TemperatureUnit,
    ): UniversalUnit? {
        return temperature?.let {
            UniversalUnit(
                value = it.toDoubleDeviation(temperatureUnit).roundDecimals(temperatureUnit.decimals.long),
                unit = temperatureUnit.id
            )
        }
    }

    private fun getPrecipitation(
        precipitation: com.universalweather.app.domain.weather.model.Precipitation?,
        precipitationUnit: PrecipitationUnit,
        snowfallUnit: PrecipitationUnit,
    ): UniversalPrecipitation? {
        return precipitation?.let {
            UniversalPrecipitation(
                total = getPrecipitationUnit(it.total, precipitationUnit),
                thunderstorm = getPrecipitationUnit(it.thunderstorm, precipitationUnit),
                rain = getPrecipitationUnit(it.rain, precipitationUnit),
                snow = getPrecipitationUnit(it.snow, snowfallUnit),
                ice = getPrecipitationUnit(it.ice, precipitationUnit)
            )
        }
    }

    private fun getPrecipitationUnit(
        precipitation: Precipitation?,
        precipitationUnit: PrecipitationUnit,
    ): UniversalUnit? {
        return precipitation?.let {
            UniversalUnit(
                value = it.toDouble(precipitationUnit).roundDecimals(precipitationUnit.decimals.long),
                unit = precipitationUnit.id
            )
        }
    }

    private fun getPrecipitationProbability(
        precipitationProbability: PrecipitationProbability?,
    ): UniversalPrecipitationProbability? {
        return precipitationProbability?.let {
            UniversalPrecipitationProbability(
                total = getPercentUnit(it.total),
                thunderstorm = getPercentUnit(it.thunderstorm),
                rain = getPercentUnit(it.rain),
                snow = getPercentUnit(it.snow),
                ice = getPercentUnit(it.ice)
            )
        }
    }

    private fun getPrecipitationDuration(
        precipitationDuration: PrecipitationDuration?,
    ): UniversalPrecipitationDuration? {
        return precipitationDuration?.let {
            UniversalPrecipitationDuration(
                total = getDurationUnit(it.total),
                thunderstorm = getDurationUnit(it.thunderstorm),
                rain = getDurationUnit(it.rain),
                snow = getDurationUnit(it.snow),
                ice = getDurationUnit(it.ice)
            )
        }
    }

    private fun getWind(
        wind: Wind?,
        speedUnit: SpeedUnit,
    ): UniversalWind? {
        return wind?.let {
            UniversalWind(
                degree = wind.degree?.roundDecimals(1),
                speed = getSpeedUnit(wind.speed, speedUnit),
                gusts = getSpeedUnit(wind.gusts, speedUnit)
            )
        }
    }

    private fun getUV(
        uV: UV?,
    ): UniversalUnit? {
        return uV?.index?.let {
            UniversalUnit(
                value = it.roundDecimals(1),
                unit = "uvi",
                description = uV.getLevel(context!!),
                color = colorToHex(uV.getUVColor(context!!))
            )
        }
    }

    private fun getSpeedUnit(
        speed: Speed?,
        speedUnit: SpeedUnit,
    ): UniversalUnit? {
        return speed?.let {
            UniversalUnit(
                value = it.toDouble(speedUnit).roundDecimals(speedUnit.decimals.long),
                unit = speedUnit.id,
                description = it.getBeaufortScaleStrength(context!!),
                color = colorToHex(it.getBeaufortScaleColor(context!!))
            )
        }
    }

    private fun getDistanceUnit(
        distance: Distance?,
        distanceUnit: DistanceUnit,
    ): UniversalUnit? {
        return distance?.let {
            UniversalUnit(
                value = it.toDouble(distanceUnit).roundDecimals(distanceUnit.decimals.long),
                unit = distanceUnit.id,
                description = it.getVisibilityDescription(context!!)
            )
        }
    }

    private fun getPressureUnit(
        pressure: Pressure?,
        pressureUnit: PressureUnit,
    ): UniversalUnit? {
        return pressure?.let {
            UniversalUnit(
                value = it.toDouble(pressureUnit).roundDecimals(pressureUnit.decimals.long),
                unit = pressureUnit.id
            )
        }
    }

    private fun getPercentUnit(
        percent: Ratio?,
        description: String? = null,
    ): UniversalUnit? {
        return percent?.let {
            UniversalUnit(
                value = it.inPercent.roundDecimals(1),
                unit = "percent",
                description = description
            )
        }
    }

    private fun getDurationUnit(
        duration: Duration?,
    ): UniversalUnit? {
        return duration?.let {
            UniversalUnit(
                value = it.inWholeMinutes.toDouble(),
                unit = "m"
            )
        }
    }

    private fun getAirQuality(
        airQuality: AirQuality?,
    ): UniversalAirQuality? {
        return airQuality?.let {
            if (airQuality.isValid) {
                UniversalAirQuality(
                    index = UniversalUnit(
                        value = airQuality.getIndex()?.toDouble(),
                        unit = "aqi",
                        description = airQuality.getName(context!!),
                        color = colorToHex(airQuality.getColor(context!!))
                    ),
                    pollutants = airQuality.validPollutants.associate {
                        it.id to UniversalPollutant(
                            index = UniversalUnit(
                                value = airQuality.getIndex(it)?.toDouble(),
                                unit = "aqi",
                                description = airQuality.getName(context!!, it),
                                color = colorToHex(airQuality.getColor(context!!, it))
                            ),
                            concentration = UniversalUnit(
                                value = airQuality.getConcentration(it)?.roundDecimals(1),
                                unit = PollutantIndex.getUnit(it).id
                            )
                        )
                    }
                )
            } else {
                null
            }
        }
    }

    private fun getPollen(
        pollen: Pollen?,
        pollenIndexSource: PollenIndexSource?,
    ): Map<String, UniversalPollen>? {
        return pollen?.let {
            if (it.isValid) {
                it.validPollens.associate { component ->
                    component.id to UniversalPollen(
                        name = it.getName(context!!, component),
                        concentration = UniversalUnit(
                            value = if (pollenIndexSource == null) {
                                it.getConcentration(component)?.value?.toDouble()
                            } else {
                                null
                            },
                            unit = PollenConcentrationUnit.PER_CUBIC_METER.id,
                            description = it.getIndexName(context!!, component, pollenIndexSource),
                            color = colorToHex(it.getColor(context!!, component, pollenIndexSource))
                        )
                    )
                }
            } else {
                null
            }
        }
    }

    private fun getAlerts(alertList: List<Alert>?): List<UniversalAlert>? {
        return alertList?.map { alert ->
            UniversalAlert(
                alertId = alert.alertId,
                startDate = alert.startDate?.time,
                endDate = alert.endDate?.time,
                headline = alert.headline,
                description = alert.description,
                instruction = alert.instruction,
                source = alert.source,
                severity = alert.severity.id,
                color = colorToHex(alert.color)
            )
        }
    }

    private fun colorToHex(@ColorInt colorInt: Int): String {
        return ColorUtils.setAlphaComponent(colorInt, 0).toHexString(hexFormat)
    }

    override fun getType(uri: Uri): String? {
        // MIME types are not relevant (for now at least)
        return null
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?,
    ): Uri? {
        // This content provider is read-only for now, so we always return null
        return null
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?,
    ): Int {
        // This content provider is read-only for now, so we always return 0
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?,
    ): Int {
        // This content provider is read-only for now, so we always return 0
        return 0
    }

    companion object {
        private const val TAG = "Universal"

        const val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.weather"

        private val uriMatcher = object : UriMatcher(NO_MATCH) {
            init {
                addURI(AUTHORITY, ProviderUri.VERSION_PATH, ProviderUri.VERSION_CODE)
                addURI(AUTHORITY, ProviderUri.LOCATIONS_PATH, ProviderUri.LOCATIONS_CODE)
                addURI(AUTHORITY, ProviderUri.WEATHER_PATH, ProviderUri.WEATHER_CODE)
            }
        }
    }
}
