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

package com.universalweather.app.sources

import android.content.Context
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.domain.source.SourceFeature
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import com.universalweather.app.UniversalWeather
import com.universalweather.app.BuildConfig
import com.universalweather.app.common.extensions.currentLocale
import com.universalweather.app.common.source.BroadcastSource
import com.universalweather.app.common.source.ConfigurableSource
import com.universalweather.app.common.source.FeatureSource
import com.universalweather.app.common.source.HttpSource
import com.universalweather.app.common.source.LocationSearchSource
import com.universalweather.app.common.source.LocationSource
import com.universalweather.app.common.source.NonFreeNetSource
import com.universalweather.app.common.source.PollenIndexSource
import com.universalweather.app.common.source.PreferencesParametersSource
import com.universalweather.app.common.source.RemovedSource
import com.universalweather.app.common.source.ReverseGeocodingSource
import com.universalweather.app.common.source.Source
import com.universalweather.app.common.source.TimeZoneSource
import com.universalweather.app.common.source.WeatherSource
import com.universalweather.app.common.source.WeatherSource.Companion.PRIORITY_NONE
import com.universalweather.app.domain.settings.SourceConfigStore
import com.universalweather.app.sources.accu.AccuService
import com.universalweather.app.sources.aemet.AemetService
import com.universalweather.app.sources.android.AndroidGeocoderService
import com.universalweather.app.sources.android.AndroidLocationService
import com.universalweather.app.sources.atmo.AtmoAuraService
import com.universalweather.app.sources.atmo.AtmoFranceService
import com.universalweather.app.sources.atmo.AtmoGrandEstService
import com.universalweather.app.sources.atmo.AtmoHdfService
import com.universalweather.app.sources.atmo.AtmoSudService
import com.universalweather.app.sources.baiduip.BaiduIPLocationService
import com.universalweather.app.sources.bmd.BmdService
import com.universalweather.app.sources.bmkg.BmkgService
import com.universalweather.app.sources.breezytz.UniversalTimeZoneService
import com.universalweather.app.sources.breezyupdatenotifier.UniversalUpdateNotifierService
import com.universalweather.app.sources.brightsky.BrightSkyService
import com.universalweather.app.sources.china.ChinaService
import com.universalweather.app.sources.climweb.AnamBfService
import com.universalweather.app.sources.climweb.AnametService
import com.universalweather.app.sources.climweb.DccmsService
import com.universalweather.app.sources.climweb.DmnNeService
import com.universalweather.app.sources.climweb.DwrGmService
import com.universalweather.app.sources.climweb.EthioMetService
import com.universalweather.app.sources.climweb.GMetService
import com.universalweather.app.sources.climweb.IgebuService
import com.universalweather.app.sources.climweb.InmgbService
import com.universalweather.app.sources.climweb.MaliMeteoService
import com.universalweather.app.sources.climweb.MeteoBeninService
import com.universalweather.app.sources.climweb.MeteoTchadService
import com.universalweather.app.sources.climweb.MettelsatService
import com.universalweather.app.sources.climweb.MsdZwService
import com.universalweather.app.sources.climweb.SmaScService
import com.universalweather.app.sources.climweb.SmaSuService
import com.universalweather.app.sources.climweb.SsmsService
import com.universalweather.app.sources.cwa.CwaService
import com.universalweather.app.sources.cydom.CyDoMService
import com.universalweather.app.sources.debug.DebugService
import com.universalweather.app.sources.dmi.DmiService
import com.universalweather.app.sources.eccc.EcccService
import com.universalweather.app.sources.ekuk.EkukService
import com.universalweather.app.sources.epdhk.EpdHkService
import com.universalweather.app.sources.fmi.FmiService
import com.universalweather.app.sources.fpas.FpasService
import com.universalweather.app.sources.gadgetbridge.GadgetbridgeService
import com.universalweather.app.sources.geonames.GeoNamesService
import com.universalweather.app.sources.geosphereat.GeoSphereAtService
import com.universalweather.app.sources.hko.HkoService
import com.universalweather.app.sources.ilmateenistus.IlmateenistusService
import com.universalweather.app.sources.imd.ImdService
import com.universalweather.app.sources.ims.ImsService
import com.universalweather.app.sources.infoplaza.InfoplazaService
import com.universalweather.app.sources.ipma.IpmaService
import com.universalweather.app.sources.ipsb.IpSbLocationService
import com.universalweather.app.sources.jma.JmaService
import com.universalweather.app.sources.knmi.KnmiService
import com.universalweather.app.sources.lhmt.LhmtService
import com.universalweather.app.sources.lvgmc.LvgmcService
import com.universalweather.app.sources.meteoam.MeteoAmService
import com.universalweather.app.sources.meteolux.MeteoLuxService
import com.universalweather.app.sources.metie.MetIeService
import com.universalweather.app.sources.metno.MetNoService
import com.universalweather.app.sources.metoffice.MetOfficeService
import com.universalweather.app.sources.mf.MfService
import com.universalweather.app.sources.mgm.MgmService
import com.universalweather.app.sources.namem.NamemService
import com.universalweather.app.sources.naturalearth.NaturalEarthService
import com.universalweather.app.sources.ncdr.NcdrService
import com.universalweather.app.sources.ncei.NceiService
import com.universalweather.app.sources.nlsc.NlscService
import com.universalweather.app.sources.nominatim.NominatimService
import com.universalweather.app.sources.nws.NwsService
import com.universalweather.app.sources.openmeteo.OpenMeteoService
import com.universalweather.app.sources.openweather.OpenWeatherService
import com.universalweather.app.sources.pagasa.PagasaService
import com.universalweather.app.sources.pirateweather.PirateWeatherService
import com.universalweather.app.sources.polleninfo.PollenInfoService
import com.universalweather.app.sources.recosante.RecosanteService
import com.universalweather.app.sources.smg.SmgService
import com.universalweather.app.sources.smhi.SmhiService
import com.universalweather.app.sources.veduris.VedurIsService
import com.universalweather.app.sources.wmosevereweather.WmoSevereWeatherService
import java.text.Collator
import javax.inject.Inject

