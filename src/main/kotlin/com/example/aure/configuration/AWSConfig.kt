package com.example.aure.configuration

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.secretsmanager.AWSSecretsManager
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.*
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client


@Configuration
class AWSConfig {

    @Bean
    fun awsSecrestManager(): AWSSecretsManager {
        return AWSSecretsManagerClientBuilder
            .standard()
            .withCredentials(DefaultAWSCredentialsProviderChain())
            .withRegion(Regions.fromName("eu-north-1"))
            .build()
    }

    @Bean
    fun awsS3Client(): S3Client {
        val region: Region = Region.EU_NORTH_1
        return S3Client
            .builder()
            .credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
            .region(region)
            .build()
    }
}