package com.example.aure.service

import com.example.aure.db.UserDaoImpl
import com.example.aure.model.User.UserProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    private lateinit var userDaoImpl: UserDaoImpl

    fun getUserProfile(user_id: String): UserProfile {
        return userDaoImpl.getUserProfile(user_id)
    }

    fun upsertUserProfile(user_id: String, userProfile: UserProfile) {
        userDaoImpl.upsertUserProfile(user_id, userProfile)
    }

    fun getFriends(user_id: String) {
        userDaoImpl.getFriends(user_id)
    }
    fun addFriend(user_id: String, recipient_id: Int) {
        val sender_id = getUserId(user_id)
        userDaoImpl.addFriend(sender_id, recipient_id)
    }
    fun acceptFriend(user_id: String, recipient_id: Int) {
        val sender_id = getUserId(user_id)
        userDaoImpl.acceptFriend(sender_id, recipient_id)
    }
    fun removeFriend(user_id: String, recipient_id: Int) {
        val sender_id = getUserId(user_id)
        userDaoImpl.removeFriend(sender_id, recipient_id)
    }

    private fun getUserId(user_id: String): Int {
        return userDaoImpl.getUserProfile(user_id).id!!
    }

}