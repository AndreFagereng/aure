package com.example.aure.model.Catch

import com.example.aure.model.Weather.Weather
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.LocalDate


data class CatchReport(
    val catch: Catch,
    val weather: Weather
    )

@JsonIgnoreProperties("user_id")
data class Catch(
    val id: Int? = null,
    val user_id: String? = null,
    val water: String,
    val species: String,
    val weight: BigDecimal,
    val length: BigDecimal,
    val captureDate: LocalDate,
    val fly: String,
    val waterTemp: BigDecimal,
    val longitude: BigDecimal,
    val latitude: BigDecimal
    )

