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

    @GetMapping(produces = ["application/json"])
    private fun getCatchReport(
        principal: JwtAuthenticationToken,
        @RequestParam(value = "size", required = true) size: Int,
        @RequestParam(value = "at", required = true) at: Int
    ): List<CatchReport> {

        return catchReportService.getCatchReport(principal.name, at, size)
    }

    @PostMapping(consumes = ["application/json"])
    fun createCatchReport(principal: JwtAuthenticationToken, @RequestBody catchReport: CatchReport): String {
        catchReportService.createCatchReport(principal.name, catchReport)
        return "OK!"
    }

    @PutMapping(consumes = ["application/json"])
    private fun updateCatchReport(principal: JwtAuthenticationToken, @RequestBody catchReport: CatchReport): String {
        catchReportService.updateCatchReport(principal.name, catchReport)
        return "OK!"
    }


}

