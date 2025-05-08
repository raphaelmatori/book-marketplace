# Security Documentation

## Overview

The Book Marketplace application implements a comprehensive security framework using Spring Security and JWT (JSON Web Tokens) for authentication and authorization.

## Authentication

### JWT Implementation

The application uses JWT for stateless authentication. The token contains the following claims:

```json
{
    "sub": "user@example.com",
    "roles": ["ROLE_USER"],
    "exp": 1739894400,
    "iat": 1739890800
}
```

### Token Generation

1. User submits credentials
2. Server validates credentials
3. Server generates JWT with:
   - Subject (user email)
   - Roles
   - Expiration time (1 hour)
   - Issued at time

### Token Validation

The `JwtAuthenticationFilter` validates tokens by:
1. Extracting token from Authorization header
2. Verifying signature
3. Checking expiration
4. Loading user details
5. Setting authentication in SecurityContext

## Authorization

### Role-Based Access Control

The application implements the following roles:

- `ROLE_USER`: Basic user privileges
- `ROLE_ADMIN`: Administrative privileges

### Endpoint Security

```kotlin
@Configuration
@EnableWebSecurity
class SecurityConfig {
    fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/books/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
    }
}
```

## Password Security

### Password Hashing

Passwords are hashed using BCrypt with:
- 12 rounds of hashing
- Random salt generation
- Secure comparison

```kotlin
@Bean
fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder(12)
}
```

## CORS Configuration

```kotlin
@Bean
fun corsConfigurationSource(): CorsConfigurationSource {
    val configuration = CorsConfiguration()
    configuration.allowedOrigins = listOf("https://bookmarketplace.com")
    configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
    configuration.allowedHeaders = listOf("Authorization", "Content-Type")
    configuration.allowCredentials = true
    
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", configuration)
    return source
}
```

## Security Headers

The application implements the following security headers:

```kotlin
@Bean
fun securityHeadersFilter(): Filter {
    return object : OncePerRequestFilter() {
        override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
        ) {
            response.setHeader("X-Content-Type-Options", "nosniff")
            response.setHeader("X-Frame-Options", "DENY")
            response.setHeader("X-XSS-Protection", "1; mode=block")
            response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
            filterChain.doFilter(request, response)
        }
    }
}
```

## Rate Limiting

API endpoints are protected by rate limiting:

```kotlin
@Bean
fun rateLimiter(): RateLimiter {
    return RateLimiter.create(100.0) // 100 requests per minute
}
```

## Security Best Practices

1. **Input Validation**
   - All user inputs are validated
   - SQL injection prevention
   - XSS protection

2. **Error Handling**
   - Generic error messages
   - No sensitive information in logs
   - Proper exception handling

3. **Session Management**
   - Stateless authentication
   - Token expiration
   - Secure cookie handling

4. **Audit Logging**
   - Security events logging
   - User actions tracking
   - Error logging

## Security Testing

The application includes security tests:

```kotlin
@Test
fun `test authentication with valid credentials`() {
    // Test implementation
}

@Test
fun `test authorization for protected endpoints`() {
    // Test implementation
}

@Test
fun `test rate limiting`() {
    // Test implementation
}
```

## Security Checklist

- [ ] JWT implementation
- [ ] Role-based access control
- [ ] Password hashing
- [ ] CORS configuration
- [ ] Security headers
- [ ] Rate limiting
- [ ] Input validation
- [ ] Error handling
- [ ] Session management
- [ ] Audit logging
- [ ] Security testing

## Security Incident Response

In case of a security incident:

1. Identify the scope of the incident
2. Contain the affected systems
3. Investigate the root cause
4. Apply necessary fixes
5. Notify affected users
6. Document the incident
7. Review and update security measures 