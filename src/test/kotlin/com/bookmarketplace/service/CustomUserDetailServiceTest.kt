package com.bookmarketplace.service

import com.bookmarketplace.model.CustomerModel
import com.bookmarketplace.repository.CustomerRepository
import com.bookmarketplace.security.CustomUserDetails
import com.bookmarketplace.enums.Profile
import com.bookmarketplace.enums.CustomerStatus
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import io.mockk.junit5.MockKExtension
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomUserDetailServiceTest {
    private lateinit var customerRepository: CustomerRepository
    private lateinit var customUserDetailService: CustomUserDetailService

    private fun defaultCustomer() = CustomerModel(
        id = 1,
        name = "User",
        email = "user@example.com",
        status = CustomerStatus.ACTIVE,
        password = "pass",
        roles = setOf(Profile.CUSTOMER)
    )

    @BeforeEach
    fun setUp() {
        customerRepository = mockk(relaxed = true)
        customUserDetailService = CustomUserDetailService(customerRepository)
    }

    @Test
    fun `should load user by username`() {
        val customer = defaultCustomer()
        every { customerRepository.findByEmail("user@example.com") } returns customer
        val userDetails = customUserDetailService.loadUserByUsername("user@example.com") as CustomUserDetails
        assertEquals(customer.id, userDetails.id)
        assertEquals(customer.email, userDetails.username)
        assertEquals(customer.password, userDetails.password)
        assertTrue(userDetails.authorities.contains(SimpleGrantedAuthority("ROLE_CUSTOMER")))
    }

    @Test
    fun `should throw UsernameNotFoundException when user not found by username`() {
        every { customerRepository.findByEmail("notfound@example.com") } returns null
        assertThrows(UsernameNotFoundException::class.java) {
            customUserDetailService.loadUserByUsername("notfound@example.com")
        }
    }

    @Test
    fun `should load user by id`() {
        val customer = defaultCustomer()
        every { customerRepository.findById(1) } returns Optional.of(customer)
        val userDetails = customUserDetailService.loadUserById(1)
        assertEquals(customer.id, userDetails.id)
        assertEquals(customer.email, userDetails.username)
        assertEquals(customer.password, userDetails.password)
        assertTrue(userDetails.authorities.contains(SimpleGrantedAuthority("ROLE_CUSTOMER")))
    }

    @Test
    fun `should throw UsernameNotFoundException when user not found by id`() {
        every { customerRepository.findById(99) } returns Optional.empty()
        assertThrows(UsernameNotFoundException::class.java) {
            customUserDetailService.loadUserById(99)
        }
    }
}
