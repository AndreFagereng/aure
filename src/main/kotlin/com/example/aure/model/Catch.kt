package com.example.aure.model

import java.time.LocalDate

data class Catch(
    val id: Int,
    val vann: String,
    val art: String,
    val vekt: String,
    val lengde: String,
    val tid: LocalDate,
    val flue: String,
    val vaerforhold: String,
    val vanntemp: Int,
    val longitude: String,
    val latitude: String
)