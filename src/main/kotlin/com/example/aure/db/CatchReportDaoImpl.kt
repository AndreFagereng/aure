package com.example.aure.db

import com.example.aure.db.utils.Tables
import com.example.aure.model.Catch.*
import com.example.aure.model.Weather.Weather
import com.example.aure.utils.buildFieldQueryString
import com.example.aure.utils.buildSetQueryString
import com.example.aure.utils.buildValueQueryString
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

    fun getCatchReport(user_id: String, at: Int, size: Int): List<CatchReport> {
        val getIdFrom = at.takeUnless { it == 0 } ?: 1
        val getIdTo = (getIdFrom + size) - 1

        return namedParameterJdbcTemplate.query(
            GET_QUERY(), mapOf<String, Any>("user_id" to user_id, "from" to getIdFrom, "to" to getIdTo)
        ) { rs: ResultSet, _ ->
            toCatchReport(rs)
        }
    }

    fun createCatchReport(user_id: String, catchReport: CatchReport): Int {
        val catchReportMap = catchReport.buildCatchReportDatabaseMap(user_id)
        val catchreportId = namedParameterJdbcTemplate.query(
            POST_QUERY(
                CatchReport.getCatchReportDatabaseFields(),
                catchReport.images.map { it.image_id }.toList().toString()
            ), catchReportMap
        ) {rs: ResultSet, _ -> rs.getInt("id")}
        return catchreportId[0]
    }

    fun updateCatchReport(user_id: String, catchReport: CatchReport) {
        val catchReportMap = catchReport.buildCatchReportDatabaseMap(user_id)
        namedParameterJdbcTemplate.query(
            PUT_QUERY(
                CatchReport.getCatchReportDatabaseFields(),
                CatchReport.getWeatherDatabaseFields(),
                catchReport.images.map { it.image_id }.toList().toString(),
                catchReport.id!!
            ), catchReportMap
        ) {}
    }

    companion object {

        fun GET_QUERY(): String {
            return """
                SELECT * FROM (
                    SELECT row_number() over(ORDER BY ${Tables.CATCH_REPORT}.id) as rownumber, * 
                    FROM ${Tables.CATCH_REPORT}
                    LEFT JOIN ${Tables.WEATHER} we
                    ON (${Tables.CATCH_REPORT}.id = we.catchreport_id)
                    WHERE ${Tables.CATCH_REPORT}.user_id = (:user_id)
                    ORDER BY capturedate ASC
                ) e
                WHERE rownumber BETWEEN (:from) AND (:to)
                """.trimIndent()
        }

        fun POST_QUERY(
            catchReportFieldList: List<String>,
            image_ids: String
        ): String {
            return """
                INSERT INTO ${Tables.CATCH_REPORT} 
                    (user_id, image_ids, ${buildFieldQueryString(catchReportFieldList, prefix = "")}
                VALUES 
                    (:user_id, ARRAY $image_ids, ${buildValueQueryString(catchReportFieldList, prefix = "")}
                RETURNING id
                    """.trimIndent()
        }

        fun PUT_QUERY(
            catchReportFieldList: List<String>,
            weatherFieldList: List<String>,
            image_ids: String,
            id: Int
        ): String {
            return """
                WITH cupdate as (
                    UPDATE ${Tables.CATCH_REPORT}
                    SET 
                        image_ids = ARRAY $image_ids, ${buildSetQueryString(catchReportFieldList)}
                    WHERE user_id = (:user_id) 
                    AND id = $id
                    RETURNING id
                )
                UPDATE ${Tables.WEATHER}
                SET 
                    ${buildSetQueryString(weatherFieldList)}
                
                AND catchreport_id = (SELECT id FROM cupdate)
                RETURNING catchreport_id
        """.trimIndent()
            //WHERE user_id = (:user_id)
        }


        fun toCatchReport(rs: ResultSet): CatchReport {
            val imageIds = rs.getObject("image_ids")
                .toString()
                .replace("{", "")
                .replace("}", "")
                .split(",")
                .map { it.toInt() }

            return CatchReport(
                id = rs.getInt("id"),
                notes = rs.getString("notes"),
                water = Water(
                    water_name = rs.getString("water_name"),
                    water_level = rs.getString("water_level"),
                    water_temperature = rs.getString("water_temperature")
                ),
                species = Species(
                    rs.getString("species_name"),
                    rs.getBigDecimal("species_weight"),
                    rs.getBigDecimal("species_length"),
                    rs.getString("species_gender")
                ),
                gear = Gear(
                    rod = Rod(
                        rs.getString("rod_brand"),
                        rs.getString("rod_sizeClass")
                    ),
                    reel = Reel(
                        rs.getString("reel_brand"),
                        rs.getString("reel_sizeClass"),
                        rs.getString("reel_fishingline"),
                        rs.getString("reel_lineType")
                    ),
                    lure = Lure(
                        rs.getString("lure_name"),
                        rs.getString("lure_sizeClass"),
                        rs.getString("lure_type"),
                    )
                ),
                location = Location(
                    rs.getString("location_municipality"),
                    rs.getString("location_county"),
                    rs.getString("location_country"),
                    rs.getBigDecimal("location_latitude"),
                    rs.getBigDecimal("location_longitude"),
                    rs.getInt("location_mamsl")
                ),
                weather = Weather(
                    rs.getInt("catchreport_id"),
                    rs.getBigDecimal("temperature"),
                    rs.getBigDecimal("temperatureApparent"),
                    rs.getBigDecimal("visibility"),
                    rs.getBoolean("daylight"),
                    rs.getBigDecimal("humidity"),
                    rs.getBigDecimal("pressure"),
                    rs.getString("pressureTrend"),
                    rs.getBigDecimal("windDirection"),
                    rs.getBigDecimal("windSpeed"),
                    rs.getBigDecimal("windGust"),
                    rs.getBigDecimal("uvIndex"),
                    rs.getString("precipitationType"),
                    rs.getBigDecimal("precipitationChance"),
                    rs.getBigDecimal("precipitationIntensity"),
                    rs.getBigDecimal("precipitationAmount"),
                    rs.getString("conditionCode"),
                    rs.getBigDecimal("cloudCover")
                ),
                captureDate = rs.getTimestamp("captureDate").toLocalDateTime(),
                images = imageIds.map {
                    Image(it, null)
                }
            )
        }
    }

}
