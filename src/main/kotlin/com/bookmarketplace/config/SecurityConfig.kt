package com.bookmarketplace.config

import com.bookmarketplace.security.CustomAuthenticationEntryPoint
import com.bookmarketplace.security.JwtAuthenticationFilter
import com.bookmarketplace.security.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val custonEntryPoint: CustomAuthenticationEntryPoint
) {

    private val PUBLIC_MATCHERS = arrayOf<String>()
    private val PUBLIC_GET_MATCHERS = arrayOf<String>()
    private val PUBLIC_POST_MATCHERS = arrayOf(
        "/auth/login",
        "/customers"
    )
    private val SWAGGER_WHITELIST = arrayOf(
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"
    )

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
        authConfig.authenticationManager

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(*SWAGGER_WHITELIST).permitAll()
                auth.requestMatchers(*PUBLIC_MATCHERS).permitAll()
                auth.requestMatchers(HttpMethod.GET, *PUBLIC_GET_MATCHERS).permitAll()
                auth.requestMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHERS).permitAll()
                auth.anyRequest().authenticated()
            }
            .exceptionHandling(Customizer { configurer ->
                configurer.authenticationEntryPoint(custonEntryPoint)
            })
            .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}
