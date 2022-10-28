package com.example.aure.controller

import com.example.aure.model.User.UserProfile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/getUserProfile")
    fun getUserProfile(): UserProfile {
        TODO()
    }

    @GetMapping("/createUserProfile")
    fun createUserProfile(userProfile: UserProfile) {
        TODO()
    }

    @PostMapping("/updateUserProfile")
    fun updateUserProfile(): UserProfile {
        TODO()
    }

}