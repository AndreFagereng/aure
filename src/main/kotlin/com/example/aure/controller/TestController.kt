package com.example.aure.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/ping")
    fun pingPong(): String {
        return "pong"
    }
}