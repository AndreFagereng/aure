package com.example.aure.controller

import com.example.aure.model.User.UserProfile
import com.example.aure.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    // USERS
    @GetMapping
    fun getUserProfile(principal: JwtAuthenticationToken): UserProfile {
        return userService.getUserProfile(principal.name)
    }

    @PostMapping
    fun createUserProfile(principal: JwtAuthenticationToken, @RequestBody userProfile: UserProfile): String {
        userService.upsertUserProfile(principal.name, userProfile)
        return "OK!"
    }

    @PutMapping
    fun updateUserProfile(principal: JwtAuthenticationToken, @RequestBody userProfile: UserProfile): String {
        userService.upsertUserProfile(principal.name, userProfile)
        return "OK!"
    }

    // FRIENDS
    @GetMapping("/friends")
    fun getFriends(principal: JwtAuthenticationToken): String {
        userService.getFriends(principal.name)
        return "OK!"
    }
    @GetMapping("/friends/add")
    fun addFriend(
        principal: JwtAuthenticationToken,
        @RequestParam(value = "id", required = true) id: Int
    ): String {
        userService.addFriend(principal.name, id)
        return "OK!"
    }

    @GetMapping("/friends/accept")
    fun acceptFriend(
        principal: JwtAuthenticationToken,
        @RequestParam(value = "id", required = true) id: Int
    ): String {
        userService.acceptFriend(principal.name, id)
        return "OK!"
    }
    @GetMapping("/friends/remove")
    fun removeFriend(
        principal: JwtAuthenticationToken,
        @RequestParam(value = "id", required = true) id: Int
    ): String {
        userService.removeFriend(principal.name, id)
        return "OK!"
    }


}