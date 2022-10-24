package com.example.aure.service

import com.example.aure.db.CatchDaoImpl
import com.example.aure.db.WeatherDaoImpl
import com.example.aure.model.Catch
import com.example.aure.model.CatchReport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CatchReportService {

    @Autowired
    private lateinit var catchDaoImpl: CatchDaoImpl

    @Autowired
    private lateinit var weatherDaoImpl: WeatherDaoImpl


    fun getCatchReport(): List<CatchReport>{
        val catchResult = catchDaoImpl.getCatchDB()
        val catchIds = catchResult.map { catch -> catch.id }

        if (catchIds.isEmpty()) {
            println("getCatchReport -> No catches fetched..")
            return emptyList()
        }

        println(catchIds)

        val weatherResult = weatherDaoImpl.getWeatherDB(catchIds)
        println(catchIds)
        println(weatherResult)
        return catchIds.map { id ->
            CatchReport(
                catch = catchResult.single{catch -> catch.id == id},
                weather = weatherResult.single{weather -> weather.catchreport_id == id}
            )
        }
    }

    fun createCatchReport(catchReport: CatchReport): String {
        val createCatchResultId = catchDaoImpl.createCatchDB(catchReport.catch)

        if (createCatchResultId == -1) {
            return "Failed"
        }

        weatherDaoImpl.createWeatherDB(catchReport.weather, createCatchResultId)


        return "Success"
    }

}