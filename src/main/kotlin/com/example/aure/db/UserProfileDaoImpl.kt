package com.example.aure.db

import com.example.aure.db.utils.Tables
import com.example.aure.model.Catch.Reel
import com.example.aure.model.Catch.Rod
import com.example.aure.model.User.UserProfile
import com.example.aure.utils.buildFieldQueryString
import com.example.aure.utils.buildSetQueryString
import com.example.aure.utils.buildValueQueryString
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
            GET_QUERY(), mapOf<String, String>("user_id" to user_id)
        ) { rs: ResultSet, _ ->
            toUserProfile(rs)
        }.single()
    }

    fun createOrUpdateUserProfile(user_id: String, userProfile: UserProfile) {
        val userProfileMap = userProfile.buildUserProfileDatabaseMap(user_id)
        namedParameterJdbcTemplate.update(PUTPOST_QUERY(
            UserProfile.userProfileDatabaseFields()
        ), userProfileMap)
    }

    private fun toUserProfile(rs: ResultSet): UserProfile {
        return UserProfile(
            rs.getString("nickname"),
            Rod(
                rs.getString("rod_brand"),
                rs.getString("rod_sizeClass"),
            ),
            Reel(
                rs.getString("reel_brand"),
                rs.getString("reel_sizeClass"),
                rs.getString("reel_fishingline"),
                rs.getString("reel_linetype"),
            ),
            rs.getString("fisherType"),
            rs.getString("profileText")
        )
    }

    companion object {

        // Selects catchreport and weather based on user_id and primary/foreignkey
        fun GET_QUERY() = """
            SELECT * FROM ${Tables.USER_PROFILE} 
            WHERE ${Tables.USER_PROFILE}.user_id = (:user_id)
        """.trimIndent()

        fun PUTPOST_QUERY(
            userProfileFieldList: List<String>,
        ) = """
            INSERT INTO ${Tables.USER_PROFILE} (user_id, ${buildFieldQueryString(userProfileFieldList, prefix = "")}
            VALUES (:user_id, ${buildValueQueryString(userProfileFieldList, prefix = "")}
            ON CONFLICT (user_id)
            DO UPDATE 
            SET user_id = (:user_id), ${buildSetQueryString(userProfileFieldList)}
            WHERE ${Tables.USER_PROFILE}.user_id = (:user_id)
        """.trimIndent()

    }


}

