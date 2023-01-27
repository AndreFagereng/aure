package com.example.aure.service

import com.example.aure.db.WeatherKitDaoImpl
import com.example.aure.model.Catch.Location
import com.example.aure.model.Weather.Weather
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Service
class WeatherService {
    @Autowired
    private lateinit var weatherKitDaoImpl: WeatherKitDaoImpl

    @Autowired
    private lateinit var tokenBuilderJWT: JWTBuilder

    suspend fun getCurrentWeather(
        catchreportId: Int,
        atDateTime: LocalDateTime,
        location: Location
    ) {

        val token = tokenBuilderJWT.build()
        val jsonNode = sendHistoricWeatherHourlyRequest(token, atDateTime, location)
        val weatherKitMap = toWeatherKitMap(jsonNode)
        val weatherKit = Weather.fromMap(weatherKitMap)
        weatherKitDaoImpl.createWeatherKit(catchreportId, weatherKit)
    }

    private suspend fun sendHistoricWeatherHourlyRequest(
        token: String,
        startTime: LocalDateTime,
        location: Location
    ): JsonNode {

        val endTime = startTime.plusHours(1)
        val url = buildUrl(startTime, endTime, location.location_longitude, location.location_latitude)

        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(url) {
            bearerAuth(token)
        }
        client.close()

        val responseString: String = response.body()
        val jsonNode: JsonNode = ObjectMapper().readTree(responseString)
        return jsonNode

    }

    private fun buildUrl(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        long: BigDecimal,
        lat: BigDecimal
    ): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return "https://weatherkit.apple.com/api/v1/weather/en/$long/$lat?dataSets=forecastHourly&hourlyStart=${startTime.format(formatter)}Z&hourlyEnd=${endTime.format(formatter)}Z&timezone=Europe/London"
    }

    private suspend fun toWeatherKitMap(jsonNode: JsonNode): Map<String, String> {
        val forecast = jsonNode.get("forecastHourly").get("hours").get(0)
        val mutableMap = emptyMap<String, String>().toMutableMap()

        mutableMap["temperature"] = forecast.get("temperature").asText()
        mutableMap["temperatureApparent"] = forecast.get("temperatureApparent").asText()
        mutableMap["visibility"] = forecast.get("visibility").asText()
        mutableMap["daylight"] = forecast.get("daylight").asText()
        mutableMap["humidity"] = forecast.get("humidity").asText()
        mutableMap["pressure"] = forecast.get("pressure").asText()
        mutableMap["pressureTrend"] = forecast.get("pressureTrend").asText()
        mutableMap["windDirection"] = forecast.get("windDirection").asText()
        mutableMap["windSpeed"] = forecast.get("windSpeed").asText()
        mutableMap["windGust"] = forecast.get("windGust").asText()
        mutableMap["pressureTrend"] = forecast.get("pressureTrend").asText()
        mutableMap["uvIndex"] = forecast.get("uvIndex").asText()
        mutableMap["precipitationType"] = forecast.get("precipitationType").asText()
        mutableMap["precipitationChance"] = forecast.get("precipitationChance").asText()
        mutableMap["precipitationIntensity"] = forecast.get("precipitationIntensity").asText()
        mutableMap["precipitationAmount"] = forecast.get("precipitationAmount").asText()
        mutableMap["conditionCode"] = forecast.get("conditionCode").asText()
        mutableMap["cloudCover"] = forecast.get("cloudCover").asText()

        return mutableMap
    }

}

@Configuration
class JWTBuilder {

    @Value("\${weatherkit.kid}")
    private val kid: String? = null

    @Value("\${weatherkit.sub}")
    private val sub: String? = null

    @Value("\${weatherkit.iss}")
    private val iss: String? = null

    @Value("\${weatherkit.id}")
    private val id: String? = null

    @Value("\${weatherkit.key}")
    private val key: String? = null

    @Value("\${weatherkit.alg}")
    private val alg: String? = null


    fun build(): String {
        val priPKCS8 = PKCS8EncodedKeySpec(org.apache.commons.codec.binary.Base64().decode(key))
        val appleKey = KeyFactory.getInstance("EC").generatePrivate(priPKCS8)
        val token = Jwts.builder()
            .setHeaderParam("id", id)
            .setHeaderParam("kid", kid)
            .setHeaderParam("alg", alg)
            .setIssuer(iss)
            .setSubject(sub)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plusSeconds(120)))
            .signWith(SignatureAlgorithm.valueOf(alg!!), appleKey).compact()
        return token
    }

}
