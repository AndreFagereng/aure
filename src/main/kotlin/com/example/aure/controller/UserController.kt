package com.example.aure.controller

import com.example.aure.model.User.UserProfile
import com.example.aure.service.UserProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/userProfile")
class UserController {

    @Autowired
    private lateinit var userProfileService: UserProfileService

    @GetMapping
    fun getUserProfile(principal: JwtAuthenticationToken): UserProfile {
        return userProfileService.getUserProfile(principal.name)
    }

    @PostMapping
    fun createUserProfile(principal: JwtAuthenticationToken, @RequestBody userProfile: UserProfile): String {
        userProfileService.createOrUpdateUserProfile(principal.name, userProfile)
        return "OK!"
    }

    @PutMapping
    fun updateUserProfile(principal: JwtAuthenticationToken, @RequestBody userProfile: UserProfile): String {
        userProfileService.createOrUpdateUserProfile(principal.name, userProfile)
        return "OK!"
    }

}