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

    @Bean
    fun getCatchFromDB(): List<Catch> {
        val catchResult = namedParameterJdbcTemplate.query(
            getCatchQuery, emptyMap<String, String>()
        ) { rs: ResultSet, _ ->
            Catch(
                rs.getInt("id"),
                rs.getString("vann"),
                rs.getString("art"),
                rs.getString("vekt"),
                rs.getString("lengde"),
                rs.getDate("tid").toLocalDate(),
                rs.getString("flue"),
                rs.getString("vaerforhold"),
                rs.getInt("vanntemp"),
                rs.getString("longitude"),
                rs.getString("latitude")
            )
        }
        return catchResult
    }

    companion object {
        val getCatchQuery: String = """ SELECT * FROM aure.fangstrapport """
    }
}