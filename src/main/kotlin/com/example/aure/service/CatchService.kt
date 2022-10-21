package com.example.aure.service

import com.example.aure.db.CatchDaoImpl
import com.example.aure.model.Catch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CatchService {

    @Autowired
    private lateinit var catchDaoImpl: CatchDaoImpl

    @Bean
    fun getCatch(): List<Catch>{
        val catchResult = catchDaoImpl.getCatchFromDB()
        return catchResult
    }
}