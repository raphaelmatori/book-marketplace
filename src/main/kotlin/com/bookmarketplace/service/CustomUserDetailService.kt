package com.bookmarketplace.service

import com.bookmarketplace.enums.Errors
import com.bookmarketplace.enums.CustomerStatus
import com.bookmarketplace.enums.Profile
import com.bookmarketplace.exception.NotFoundException
import com.bookmarketplace.model.CustomerModel
import com.bookmarketplace.repository.CustomerRepository
import com.bookmarketplace.security.CustomUserDetails
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    val customerRepository: CustomerRepository): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val customer = customerRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found")
        return CustomUserDetails(
            id = customer.id!!,
            email = customer.email,
            password = customer.password,
            authorities = customer.roles!!.map { SimpleGrantedAuthority("ROLE_$it") }
        )
    }

    fun loadUserById(id: Int): CustomUserDetails {
        val customer = customerRepository.findById(id).orElseThrow {
            UsernameNotFoundException("User not found")
        }

        customer.roles!!.map { println(it) }

        return CustomUserDetails(
            id = customer.id!!,
            email = customer.email,
            password = customer.password,
            authorities = customer.roles!!.map { SimpleGrantedAuthority("ROLE_$it") }
        )
    }
}
