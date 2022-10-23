package com.example.aure.db

import com.example.aure.model.Catch
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.annotation.Resource
import javax.sql.DataSource


@Repository
class CatchDaoImpl {

    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate


    @Resource(name = "aure-db")
    fun setDataSource(dataSource: DataSource?) {
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource!!)
    }


    fun createCatchDB(catch: Catch): Int {
        try {
            val createdId = namedParameterJdbcTemplate.query(
                createCatchQuery, mapOf<String, Any?>(
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
        } catch (e: Exception) {
            println("createCatchDB Error -> ${e.message}")
        }
        return -1
    }

    fun getCatchDB(): List<Catch> {
        val catchResult = namedParameterJdbcTemplate.query(
            getCatchQuery, emptyMap<String, String>()
        ) { rs: ResultSet, _ ->
            Catch(
                rs.getInt("id"),
                rs.getString("water"),
                rs.getString("species"),
                rs.getBigDecimal("weight"),
                rs.getBigDecimal("length"),
                rs.getDate("captureDate").toLocalDate(),
                rs.getString("fly"),
                rs.getBigDecimal("waterTemp"),
                rs.getBigDecimal("longitude"),
                rs.getBigDecimal("latitude")
            )
        }
        return catchResult
    }

    companion object {
        private val catchTable: String = "aure.catchreports"

        val getCatchQuery: String = """ SELECT * FROM $catchTable """
        val createCatchQuery: String =
            """ INSERT INTO $catchTable (water, species, weight, length, captureDate, fly, waterTemp, longitude, latitude)
                VALUES (:water, :species, :weight, :length, :captureDate, :fly, :waterTemp, :longitude, :latitude) RETURNING id
            """.trimMargin()
    }
}


