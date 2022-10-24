package com.example.aure.controller

import com.example.aure.model.Catch
import com.example.aure.model.CatchReport
import com.example.aure.service.CatchReportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
class TestController {

    @Autowired
    private lateinit var catchReportService: CatchReportService


    @GetMapping("/")
    fun ping(): String{
        return "pong"
    }

    @GetMapping("/getCatch", produces = arrayOf("application/json"))
    fun getCatch(): List<CatchReport> {
        val catchResult = catchReportService.getCatchReport()
        return catchResult
    }

    @PostMapping("/createCatchReport", consumes = arrayOf("application/json"))
    fun createCatch(@RequestBody catchReport: CatchReport): String {
        println("Catch $catchReport")
        val result = catchReportService.createCatchReport(catchReport)
        return when (result) {
            "Success" -> {
                "OK!"
            }
            "Failed" -> {
                "Failed"
            }
            else -> {
                "Unknown error"
            }
        }
    }

}

