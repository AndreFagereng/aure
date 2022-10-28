package com.example.aure.db

import com.example.aure.model.User.UserProfile
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.annotation.Resource
import javax.sql.DataSource

@Repository
class UserProfileDaoImpl {

    lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Resource(name = "aure-db")
    fun setDataSource(dataSource: DataSource?) {
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource!!)
    }

    fun getUserProfile(): UserProfile {
        TODO()
    }

    fun createUserProfile(userProfile: UserProfile): String {
        TODO()
    }

    fun updateUserProfile(userProfile: UserProfile): String {
        TODO()
    }

    companion object {
        private val userProfileTable: String = "aure.userprofile"

        val getCatchQuery: String = """ SELECT * FROM $userProfileTable """
        val updateUserProfileQuery: String = """"""
        val createUserProfileQuery: String =
            """ INSERT INTO $userProfileTable (nickname, rod, line, fisherType, profileText)
                VALUES (:nickname, :rod, :line, :fisherType, :profileText)
            """.trimMargin()
    }


}

