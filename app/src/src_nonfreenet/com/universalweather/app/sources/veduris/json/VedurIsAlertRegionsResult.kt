package com.universalweather.app.sources.veduris.json

import kotlinx.serialization.Serializable
import com.universalweather.app.sources.veduris.serializers.VedurIsAnySerializer

@Serializable
data class VedurIsAlertRegionsResult(
    @Suppress("ktlint")
    val features: List<@Serializable(with = VedurIsAnySerializer::class) Any?> = listOf(),
)
