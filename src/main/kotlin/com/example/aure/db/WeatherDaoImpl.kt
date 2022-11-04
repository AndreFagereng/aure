package com.example.aure.db

import com.example.aure.model.Weather.UvIndex
import com.example.aure.model.Weather.Weather
import com.example.aure.model.Weather.Wind
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.annotation.Resource
import javax.sql.DataSource

@Repository
class WeatherDaoImpl {

    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Resource(name = "aure-db")
    fun setDataSource(dataSource: DataSource?) {
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource!!)
    }

    fun getWeather(ids: List<Int>): List<Weather> {
        val weatherResult =
            namedParameterJdbcTemplate.query(
                getWeatherQuery,
                mapOf<String, List<Int>>("catchreport_id" to ids)
            ) { rs: ResultSet, _ ->
                Weather(
                    rs.getInt("catchreport_id"),
                    rs.getBigDecimal("temperature"),
                    rs.getBigDecimal("apparentTemperature"),
                    Wind(
                        rs.getBigDecimal("wind_direction"),
                        rs.getString("wind_compassDirection"),
                        rs.getBigDecimal("wind_gust")
                    ),
                    UvIndex(
                        rs.getBigDecimal("uvindex_value"),
                        rs.getString("uvindex_category")
                    ),
                    rs.getBigDecimal("visibility"),
                    rs.getBoolean("isDayLight"),
                    rs.getBigDecimal("humidity"),
                    rs.getBigDecimal("dewPoint"),
                    rs.getBigDecimal("pressure"),
                    rs.getString("pressureTrend"),
                    rs.getString("symbolName")
                )
            }
        return weatherResult
    }

    fun createWeather(user_id: String, catchreport_id: Int, weather: Weather) {
        namedParameterJdbcTemplate.update(
            createWeatherQuery, mapOf(
            "catchreport_id" to catchreport_id,
            "user_id" to user_id,
            "temperature" to weather.temperature,
            "apparentTemperature" to weather.apparentTemperature,
            "wind_direction" to weather.wind.direction,
            "wind_compassDirection" to weather.wind.compassDirection,
            "wind_gust" to weather.wind.gust,
            "uvindex_value" to weather.uvIndex.value,
            "uvindex_category" to weather.uvIndex.category,
            "visibility" to weather.visibility,
            "isDayLight" to weather.isDayLight,
            "humidity" to weather.humidity,
            "dewPoint" to weather.dewPoint,
            "pressure" to weather.pressure,
            "pressureTrend" to weather.pressureTrend,
            "symbolName" to weather.symbolName
        ))
    }

    companion object {
        private val weatherTable: String = "aure.weather"

        // Selects from weather on catchreport_id (linked to catchreport id key)
        val getWeatherQuery = """ SELECT * FROM $weatherTable where catchreport_id in (:catchreport_id)"""

        // Inserts row into weather
        val createWeatherQuery =
            """ INSERT INTO $weatherTable
                 (catchreport_id, user_id, temperature, apparentTemperature, wind_direction, 
                 wind_compassDirection, wind_gust, uvindex_value, uvindex_category, visibility, 
                 isDayLight, humidity, dewPoint, pressure, pressureTrend, symbolName)
            VALUES (:catchreport_id, :user_id, :temperature, :apparentTemperature, :wind_direction, 
            :wind_compassDirection, :wind_gust, :uvindex_value, :uvindex_category, :visibility, 
            :isDayLight, :humidity, :dewPoint, :pressure, :pressureTrend, :symbolName) """.trimMargin()
    }


}