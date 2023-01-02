package com.example.aure.db

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.annotation.Resource
import javax.sql.DataSource

@Repository
class FeedDaoImpl {

    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Resource(name = "aure-db")
    fun setDataSource(dataSource: DataSource?) {
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource!!)
    }



}