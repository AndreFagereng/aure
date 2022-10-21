package com.example.aure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource


class DbConfig {

    @Configuration
    class DbConfig {
        @Bean(name = ["aure-db"])
        @ConfigurationProperties(prefix = "aure-dataservice")
        @Primary // nødvendig?
        fun createM2DataService(): DataSource {
            return DataSourceBuilder.create().build()
        }
    }
}