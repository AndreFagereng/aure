package com.example.aure.model.Weather

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class Wind(
    @JsonProperty("direction") val wind_direction: BigDecimal,
    @JsonProperty("compassDirection") val wind_compassDirection: String,
    @JsonProperty("gust") val wind_gust: BigDecimal
)

data class UvIndex(
    @JsonProperty("value") val uvindex_value: BigDecimal,
    @JsonProperty("category") val uvindex_category: String
)

@JsonIgnoreProperties("id", "catchreport_id", "user_id")
data class Weather(
    val catchreport_id: Int? = null,
    @JsonProperty("temperature") val weather_temperature: BigDecimal,
    @JsonProperty("apparentTemperature")val weather_apparentTemperature: BigDecimal,
    val wind: Wind,
    val uvIndex: UvIndex,
    @JsonProperty("visibility")val weather_visibility: BigDecimal,
    @JsonProperty("isDayLight")val weather_isDayLight: Boolean,
    @JsonProperty("humidity")val weather_humidity: BigDecimal,
    @JsonProperty("dewPoint")val weather_dewPoint: BigDecimal,
    @JsonProperty("pressure")val weather_pressure: BigDecimal,
    @JsonProperty("pressureTrend")val weather_pressureTrend: String,
    @JsonProperty("symbolName")val weather_symbolName: String

)
