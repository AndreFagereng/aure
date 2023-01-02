package com.example.aure.service

import com.example.aure.db.FeedDaoImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FeedService {

    @Autowired
    lateinit var feedDaoImpl: FeedDaoImpl

}