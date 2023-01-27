package com.example.aure.service

import com.example.aure.db.CatchReportDaoImpl
import com.example.aure.db.ImageDaoImpl
import com.example.aure.model.Catch.CatchReport
import com.example.aure.model.Catch.Image
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CatchReportService {

    @Autowired
    private lateinit var catchReportDaoImpl: CatchReportDaoImpl

    @Autowired
    private lateinit var imageService: ImageService

    fun getCatchReport(user_id: String, at: Int, size: Int): List<CatchReport> {
        val catchReports = catchReportDaoImpl.getCatchReport(user_id, at, size)

        for (cr in catchReports) {
            val listOfImageMap = imageService.getImageFilepath(cr.images)

            if (listOfImageMap.isNotEmpty()) {
                // Get possibly multiple pre-signed URLs per catch-report
                val imageList = listOfImageMap.map {
                    Image(
                        image_id = it["id"]!!.toInt(),
                        image_url = imageService.getPresignedImage(
                            imageService.toThumbnailPath(it["filepath"]!!)
                        )
                    )
                }
                cr.images = imageList
            }
        }
        return catchReports
    }

    fun createCatchReport(user_id: String, catchReport: CatchReport): Int {
        return catchReportDaoImpl.createCatchReport(user_id, catchReport)
    }

    fun updateCatchReport(user_id: String, catchReport: CatchReport) {
        catchReportDaoImpl.updateCatchReport(user_id, catchReport)
    }


}