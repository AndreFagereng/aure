package com.example.aure.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @RequestMapping("/ping")
    fun healthCheck(): String{
        return "pong"
    }
    @RequestMapping("/")
    fun check(): String{
        return "pong"
    }

    @GetMapping("/error")
    fun errorCheck(): String{
        return "error"
    }

}