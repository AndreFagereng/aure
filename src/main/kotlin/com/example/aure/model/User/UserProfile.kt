package com.example.aure.model.User

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * TODO
 * Utfyll og implementer FisherType Enum i UserProfile
 * */
enum class FisherType {
    FLYFISHER, BAITFISHER, TROLLING, SPINNING
}
@JsonIgnoreProperties("id", "user_id")
data class UserProfile (
    val nickname: String,
    val rod: String,
    val line: String,
    val fisherType: String,
    val profileText: String)