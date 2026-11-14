package com.universalweather.app.sources.veduris.json

import kotlinx.serialization.Serializable

@Serializable
data class VedurIsStationResult(
    val forecasts: Map<String, VedurIsForecast>? = null,
)
