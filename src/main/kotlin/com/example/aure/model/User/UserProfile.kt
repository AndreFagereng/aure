package com.example.aure.model.User

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

enum class FisherType {
    FLYFISHER, BAITFISHER, TROLLING, SPINNING
}
@JsonIgnoreProperties("id", "user_id")
data class UserProfile (
    val id: Int? = null,
    val user_id: String? = null,
    val nickname: String,
    val rod: String,
    val line: String,
    val fisherType: List<FisherType>,
    val profileText: String)