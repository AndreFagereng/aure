package com.example.aure.model.Weather

import com.example.aure.utils.toMapAndExcludeInclude
import java.math.BigDecimal
import kotlin.reflect.full.declaredMemberProperties

data class Weather(
    val catchreport_id: Int? = null,
    //val forecastStart: LocalDateTime,
    val temperature: BigDecimal?,
    val temperatureApparent: BigDecimal?,
    val visibility: BigDecimal?,
    val daylight: Boolean?,
    val humidity: BigDecimal?,
    val pressure: BigDecimal?,
    val pressureTrend: String?,
    val windDirection: BigDecimal?,
    val windSpeed: BigDecimal?,
    val windGust: BigDecimal?,
    val uvIndex: BigDecimal?,
    val precipitationType: String?,
    val precipitationChance: BigDecimal?,
    val precipitationIntensity: BigDecimal?,
    val precipitationAmount: BigDecimal?,
    val conditionCode: String?,
    val cloudCover: BigDecimal?
) {
    fun buildWeatherKitDatabaseMap(): Map<String, Any?> {
        return toMapAndExcludeInclude(
            this,
            include = emptyMap(),
            exclude = emptyList()
        )
    }
    companion object {
        fun fromMap(weatherMap: Map<String, String>): Weather {
            return Weather(
                //forecastStart = LocalDateTime.parse(weatherMap["forecastStart"]),
                temperature = BigDecimal(weatherMap["temperature"]),
                temperatureApparent = BigDecimal(weatherMap["temperatureApparent"]),
                visibility = BigDecimal(weatherMap["visibility"]),
                daylight = weatherMap["daylight"].toBoolean(),
                humidity = BigDecimal(weatherMap["humidity"]),
                pressure = BigDecimal(weatherMap["pressure"]),
                pressureTrend = weatherMap["pressureTrend"]!!,
                windDirection = BigDecimal(weatherMap["windDirection"]),
                windSpeed = BigDecimal(weatherMap["windSpeed"]),
                windGust = BigDecimal(weatherMap["windGust"]),
                uvIndex = BigDecimal(weatherMap["uvIndex"]),
                precipitationType = weatherMap["precipitationType"]!!,
                precipitationChance = BigDecimal(weatherMap["precipitationChance"]),
                precipitationIntensity = BigDecimal(weatherMap["precipitationIntensity"]),
                precipitationAmount = BigDecimal(weatherMap["precipitationAmount"]),
                conditionCode = weatherMap["conditionCode"]!!,
                cloudCover = BigDecimal(weatherMap["cloudCover"])
            )

        }

        fun getWeatherKitDatabaseFields(): List<String> {
            val wkdbFields = mutableListOf<String>()
            wkdbFields.addAll(Weather::class.declaredMemberProperties.map { it.name })

            return wkdbFields
        }
    }
}


/*

val defaultWind = Wind(
    wind_direction = BigDecimal(-1),
    wind_compassDirection = "",
    wind_gust = BigDecimal(-1)
)

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
    @JsonProperty("apparentTemperature") val weather_apparentTemperature: BigDecimal,
    val wind: Wind = defaultWind,
    val uvIndex: UvIndex,
    @JsonProperty("visibility") val weather_visibility: BigDecimal,
    @JsonProperty("isDayLight") val weather_isDayLight: Boolean,
    @JsonProperty("humidity") val weather_humidity: BigDecimal,
    @JsonProperty("dewPoint") val weather_dewPoint: BigDecimal,
    @JsonProperty("pressure") val weather_pressure: BigDecimal,
    @JsonProperty("pressureTrend") val weather_pressureTrend: String,
    @JsonProperty("symbolName") val weather_symbolName: String
)*/
