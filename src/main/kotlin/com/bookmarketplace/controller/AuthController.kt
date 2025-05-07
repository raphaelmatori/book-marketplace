package com.bookmarketplace.controller

import com.bookmarketplace.security.CustomUserDetails
import com.bookmarketplace.security.JwtTokenProvider
import com.bookmarketplace.service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: JwtTokenProvider,
    private val customerService: CustomerService
) {
    data class LoginRequest(val username: String, val password: String)
    data class TokenResponse(val token: String)

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<Any> {
        val authToken = UsernamePasswordAuthenticationToken(req.username, req.password)
        val auth = authenticationManager.authenticate(authToken)
        val userDetails: CustomUserDetails = auth.principal as CustomUserDetails
        val roles: List<String> = auth.authorities.map {  it.authority.removePrefix("ROLE_") }
        val jwt = tokenProvider.createToken(userDetails.id, roles)
        return ResponseEntity.ok(TokenResponse(jwt))
    }

}
