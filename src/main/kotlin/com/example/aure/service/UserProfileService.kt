package com.example.aure.service

import com.example.aure.db.UserProfileDaoImpl
import com.example.aure.model.User.UserProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserProfileService {

    @Autowired
    private lateinit var userProfileDaoImpl: UserProfileDaoImpl

    fun getUserProfile(user_id: String): UserProfile {
        return userProfileDaoImpl.getUserProfile(user_id)
    }

    fun createOrUpdateUserProfile(user_id: String, userProfile: UserProfile) {
        userProfileDaoImpl.createOrUpdateUserProfile(user_id, userProfile)
    }

}