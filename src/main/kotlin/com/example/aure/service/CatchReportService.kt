package com.example.aure.service

import com.example.aure.db.CatchDaoImpl
import com.example.aure.db.CatchReportDaoImpl
import com.example.aure.model.Catch.Catch
import com.example.aure.model.Catch.CatchReport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CatchReportService {

    @Autowired
    private lateinit var catchDaoImpl: CatchDaoImpl

    @Autowired
    private lateinit var catchReportDaoImpl: CatchReportDaoImpl

    fun getCatchReport(user_id: String): List<CatchReport> {
        return catchReportDaoImpl.getCatchReport(user_id)
    }

    fun createCatchReport(user_id: String, catchReport: CatchReport) {
        catchReportDaoImpl.createCatchReport(user_id, catchReport)
    }

    fun updateCatchReport(user_id: String, catchReport: CatchReport) {
        catchReportDaoImpl.updateCatchReport(user_id, catchReport)
    }


    fun getCatch(user_id: String): List<Catch>{
        return catchDaoImpl.getCatch(user_id)
    }

    fun createCatch(user_id: String, catchReport: CatchReport) {
        catchDaoImpl.createCatch(user_id, catchReport.catch)
    }

}