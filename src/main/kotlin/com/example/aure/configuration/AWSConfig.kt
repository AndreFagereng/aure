package com.example.aure.configuration

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.secretsmanager.AWSSecretsManager
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClient
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
}