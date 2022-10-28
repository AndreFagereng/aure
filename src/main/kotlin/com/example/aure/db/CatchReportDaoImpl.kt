package com.example.aure.db

import com.example.aure.db.utils.Tables
import com.example.aure.model.Catch.Catch
import com.example.aure.model.Catch.CatchReport
import com.example.aure.utils.toMapAndExclude
import com.example.aure.model.Weather.UvIndex
import com.example.aure.model.Weather.Weather
import com.example.aure.model.Weather.Wind
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.annotation.Resource
import javax.sql.DataSource

@Repository
class CatchReportDaoImpl {

    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Resource(name = "aure-db")
    fun setDataSource(dataSource: DataSource?) {
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource!!)
    }

    fun getCatchReport(user_id: String): List<CatchReport> {
        return namedParameterJdbcTemplate.query(
            GET_QUERY, mapOf<String, String>("user_id" to user_id)
        ) { rs: ResultSet, _ ->
            toCatchReport(rs)
        }
    }

    fun createCatchReport(user_id: String, catchReport: CatchReport) {
        val catchReportMap = toMapAndExclude(catchReport, include = mapOf("user_id" to user_id), exclude = listOf("id", "user_id", "catchreport_id"))
        namedParameterJdbcTemplate.query(
            POST_QUERY, catchReportMap
        ){}
    }

    fun updateCatchReport(user_id: String, catchReport: CatchReport) {
        val catchReportMap = toMapAndExclude(catchReport, include = mapOf("user_id" to user_id), exclude = listOf("user_id", "catchreport_id"))
        namedParameterJdbcTemplate.query(
            PUT_QUERY, catchReportMap
        ) {}
    }

    companion object {
        val CR = Tables.CATCH_REPORT
        val WH = Tables.WEATHER

        // Selects catchreport and weather based on user_id and primary/foreignkey
        val GET_QUERY = """
            SELECT * FROM $CR 
            LEFT JOIN $WH 
            ON ($CR.id = $WH.catchreport_id)
            WHERE $CR.user_id = (:user_id)
        """.trimIndent()

        // Updates current catchreport with updated values
        val PUT_QUERY = """
            WITH cupdate as (
                UPDATE $CR
                SET water = (:water), species = (:species), weight = (:weight), length = (:length), captureDate = (:captureDate), fly = (:fly), waterTemp = (:waterTemp), longitude = (:longitude), latitude = (:latitude) 
                WHERE user_id = (:user_id) 
                AND id = (:id)
                RETURNING id
            )
            UPDATE $WH
            SET  temperature = (:temperature), apparenttemperature = (:apparentTemperature), wind_direction = (:direction), wind_compassdirection = (:compassDirection), 
                                wind_gust = (:gust), uvindex_value = (:value), uvindex_category = (:category), visibility = (:visibility), isDayLight = (:isDayLight), 
                                humidity = (:humidity), dewPoint = (:dewPoint), pressure = (:pressure), pressuretrend = (:pressureTrend), symbolname = (:symbolName)
            WHERE user_id = (:user_id)
            AND catchreport_id = (SELECT id FROM cupdate)
            RETURNING catchreport_id
            
        """.trimIndent()

        // Inserts and links catchreport and weather tables on id/catchreport_id.
        val POST_QUERY = """
            WITH cinsert as (
                INSERT INTO $CR (user_id, water, species, weight, length, captureDate, fly, waterTemp, longitude, latitude)
                VALUES (:user_id, :water, :species, :weight, :length, :captureDate, :fly, :waterTemp, :longitude, :latitude) 
                RETURNING id
            )
            INSERT INTO $WH (catchreport_id, user_id, temperature, apparenttemperature, wind_direction, wind_compassdirection, wind_gust, uvindex_value, uvindex_category, visibility, isDayLight, humidity, dewPoint, pressure, pressuretrend, symbolname)
            VALUES ((SELECT id FROM cinsert), :user_id, :temperature, :apparentTemperature, :direction, :compassDirection, :gust, :value, :category, :visibility, :isDayLight, :humidity, :dewPoint, :pressure, :pressureTrend, :symbolName)
            RETURNING catchreport_id
        """.trimIndent()

        fun toCatchReport(rs: ResultSet): CatchReport {
            return CatchReport(
                Catch(
                    rs.getInt("id"),
                    rs.getString("user_id"),
                    rs.getString("water"),
                    rs.getString("species"),
                    rs.getBigDecimal("weight"),
                    rs.getBigDecimal("length"),
                    rs.getDate("captureDate").toLocalDate(),
                    rs.getString("fly"),
                    rs.getBigDecimal("waterTemp"),
                    rs.getBigDecimal("longitude"),
                    rs.getBigDecimal("latitude")
                ), Weather(
                    rs.getInt("catchreport_id"),
                    rs.getString("user_id"),
                    rs.getBigDecimal("temperature"),
                    rs.getBigDecimal("apparentTemperature"),
                    Wind(
                        rs.getBigDecimal("wind_direction"),
                        rs.getString("wind_compassDirection"),
                        rs.getBigDecimal("wind_gust")
                    ),
                    UvIndex(
                        rs.getBigDecimal("uvindex_value"), rs.getString("uvindex_category")
                    ),
                    rs.getBigDecimal("visibility"),
                    rs.getBoolean("isDayLight"),
                    rs.getBigDecimal("humidity"),
                    rs.getBigDecimal("dewPoint"),
                    rs.getBigDecimal("pressure"),
                    rs.getString("pressureTrend"),
                    rs.getString("symbolName")
                )
            )
        }
    }

}
