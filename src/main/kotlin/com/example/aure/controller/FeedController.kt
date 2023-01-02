package com.example.aure.controller

import com.example.aure.service.FeedService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpClient

@RestController
@RequestMapping("api/feed")
class FeedController {

    @Autowired
    lateinit var feedService: FeedService


}