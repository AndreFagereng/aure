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
class UserDaoImpl {

    lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Resource(name = "aure-db")
    fun setDataSource(dataSource: DataSource?) {
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource!!)
    }

    fun getFriends(user_id: String) {
        print("getFriends not implemented..")
        TODO()
    }

    fun addFriend(sender_id: Int, recipient_id: Int) {
        namedParameterJdbcTemplate.query(
            ADD_FRIEND(),
            mapOf<String, Any>("sender_id" to sender_id, "recipient_id" to recipient_id, "accepted" to false)
        ) {}
    }

    fun acceptFriend(sender_id: Int, recipient_id: Int) {
        namedParameterJdbcTemplate.query(
            ACCEPT_FRIEND(), mapOf<String, Any>("sender_id" to sender_id, "recipient_id" to recipient_id)
        ) { }
    }

    fun removeFriend(sender_id: Int, recipient_id: Int) {
        namedParameterJdbcTemplate.query(
            REMOVE_FRIEND(), mapOf<String, Any>("sender_id" to sender_id, "recipient_id" to recipient_id)
        ) { }
    }

    fun getUserProfile(user_id: String): UserProfile {
        return namedParameterJdbcTemplate.query(
            GET_USERPROFILE, mapOf<String, String>("user_id" to user_id)
        ) { rs: ResultSet, _ ->
            toUserProfile(rs)
        }.single()
    }

    fun upsertUserProfile(user_id: String, userProfile: UserProfile) {
        val userProfileMap = userProfile.buildUserProfileDatabaseMap(user_id)
        namedParameterJdbcTemplate.update(
            UPSERT_USERPROFILE(
                UserProfile.userProfileDatabaseFields()
            ), userProfileMap
        )
    }

    private fun toUserProfile(rs: ResultSet): UserProfile {
        return UserProfile(
            rs.getInt("id"),
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

        //id, sender_id, recipient_id, status
        fun ADD_FRIEND() = """
            INSERT INTO ${Tables.FRIENDSHIPS} (sender_id, recipient_id, accepted)
            VALUES (:sender_id, :recipient_id, :accepted)
            RETURNING id
        """.trimIndent()

        fun REMOVE_FRIEND() = """
            DELETE FROM ${Tables.FRIENDSHIPS}
            WHERE (sender_id = (:sender_id) AND recipient_id = (:recipient_id))
            OR (sender_id = (:recipient_id) AND recipient_id = (:sender_id)) 
            RETURNING id
        """.trimIndent()

        fun ACCEPT_FRIEND() = """
            UPDATE ${Tables.FRIENDSHIPS}
            SET accepted = true
            WHERE (sender_id = (:sender_id) AND recipient_id = (:recipient_id))
            OR (sender_id = (:recipient_id) AND recipient_id = (:sender_id))
            RETURNING id
        """.trimIndent()

        fun CANCEL_FRIEND_REQUEST() = """
            DELETE FROM ${Tables.FRIENDSHIPS}
            WHERE sender_id = (:sender_id)
            AND recipient_id = (:recipient_id)
        """.trimIndent()

        val GET_USERPROFILE = """
            SELECT * FROM ${Tables.USER_PROFILE} 
            WHERE ${Tables.USER_PROFILE}.user_id = (:user_id)
        """.trimIndent()

        fun UPSERT_USERPROFILE(
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

