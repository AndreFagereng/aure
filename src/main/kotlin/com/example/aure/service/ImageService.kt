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

    val originalBasePath = "original_images/"
    val thumbnailBasePath = "thumbnails/"
    val bucket: String = "aure-image-storage"
    val urlDuration: Long = 5

    fun getImage(id: Int): Image {
        val imageMap = imageDaoImpl.getImage(id)
        return Image(
            image_id = imageMap["id"]!!.toInt(),
            image_url = getPresignedImage(imageMap["filepath"]!!)
        )
    }

    fun postImage(user_id: String, inputStream: InputStream): Image {
        val s3FilePath = uploadImageFileToS3(user_id, inputStream)

        // Stores s3FilePath to RDS and returns row ID
        val imageId = saveFilePathToRDS(s3FilePath) ?: throw Exception(
            "ImageService.postImage : RDS returned null as imageId "
        )

        return Image(imageId, s3FilePath)

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
        val filePath = "original_images/$user_id/$uniqueFilename.jpg"
        val request = createPutObjectRequest(bucket, filePath)

        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available().toLong()))
        return filePath

    }

    fun getImageFilepath(imageIds: List<Image>): List<Map<String, String>> {
        return imageDaoImpl.getImagesFilepath(imageIds)
    }

    private fun saveFilePathToRDS(filePath: String): Int? {
        return imageDaoImpl.storeImageFilepath(filePath)
    }

    fun getPresignedImage(key: String): String {
        val getObjectRequest: GetObjectRequest = GetObjectRequest
            .builder()
            .bucket(bucket)
            .key(key)
            .build()

        val s3Presigner: S3Presigner = S3Presigner.create()

        val getObjectPresignRequest = GetObjectPresignRequest
            .builder()
            .signatureDuration(Duration.ofMinutes(urlDuration))
            .getObjectRequest(getObjectRequest)
            .build()

        val presignedGetObjectRequest: PresignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest)

        return presignedGetObjectRequest.url().toString()
    }

    fun toThumbnailPath(path: String): String {
        return path.replace(originalBasePath, thumbnailBasePath)
    }
}