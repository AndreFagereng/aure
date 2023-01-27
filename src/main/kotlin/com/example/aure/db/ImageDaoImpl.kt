package com.example.aure.db

import com.example.aure.db.utils.Tables
import com.example.aure.model.Catch.Image
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.annotation.Resource
import javax.sql.DataSource


@Repository
class ImageDaoImpl {

    lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Resource(name = "aure-db")
    fun setDataSource(dataSource: DataSource?) {
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource!!)
    }

    fun storeImageFilepath(filePath: String): Int? {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val sqlParameterSource: SqlParameterSource = MapSqlParameterSource()
            .addValue("filepath", filePath)
        namedParameterJdbcTemplate.update(STORE_IMAGE_QUERY, sqlParameterSource, keyHolder, arrayOf("id"))

        return keyHolder.key?.toInt()
    }

    fun getImage(id: Int): Map<String, String> {
        val parameters = mapOf("id" to id)
        val res =  namedParameterJdbcTemplate.query(GET_SINGLE_IMAGE_QUERY, parameters) { rs: ResultSet, _ ->
            mapOf(
                "id" to rs.getString("id"),
                "filepath" to rs.getString("filepath")
            )
        }
        return res.first()
    }

    fun getImagesFilepath(ids: List<Image>): List<Map<String, String>> {
        val imageMap: Map<String, Any> = mapOf("ids" to ids.map { it.image_id })
        return namedParameterJdbcTemplate.query(GET_IMAGES_QUERY, imageMap) { rs: ResultSet, _ ->
            mapOf(
                "id" to rs.getString("id"),
                "filepath" to rs.getString("filepath")
            )
        }
    }

    companion object {
        val STORE_IMAGE_QUERY = """
            INSERT INTO ${Tables.IMAGE_STORE} (filepath)
            VALUES (:filepath)
        """.trimIndent()

        val GET_IMAGES_QUERY = """
            SELECT * FROM ${Tables.IMAGE_STORE}
            WHERE id IN (:ids)
        """.trimIndent()

        val GET_SINGLE_IMAGE_QUERY = """
            SELECT * FROM ${Tables.IMAGE_STORE}
            WHERE id = (:id)
        """.trimIndent()
    }


}
