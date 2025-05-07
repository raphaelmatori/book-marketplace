package com.bookmarketplace.security

import com.bookmarketplace.service.CustomUserDetailService
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import java.security.Key
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.expiration-ms}") private val validityInMs: Long,
    private val customUserDetailsService: CustomUserDetailService
) {
    private val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createToken(userId: Int, roles: List<String>): String {
        val now = Date()
        val expiry = Date(now.time + validityInMs)
        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("roles", roles)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userId = getSubject(token).toInt()
        val userDetails: CustomUserDetails = customUserDetailsService.loadUserById(userId)
        return UsernamePasswordAuthenticationToken(userDetails, token, userDetails.authorities)
    }

    fun getSubject(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (_: JwtException) {
            false
        }
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearer = request.getHeader("Authorization") ?: return null
        return if (bearer.startsWith("Bearer ")) bearer.substring(7) else null
    }
}
