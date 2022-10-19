import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.web.SecurityFilterChain


/**
 * Configures our application with Spring Security to restrict access to our API endpoints.
 */

class AudienceValidator(audience: String?) : OAuth2TokenValidator<Jwt?> {
    private val audience: String?

    init {
        this.audience = audience
    }

    override fun validate(token: Jwt?): OAuth2TokenValidatorResult {
        val error = OAuth2Error("invalid_token", "The required audience is missing", null)
        return if (token!!.audience.contains(audience)) {
            OAuth2TokenValidatorResult.success()
        } else OAuth2TokenValidatorResult.failure(error)    }
}

@EnableWebSecurity
class SecurityConfig {
    @Value("\${auth0.audience}")
    private val audience: String? = null

    @Value("\${spring.security.oauth2.client.provider.cognito.issuerUri}")
    private val issuer: String? = null
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        /*
        This is where we configure the security required for our endpoints and setup our app to serve as
        an OAuth2 Resource Server, using JWT validation.
        */
        http.authorizeRequests()
            .mvcMatchers("/api/public").permitAll()
            .mvcMatchers("/api/private").authenticated()
            .mvcMatchers("/api/private-scoped").hasAuthority("SCOPE_read:messages")
            .and().cors()
            .and().oauth2ResourceServer().jwt()
        return http.build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        /*
        By default, Spring Security does not validate the "aud" claim of the token, to ensure that this token is
        indeed intended for our app. Adding our own validator is easy to do:
        */
        val jwtDecoder = JwtDecoders.fromOidcIssuerLocation<JwtDecoder>(issuer) as NimbusJwtDecoder
        val audienceValidator: AudienceValidator = AudienceValidator(audience)
        val withIssuer = JwtValidators.createDefaultWithIssuer(issuer)
        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer, audienceValidator)
        jwtDecoder.setJwtValidator(withAudience)
        return jwtDecoder
    }
}