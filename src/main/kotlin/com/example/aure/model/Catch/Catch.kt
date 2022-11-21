package com.example.aure.model.Catch

import com.example.aure.model.Weather.UvIndex
import com.example.aure.model.Weather.Weather
import com.example.aure.model.Weather.Wind
import com.example.aure.utils.toMapAndExcludeInclude
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.reflect.full.declaredMemberProperties

data class CatchReport(
    val id: Int? = null,
    val species: Species,
    val gear: Gear,
    val location: Location,
    val weather: Weather,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val captureDate: LocalDateTime,
    val notes: String,
    val water: Water,
    var images: List<Image>
) {
    fun buildCatchReportDatabaseMap(user_id: String): Map<String, Any?> {
        val excludeValues = listOf("id", "catchreport_id", "images")
        val includeMap = mapOf(
            "user_id" to user_id,
        )
        return toMapAndExcludeInclude(
            this,
            include = includeMap,
            exclude = excludeValues
        )
    }
    companion object {
        fun getCatchReportDatabaseFields(): List<String> {
            val includeFields = listOf(
                "captureDate",
                "notes",
                )
            val excludeFields = listOf(
                "water"
            )
            val crdbFields = mutableListOf<String>()
            crdbFields.addAll(Lure::class.declaredMemberProperties.map { it.name })
            crdbFields.addAll(Reel::class.declaredMemberProperties.map { it.name })
            crdbFields.addAll(Rod::class.declaredMemberProperties.map { it.name })
            crdbFields.addAll(Species::class.declaredMemberProperties.map { it.name })
            crdbFields.addAll(Location::class.declaredMemberProperties.map { it.name })
            crdbFields.addAll(Water::class.declaredMemberProperties.map { it.name })

            crdbFields.addAll(includeFields)
            crdbFields.removeAll(excludeFields)
            return crdbFields.toMutableList()
        }

        fun getWeatherDatabaseFields(): List<String> {
            val includeFields = listOf<String>()
            val excludeFields = listOf(
                "catchreport_id",
                "uvIndex",
                "wind"
            )
            val crdbFields = mutableListOf<String>()
            crdbFields.addAll(Weather::class.declaredMemberProperties.map { it.name })
            crdbFields.addAll(Wind::class.declaredMemberProperties.map { it.name })
            crdbFields.addAll(UvIndex::class.declaredMemberProperties.map { it.name })

            crdbFields.addAll(includeFields)
            crdbFields.removeAll(excludeFields)
            return crdbFields
        }
    }
}

data class Image(
    @JsonProperty("id") val image_id: Int,
    @JsonProperty("url") val image_url: String?
)

data class Species(
    @JsonProperty("name") val species_name: String,
    @JsonProperty("weight") val species_weight: BigDecimal,
    @JsonProperty("length") val species_length: BigDecimal,
    @JsonProperty("gender") val species_gender: String
)

data class Gear(
    val rod: Rod,
    val reel: Reel,
    val lure: Lure
)

data class Rod(
    @JsonProperty("brand") val rod_brand: String,
    @JsonProperty("sizeClass") val rod_sizeClass: String
)

data class Reel(
    @JsonProperty("brand") val reel_brand: String,
    @JsonProperty("sizeClass") val reel_sizeClass: String,
    @JsonProperty("fishingLine") val reel_fishingLine: String,
    @JsonProperty("lineType") val reel_lineType: String
)

data class Lure(
    @JsonProperty("name") val lure_name: String,
    @JsonProperty("sizeClass") val lure_sizeClass: String,
    @JsonProperty("type") val lure_type: String,
)

data class Location(
    @JsonProperty("municipality") val location_municipality: String,
    @JsonProperty("county") val location_county: String,
    @JsonProperty("country") val location_country: String,
    @JsonProperty("latitude") val location_latitude: BigDecimal,
    @JsonProperty("longitude") val location_longitude: BigDecimal,
    @JsonProperty("mamsl") val location_mamsl: Int
)

data class Water(
    @JsonProperty("name") val water_name: String,
    @JsonProperty("waterLevel") val water_level: String,
    @JsonProperty("temperature") val water_temperature: String
)
