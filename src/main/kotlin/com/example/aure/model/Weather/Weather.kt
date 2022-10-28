package com.example.aure.model.Weather

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

data class Wind(
    val direction: BigDecimal,
    val compassDirection: String,
    val gust: BigDecimal
)

data class UvIndex(
    val value: BigDecimal,
    val category: String
)

@JsonIgnoreProperties("id", "catchreport_id", "user_id")
data class Weather(
    val catchreport_id: Int? = null,
    val user_id: String? = null,
    val temperature: BigDecimal,
    val apparentTemperature: BigDecimal,
    val wind: Wind,
    val uvIndex: UvIndex,
    val visibility: BigDecimal,
    val isDayLight: Boolean,
    val humidity: BigDecimal,
    val dewPoint: BigDecimal,
    val pressure: BigDecimal,
    val pressureTrend: String,
    val symbolName: String

)