class SourceManager @Inject constructor(
    @ApplicationContext context: Context,
    accuService: AccuService,
    aemetService: AemetService,
    anamBfService: AnamBfService,
    anametService: AnametService,
    androidGeocoderService: AndroidGeocoderService,
    androidLocationService: AndroidLocationService,
    atmoAuraService: AtmoAuraService,
    atmoFranceService: AtmoFranceService,
    atmoGrandEstService: AtmoGrandEstService,
    atmoHdfService: AtmoHdfService,
    atmoSudService: AtmoSudService,
    baiduIPService: BaiduIPLocationService,
    bmdService: BmdService,
    bmkgService: BmkgService,
    breezyTimeZoneService: UniversalTimeZoneService,
    breezyUpdateNotifierService: UniversalUpdateNotifierService,
    brightSkyService: BrightSkyService,
    chinaService: ChinaService,
    cwaService: CwaService,
    cyDoMService: CyDoMService,
    dccmsService: DccmsService,
    debugService: DebugService,
    dmnNeService: DmnNeService,
    dmiService: DmiService,
    dwrGmService: DwrGmService,
    ecccService: EcccService,
    ekukService: EkukService,
    epdHkService: EpdHkService,
    ethioMetService: EthioMetService,
    fmiService: FmiService,
    fpasService: FpasService,
    gadgetbridgeService: GadgetbridgeService,
    geoNamesService: GeoNamesService,
    geoSphereAtService: GeoSphereAtService,
    gMetService: GMetService,
    hkoService: HkoService,
    igebuService: IgebuService,
    ilmateenistusService: IlmateenistusService,
    imdService: ImdService,
    imsService: ImsService,
    infoplazaService: InfoplazaService,
    inmgbService: InmgbService,
    ipmaService: IpmaService,
    ipSbService: IpSbLocationService,
    jmaService: JmaService,
    knmiService: KnmiService,
    lhmtService: LhmtService,
    lvgmcService: LvgmcService,
    maliMeteoService: MaliMeteoService,
    meteoAmService: MeteoAmService,
    meteoBeninService: MeteoBeninService,
    meteoLuxService: MeteoLuxService,
    meteoTchadService: MeteoTchadService,
    metIeService: MetIeService,
    metNoService: MetNoService,
    metOfficeService: MetOfficeService,
    mettelsatService: MettelsatService,
    mfService: MfService,
    mgmService: MgmService,
    msdZwService: MsdZwService,
    namemService: NamemService,
    naturalEarthService: NaturalEarthService,
    ncdrService: NcdrService,
    nceiService: NceiService,
    nlscService: NlscService,
    nominatimService: NominatimService,
    nwsService: NwsService,
    openMeteoService: OpenMeteoService,
    openWeatherService: OpenWeatherService,
    pagasaService: PagasaService,
    pirateWeatherService: PirateWeatherService,
    pollenInfoService: PollenInfoService,
    recosanteService: RecosanteService,
    smaScService: SmaScService,
    smaSuService: SmaSuService,
    smgService: SmgService,
    smhiService: SmhiService,
    ssmsService: SsmsService,
    vedurIsService: VedurIsService,
    wmoSevereWeatherService: WmoSevereWeatherService,
) {
    // Location sources
    private val locationSourceList = persistentListOf(
        androidLocationService,
        ipSbService,
        baiduIPService
    )

    // Location search sources
    private val locationSearchSourceList = persistentListOf(
        geoNamesService
    )

    // Reverse geocoding sources
    private val reverseGeocodingSourceList = persistentListOf(
        naturalEarthService,
        androidGeocoderService,
        nominatimService
    )

    // Worldwide weather sources, excluding national sources with worldwide support,
    // with the exception of MET Norway
    private val worldwideWeatherSourceList = persistentListOf(
        openMeteoService,
        accuService,
        fpasService,
        infoplazaService,
        metNoService,
        nceiService,
        openWeatherService,
        pirateWeatherService,
        wmoSevereWeatherService
    )

    // Region-specific or national weather sources
    private val nationalWeatherSourceList = persistentListOf(
        aemetService,
        anamBfService,
        anametService,
        atmoAuraService,
        atmoFranceService,
        atmoGrandEstService,
        atmoHdfService,
        atmoSudService,
        bmdService,
        bmkgService,
        brightSkyService,
        chinaService,
        cwaService,
        cyDoMService,
        dccmsService,
        dmiService,
        dmnNeService,
        dwrGmService,
        ecccService,
        ekukService,
        epdHkService,
        ethioMetService,
        fmiService,
        geoSphereAtService,
        gMetService,
        hkoService,
        igebuService,
        ilmateenistusService,
        imdService,
        imsService,
        inmgbService,
        ipmaService,
        jmaService,
        knmiService,
        lhmtService,
        lvgmcService,
        maliMeteoService,
        meteoAmService,
        meteoBeninService,
        meteoLuxService,
        meteoTchadService,
        metIeService,
        metOfficeService,
        mettelsatService,
        mfService,
        mgmService,
        msdZwService,
        namemService,
        ncdrService,
        nlscService,
        nwsService,
        pollenInfoService,
        pagasaService,
        recosanteService,
        smaScService,
        smaSuService,
        smgService,
        smhiService,
        ssmsService,
        vedurIsService
    )

    // Broadcast sources
    private val broadcastSourceList = persistentListOf(
        breezyUpdateNotifierService,
        gadgetbridgeService
    )

    private val timeZoneSource = breezyTimeZoneService

    // The order of this list is preserved in "source chooser" dialogs
    private val sourceList: ImmutableList<Source> = buildList {
        addAll(locationSourceList)
        addAll(locationSearchSourceList)
        addAll(reverseGeocodingSourceList)
        addAll(worldwideWeatherSourceList)
        if (UniversalWeather.instance.debugMode) {
            add(debugService)
        }
        addAll(
            nationalWeatherSourceList
                .sortedWith { ws1, ws2 ->
                    // Sort by name because there are now a lot of sources
                    Collator.getInstance(context.currentLocale).compare(ws1.name, ws2.name)
                }
        )
        addAll(broadcastSourceList)
    }.toImmutableList()

    fun getSource(id: String): Source? = sourceList.firstOrNull { it.id == id }
    fun getHttpSources(): ImmutableList<HttpSource> = sourceList
        .filterIsInstance<HttpSource>()
        .toImmutableList()

    // Location
    fun getLocationSources(): ImmutableList<LocationSource> = sourceList
        .filterIsInstance<LocationSource>()
        .toImmutableList()

    fun getLocationSource(id: String): LocationSource? = getLocationSources().firstOrNull { it.id == id }
    fun getLocationSourceOrDefault(id: String): LocationSource = getLocationSource(id)
        ?: getLocationSource(BuildConfig.DEFAULT_LOCATION_SOURCE)!!

    fun getFeatureSources(): ImmutableList<FeatureSource> = sourceList
        .filterIsInstance<FeatureSource>()
        .toImmutableList()

    fun getFeatureSource(id: String): FeatureSource? = getFeatureSources().firstOrNull { it.id == id }

    fun getWeatherSources(): ImmutableList<WeatherSource> = sourceList
        .filterIsInstance<WeatherSource>()
        .toImmutableList()

    fun getWeatherSource(id: String): WeatherSource? = getWeatherSources().firstOrNull { it.id == id }

    // Secondary weather
    fun getPollenIndexSource(id: String): PollenIndexSource? = sourceList
        .filterIsInstance<PollenIndexSource>()
        .firstOrNull { it.id == id }

    // Location search
    fun getLocationSearchSources(): ImmutableList<LocationSearchSource> = sourceList
        .filterIsInstance<LocationSearchSource>()
        .toImmutableList()

    fun getLocationSearchSource(id: String): LocationSearchSource? = getLocationSearchSources()
        .firstOrNull { it.id == id }

    fun getLocationSearchSourceOrDefault(id: String): LocationSearchSource = getLocationSearchSource(id)
        ?: getLocationSearchSource(BuildConfig.DEFAULT_LOCATION_SEARCH_SOURCE)!!

    fun getConfiguredLocationSearchSources(): ImmutableList<LocationSearchSource> = getLocationSearchSources()
        .filter { it !is ConfigurableSource || it.isConfigured }
        .toImmutableList()

    // Reverse geocoding
    fun getReverseGeocodingSources(): ImmutableList<ReverseGeocodingSource> = sourceList
        .filterIsInstance<ReverseGeocodingSource>()
        .filter { it.supportedFeatures.containsKey(SourceFeature.REVERSE_GEOCODING) }
        .toImmutableList()

    fun getReverseGeocodingSource(id: String): ReverseGeocodingSource? = getReverseGeocodingSources()
        .firstOrNull { it.id == id }

    fun getReverseGeocodingSourceOrDefault(id: String): ReverseGeocodingSource = getReverseGeocodingSource(id)
        ?: getReverseGeocodingSource(BuildConfig.DEFAULT_GEOCODING_SOURCE)!!

    // Broadcast
    fun getBroadcastSources(): ImmutableList<BroadcastSource> = sourceList
        .filterIsInstance<BroadcastSource>()
        .toImmutableList()

    fun isBroadcastSourcesEnabled(context: Context): Boolean {
        return getBroadcastSources().any {
            (SourceConfigStore(context, it.id).getString("packages", null) ?: "").isNotEmpty()
        }
    }

    // Configurables sources
    fun getConfigurableSources(): ImmutableList<ConfigurableSource> = sourceList
        .filterIsInstance<ConfigurableSource>()
        .toImmutableList()

    // Time zone sources
    fun getTimeZoneSource(): TimeZoneSource = timeZoneSource

    fun sourcesWithPreferencesScreen(
        location: Location,
    ): ImmutableList<PreferencesParametersSource> {
        val preferencesScreenSources = mutableListOf<PreferencesParametersSource>()

        with(location) {
            listOf(
                Pair(forecastSource, SourceFeature.FORECAST),
                Pair(currentSource, SourceFeature.CURRENT),
                Pair(airQualitySource, SourceFeature.AIR_QUALITY),
                Pair(pollenSource, SourceFeature.POLLEN),
                Pair(minutelySource, SourceFeature.MINUTELY),
                Pair(alertSource, SourceFeature.ALERT),
                Pair(normalsSource, SourceFeature.NORMALS)
            ).forEach {
                val source = getWeatherSource(it.first ?: location.forecastSource)
                if (source is PreferencesParametersSource &&
                    source.hasPreferencesScreen(location, listOf(it.second)) &&
                    !preferencesScreenSources.contains(source)
                ) {
                    preferencesScreenSources.add(source)
                }
            }
        }

        return preferencesScreenSources
            /*.sortedWith { s1, s2 ->
                // Sort by name because there are now a lot of sources
                Collator.getInstance(
                    SettingsManager.getInstance(context).language.locale
                ).compare(s1.name, s2.name)
            })*/
            .toImmutableList()
    }

    fun getSupportedFeatureSources(
        feature: SourceFeature? = null,
        location: Location? = null,
        // Optional id of the source that will always be taken, even if not matching the criteria
        sourceException: String? = null,
    ): ImmutableList<FeatureSource> = getFeatureSources()
        .filter {
            it.id != "naturalearth" && (
                it.id == sourceException ||
                    (
                        it !is RemovedSource && (
                            feature == null ||
                                (
                                    it.supportedFeatures.containsKey(feature) &&
                                        (
                                            location == null ||
                                                (location.isCurrentPosition && !location.isUsable) ||
                                                it.isFeatureSupportedForLocation(location, feature)
                                            )
                                    )
                            )
                        )
                )
        }.toImmutableList()

    /**
     * Best source is determined using the priority given by sources, excluding unconfigured and restricted sources
     */
    fun getBestSourceForFeature(
        location: Location,
        feature: SourceFeature,
    ): FeatureSource? {
        return getSupportedFeatureSources(feature, location)
            .filter {
                it !is RemovedSource &&
                    it.isFeatureSupportedForLocation(location, feature) &&
                    it.getFeaturePriorityForLocation(location, feature) > PRIORITY_NONE &&
                    (it !is ConfigurableSource || (it.isConfigured && !it.isRestricted)) &&
                    (BuildConfig.FLAVOR != "freenet" || it !is NonFreeNetSource)
            }
            .maxByOrNull { it.getFeaturePriorityForLocation(location, feature) }
    }

    /**
     * For air quality, default source is Open-Meteo (except India due to different times from the preselected IMD forecast)
     * For pollen:
     * - Open-Meteo in Europe
     * - AccuWeather in USA/Canada
     * - None in other countries
     * For alerts, default source is AccuWeather (may be FPAS or WMO SWIC in the future)
     * For normals, default source is AccuWeather (may be NCEI in the future), unless:
     * - In China: no normals source, due to firewall
     * For other cases, default source is Open-Meteo
     */
    fun getDefaultSourceForFeature(
        location: Location,
        feature: SourceFeature,
    ): FeatureSource? {
        return when (feature) {
            SourceFeature.AIR_QUALITY -> if (!location.countryCode.equals("IN", ignoreCase = true) ||
                BuildConfig.FLAVOR == "freenet"
            ) {
                getWeatherSource("openmeteo")
            } else {
                null
            }

            SourceFeature.POLLEN -> getWeatherSource("openmeteo")?.let {
                if (it.isFeatureSupportedForLocation(location, feature)) it else null
            } ?: getWeatherSource("accu")?.takeIf {
                it.isFeatureSupportedForLocation(location, feature) && BuildConfig.FLAVOR != "freenet"
            }

            SourceFeature.ALERT -> if (BuildConfig.FLAVOR != "freenet") getWeatherSource("accu") else null
            SourceFeature.NORMALS -> if (!location.countryCode.equals("CN", ignoreCase = true) &&
                BuildConfig.FLAVOR != "freenet"
            ) {
                getWeatherSource("accu")
            } else {
                null
            }

            SourceFeature.REVERSE_GEOCODING -> getReverseGeocodingSource("nominatim")
            else -> getWeatherSource("openmeteo")
        }
    }

    fun getBestSourceForFeatureOrDefault(
        location: Location,
        feature: SourceFeature,
    ): FeatureSource? {
        return if (feature != SourceFeature.REVERSE_GEOCODING ||
            location.isCurrentPosition ||
            location.needsGeocodeRefresh
        ) {
            getBestSourceForFeature(location, feature)
                ?: getDefaultSourceForFeature(location, feature)
        } else {
            null
        }
    }
}
