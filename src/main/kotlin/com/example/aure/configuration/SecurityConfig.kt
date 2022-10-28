package com.example.aure.configuration

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfiguration {

    @Throws(java.lang.Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .cors().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests(
                Customizer { configurer ->
                    configurer
                        .antMatchers("/ping")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                }
            ) // Enable JWT Authentication
            .oauth2ResourceServer().jwt()


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
