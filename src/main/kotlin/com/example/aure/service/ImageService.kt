package com.example.aure.service

import com.example.aure.db.ImageDaoImpl
import com.example.aure.model.Catch.Image
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest
import java.io.InputStream
import java.time.Duration
import java.util.*


@Service
class ImageService {

    @Autowired
    lateinit var s3Client: S3Client

    @Autowired
    lateinit var imageDaoImpl: ImageDaoImpl

    val BUCKET: String = "aure-image-storage"
    val URL_DURATION: Long = 5

    fun postImage(user_id: String, inputStream: InputStream): Image {

        // Uploads image to S3
        val s3FilePath = uploadImageFileToS3(user_id, inputStream)

        // Stores s3FilePath to RDS and returns row ID
        val imageId = saveFilePathToRDS(s3FilePath) ?: throw Exception(
            "ImageService.postImage : RDS returned null as imageId "
        )

        return Image(imageId, s3FilePath)
//        imageId?.let {
//            return Image(imageId, s3FilePath)
//        } ?: run {
//            throw Exception("ImageService.uploadImageFile --> ImageId is null")
//        }
    }

    private fun createPutObjectRequest(bucket: String, filePath: String): PutObjectRequest {
        return PutObjectRequest
            .builder()
            .bucket(bucket)
            .key(filePath)
            .build()
    }

    private fun uploadImageFileToS3(user_id: String, inputStream: InputStream): String {

        val uniqueFilename = UUID.randomUUID().toString()
        val filePath = "$user_id/$uniqueFilename.jpg"
        val request = createPutObjectRequest(BUCKET, filePath)

        // TODO Create a S3ClientWaiter instead to verify the putObject call
        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available().toLong()))
        return filePath

    }

    fun getImageFilepath(imageIds: List<Image>): List<Map<String, String>> {
        return imageDaoImpl.getImageFilepath(imageIds)
    }

    private fun saveFilePathToRDS(filePath: String): Int? {
        return imageDaoImpl.storeImageFilepath(filePath)
    }

    fun getPresignedImage(key: String): String {
        val getObjectRequest: GetObjectRequest = GetObjectRequest
            .builder()
            .bucket(BUCKET)
            .key(key)
            .build()

        val s3Presigner: S3Presigner = S3Presigner.create()

        val getObjectPresignRequest = GetObjectPresignRequest
            .builder()
            .signatureDuration(Duration.ofMinutes(URL_DURATION))
            .getObjectRequest(getObjectRequest)
            .build()

        val presignedGetObjectRequest: PresignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest)

        return presignedGetObjectRequest.url().toString()
    }
}