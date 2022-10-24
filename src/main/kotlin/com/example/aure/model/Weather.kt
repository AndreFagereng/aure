package com.example.aure.model

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

@JsonIgnoreProperties("id", "catchreport_id")
data class Weather(

    val id: Int? = null,
    val catchreport_id: Int? = null,
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