package com.example.aure.model.Feed

import com.example.aure.model.Catch.CatchReport
import java.time.LocalDateTime

data class Like(
    val id: Int,
    val feedId: Int,
    val nickname: String,
    val type: String
    )

data class Comment(
    val id: Int,
    val feedId: Int,
    val date: LocalDateTime,
    val nickname: String,
    val message: String
    )

data class FeedEntity (
    val id: Int,
    val date: LocalDateTime,
    val description: String,
    val likes: List<Like>,
    val comments: List<Comment>,
    val catchReport: CatchReport,
    )