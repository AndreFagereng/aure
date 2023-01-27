package com.example.aure.controller


import com.example.aure.model.Catch.Image
import com.example.aure.service.ImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/image")
class ImageController {

    @Autowired
    lateinit var imageService: ImageService

    @PostMapping(produces = ["application/json"])
    fun uploadImage(principal: JwtAuthenticationToken, @RequestParam("file") multiPartFile: MultipartFile ): Image {
        return imageService.postImage(principal.name, multiPartFile.inputStream)
    }

    @GetMapping
    fun getImage(principal: JwtAuthenticationToken, @RequestParam(value = "id", required = true) id: Int): Image {
        return imageService.getImage(id)
    }

}