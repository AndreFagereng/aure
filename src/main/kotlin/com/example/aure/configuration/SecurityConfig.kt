package com.example.aure.configuration

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfiguration {

    @Throws(java.lang.Exception::class)
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors().disable().csrf().disable().authorizeRequests()
            .antMatchers("/ping", "/")
            .permitAll()
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2ResourceServer()
            .jwt()
        return http.build()
    }
}
@Configuration
class JwtConfig {
    @Bean
    fun jwtDecoder(properties: OAuth2ResourceServerProperties): JwtDecoder {
        return NimbusJwtDecoder.withJwkSetUri(properties.jwt.jwkSetUri).jwsAlgorithm(SignatureAlgorithm.RS256).build()
    }
}
