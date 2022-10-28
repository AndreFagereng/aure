package com.example.aure.controller

import com.example.aure.model.Catch.CatchReport
import com.example.aure.service.CatchReportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.security.Principal


@RestController
@RequestMapping("/api/catchReport")
class CatchController {

    @Autowired
    private lateinit var catchReportService: CatchReportService

    @GetMapping(produces = arrayOf("application/json"))
    private fun getCatchReport(principal: JwtAuthenticationToken): List<CatchReport> {
        println(principal.name)
        println(principal.tokenAttributes)
        return catchReportService.getCatchReport(principal.name)
    }

    @PostMapping(consumes = arrayOf("application/json"))
    fun createCatchReport(principal: JwtAuthenticationToken, @RequestBody catchReport: CatchReport): String {
        println(principal.name)
        println(principal.tokenAttributes)
        catchReportService.createCatchReport(principal.name, catchReport)
        return "OK!"
    }


    @PutMapping(consumes = arrayOf("application/json"))
    private fun updateCatchReport(principal: JwtAuthenticationToken, @RequestBody catchReport: CatchReport): String {
        println(principal.name)
        println(principal.tokenAttributes)
        println("catchReport $catchReport")
        catchReportService.updateCatchReport(principal.name, catchReport)
        return "OK!"
    }


}

