package com.example.aure.db

import com.example.aure.model.Catch.Catch
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.annotation.Resource
import javax.sql.DataSource


@Repository
class CatchDaoImpl {

    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate


    @Resource(name = "aure-db")
    fun setDataSource(dataSource: DataSource?) {
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource!!)
    }

    fun updateCatch(catch: Catch): Int {
        // Don't need for now.
        TODO()
    }

    fun getCatch(user_id: String): List<Catch> {
        val catchResult = namedParameterJdbcTemplate.query(
            getCatchQuery, mapOf<String, String>(
                "user_id" to user_id
            )
        ) { rs: ResultSet, _ ->
            Catch(
                rs.getInt("id"),
                rs.getString("water"),
                rs.getString("species"),
                rs.getBigDecimal("weight"),
                rs.getBigDecimal("length"),
                rs.getTimestamp("captureDate").toLocalDateTime(),
                rs.getString("fly"),
                rs.getBigDecimal("waterTemp"),
                rs.getBigDecimal("longitude"),
                rs.getBigDecimal("latitude")
            )
        }
        return catchResult
    }
    fun createCatch(user_id: String, catch: Catch): Int {

        val createdId = namedParameterJdbcTemplate.query(
            createCatchQuery, mapOf<String, Any?>(
                "user_id" to user_id,
                "water" to catch.water,
                "species" to catch.species,
                "weight" to catch.weight,
                "length" to catch.length,
                "captureDate" to catch.captureDate,
                "fly" to catch.fly,
                "waterTemp" to catch.waterTemp,
                "longitude" to catch.longitude,
                "latitude" to catch.latitude
            )
        ){rs: ResultSet, _ -> rs.getInt("id")}
        return createdId.single()
        }


    companion object {
        private val catchTable: String = "aure.catchreports"

        val getCatchQuery: String = """ SELECT * FROM $catchTable where user_id = :user_id """
        val createCatchQuery: String =
            """ INSERT INTO $catchTable (user_id, water, species, weight, length, captureDate, fly, waterTemp, longitude, latitude)
                VALUES (:user_id, :water, :species, :weight, :length, :captureDate, :fly, :waterTemp, :longitude, :latitude) RETURNING id
            """.trimMargin()
    }
}


