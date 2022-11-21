package com.example.aure.model.User

import com.example.aure.model.Catch.Gear
import com.example.aure.model.Catch.Reel
import com.example.aure.model.Catch.Rod
import com.example.aure.model.Catch.Water
import com.example.aure.utils.toMapAndExcludeInclude
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlin.reflect.full.declaredMemberProperties

/**
 * TODO
 * Utfyll og implementer FisherType Enum i UserProfile
 * */
enum class FisherType {
    FLYFISHER, BAITFISHER, TROLLING, SPINNING
}

data class UserProfile(
    val nickname: String,
    val rod: Rod,
    val reel: Reel,
    val fisherType: String,
    val profileText: String
) {
    fun buildUserProfileDatabaseMap(user_id: String): Map<String, Any?> {
        val include = mapOf("user_id" to user_id)
        val exclude = listOf<String>()
        return toMapAndExcludeInclude(
            this,
            include=include
        )
    }
    companion object {
        fun userProfileDatabaseFields(): List<String> {
            val include = listOf<String>()
            val exclude = listOf<String>("rod", "reel")
            val crdbFields = mutableListOf<String>()
            crdbFields.addAll(Rod::class.declaredMemberProperties.map { it.name })
            crdbFields.addAll(Reel::class.declaredMemberProperties.map { it.name })
            crdbFields.addAll(UserProfile::class.declaredMemberProperties.map { it.name })

            crdbFields.addAll(include)
            crdbFields.removeAll(exclude)
            return crdbFields
        }
    }
}