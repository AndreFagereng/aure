package com.example.aure.controller

import com.example.aure.model.Catch
import com.example.aure.service.CatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.ResultSet
import javax.annotation.Resource
import javax.sql.DataSource

@RestController
class TestController {

    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    private lateinit var catchService: CatchService


    @GetMapping("/getCatch", produces = arrayOf("application/json"))
    fun getCatch(): List<Catch> {
        val catchResult = catchService.getCatch()
        return catchResult
    }

}