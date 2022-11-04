package com.example.aure.db

import com.example.aure.db.utils.Tables
import com.example.aure.model.User.UserProfile
import com.example.aure.utils.toMapAndExclude
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.annotation.Resource
import javax.sql.DataSource

@Repository
class UserProfileDaoImpl {

    lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Resource(name = "aure-db")
    fun setDataSource(dataSource: DataSource?) {
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource!!)
    }

    fun getUserProfile(user_id: String): UserProfile {
        return namedParameterJdbcTemplate.query(
            GET_QUERY, mapOf<String, String>("user_id" to user_id)
        ) { rs: ResultSet, _ ->
            toUserProfile(rs)
        }.single()
    }

    fun createOrUpdateUserProfile(user_id: String, userProfile: UserProfile) {
        val userProfileMap = toMapAndExclude(userProfile, include = mapOf("user_id" to user_id))
        namedParameterJdbcTemplate.update(PUTPOST_QUERY, userProfileMap)
    }

    private fun toUserProfile(rs: ResultSet): UserProfile {
        return UserProfile(
            rs.getString("nickname"),
            rs.getString("rod"),
            rs.getString("line"),
            rs.getString("fisherType"),
            rs.getString("profileText")
        )
    }

    companion object {

        // Selects catchreport and weather based on user_id and primary/foreignkey
        val GET_QUERY = """
            SELECT * FROM ${Tables.USER_PROFILE} 
            WHERE ${Tables.USER_PROFILE}.user_id = (:user_id)
        """.trimIndent()

        val PUTPOST_QUERY = """
            INSERT INTO ${Tables.USER_PROFILE} (user_id, nickname, rod, line, fisherType, profileText)
            VALUES (:user_id, :nickname, :rod, :line, :fisherType, :profileText)
            ON CONFLICT (user_id)
            DO UPDATE 
            SET user_id = (:user_id), nickname = (:nickname), rod = (:rod), line = (:line), fisherType = (:fisherType), profileText = (:profileText)
            WHERE ${Tables.USER_PROFILE}.user_id = (:user_id)
        """.trimIndent()

    }


}

