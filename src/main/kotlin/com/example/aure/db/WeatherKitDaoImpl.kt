package com.example.aure.db

import com.example.aure.db.utils.Tables
import com.example.aure.model.Weather.Weather
import com.example.aure.utils.buildFieldQueryString
import com.example.aure.utils.buildValueQueryString
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.annotation.Resource
import javax.sql.DataSource


@Repository
class WeatherKitDaoImpl {

    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Resource(name = "aure-db")
    fun setDataSource(dataSource: DataSource?) {
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource!!)
    }

    fun createWeatherKit(catchreportId: Int, weatherKit: Weather) {
        val weatherKitMap = weatherKit.buildWeatherKitDatabaseMap().toMutableMap()
        weatherKitMap["catchreport_id"] = catchreportId
        namedParameterJdbcTemplate.query(
            POST_QUERY(
                Weather.getWeatherKitDatabaseFields()
            ),
            weatherKitMap
        ) {}
    }

    companion object {
        fun POST_QUERY(
            weatherFieldList: List<String>
        ): String = """
            INSERT INTO ${Tables.WEATHER}
                (${buildFieldQueryString(weatherFieldList, prefix = "", postfix = "")})
            VALUES
                (${buildValueQueryString(weatherFieldList, prefix = "", postfix = "")})
            RETURNING id
        """.trimIndent()


    }
}