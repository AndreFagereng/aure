package com.example.aure.controller

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/ping")
    fun pingPong(): String {
        return "pong"
    }
    @GetMapping("/api/public")
    fun a(): String {
        return "/api/public"
    }
    @GetMapping("/api/private")
    fun b(): String {
        return "/api/private"
    }
    @GetMapping("/api/private-scoped")
    fun c(): String {
        return "/api/private-scoped"
    }
}