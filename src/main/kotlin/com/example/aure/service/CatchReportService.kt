package com.example.aure.service

import com.example.aure.db.CatchReportDaoImpl
import com.example.aure.db.ImageDaoImpl
import com.example.aure.model.Catch.CatchReport
import com.example.aure.model.Catch.Image
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

            // Get Image ID and filepath in S3.
            val listOfImageMap = imageService.getImageFilepath(cr.images)

            if (listOfImageMap.isNotEmpty()) {

                // Get possibly multiple pre-signed URLs per catch-report
                val imageList = listOfImageMap.map {
                    Image(
                        image_id = it["id"]!!.toInt(),
                        image_url = imageService.getPresignedImage(it["filepath"]!!)
                    )
                }

                // Update catch-report List<Image>
                cr.images = imageList
            }
        }
        return catchReports
    }

    fun createCatchReport(user_id: String, catchReport: CatchReport) {
        catchReportDaoImpl.createCatchReport(user_id, catchReport)
    }

    fun updateCatchReport(user_id: String, catchReport: CatchReport) {
        catchReportDaoImpl.updateCatchReport(user_id, catchReport)
    }


}