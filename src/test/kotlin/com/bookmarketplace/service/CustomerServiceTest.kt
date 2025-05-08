package com.bookmarketplace.service

import com.bookmarketplace.enums.CustomerStatus
import com.bookmarketplace.enums.Profile
import com.bookmarketplace.exception.NotFoundException
import com.bookmarketplace.model.CustomerModel
import com.bookmarketplace.repository.CustomerRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.junit5.MockKExtension
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
    private lateinit var customerRepository: CustomerRepository
    private lateinit var bookService: BookService
    private lateinit var bCrypt: BCryptPasswordEncoder

    private lateinit var customerService: CustomerService

    private fun defaultCustomer() = CustomerModel(
        id = 1,
        name = "Customer",
        email = "c@c.com",
        status = CustomerStatus.ACTIVE,
        password = "pass",
        roles = setOf(Profile.CUSTOMER)
    )

    @BeforeEach
    fun setUp() {
        customerRepository = mockk()
        bookService = mockk()
        bCrypt = mockk()
        customerService = CustomerService(customerRepository, bookService, bCrypt)
    }

    @Test
    fun `should create a customer with encoded password and CUSTOMER role`() {
        val customer = defaultCustomer()
        every { bCrypt.encode("pass") } returns "encodedPass"
        every { customerRepository.save(any()) } returns customer.copy(password = "encodedPass", roles = setOf(Profile.CUSTOMER))
        customerService.create(customer)
        verify { customerRepository.save(match { it.password == "encodedPass" && it.roles == setOf(Profile.CUSTOMER) }) }
    }

    @Test
    fun `should find customer by id`() {
        val customer = defaultCustomer()
        every { customerRepository.findById(1) } returns Optional.of(customer)
        val result = customerService.findById(1)
        assertEquals(customer, result)
        verify { customerRepository.findById(1) }
    }

    @Test
    fun `should throw NotFoundException when customer not found`() {
        every { customerRepository.findById(99) } returns Optional.empty()
        val ex = assertThrows(NotFoundException::class.java) {
            customerService.findById(99)
        }
        assertTrue(ex.message.contains("99"))
    }

    @Test
    fun `should find all customers by name`() {
        val customers = listOf(defaultCustomer())
        every { customerRepository.findByNameContainingIgnoreCase("Cust") } returns customers
        val result = customerService.findAll("Cust")
        assertEquals(customers, result)
        verify { customerRepository.findByNameContainingIgnoreCase("Cust") }
    }

    @Test
    fun `should find all customers when name is null`() {
        val customers = listOf(defaultCustomer())
        every { customerRepository.findAll() } returns customers
        val result = customerService.findAll(null)
        assertEquals(customers, result)
        verify { customerRepository.findAll() }
    }

    @Test
    fun `should update customer if exists`() {
        val customer = defaultCustomer()
        every { customerRepository.existsById(1) } returns true
        every { customerRepository.save(customer) } returns customer
        customerService.update(customer)
        verify { customerRepository.save(customer) }
    }

    @Test
    fun `should throw Exception when updating non-existent customer`() {
        val customer = defaultCustomer().copy(id = 99)
        every { customerRepository.existsById(99) } returns false
        assertThrows(Exception::class.java) {
            customerService.update(customer)
        }
    }

    @Test
    fun `should delete customer and set status to INACTIVE`() {
        val customer = defaultCustomer().copy(status = CustomerStatus.ACTIVE)
        every { customerRepository.findById(1) } returns Optional.of(customer)
        every { bookService.deleteByCustomer(customer) } just Runs
        every { customerRepository.save(customer) } returns customer.copy(status = CustomerStatus.INACTIVE)
        customerService.delete(1)
        assertEquals(CustomerStatus.INACTIVE, customer.status)
        verify { bookService.deleteByCustomer(customer) }
        verify { customerRepository.save(customer) }
    }

    @Test
    fun `should return true if email is available`() {
        every { customerRepository.existsByEmail("c@c.com") } returns false
        val result = customerService.isEmailAvailable("c@c.com")
        assertTrue(result)
    }

    @Test
    fun `should return false if email is not available`() {
        every { customerRepository.existsByEmail("c@c.com") } returns true
        val result = customerService.isEmailAvailable("c@c.com")
        assertFalse(result)
    }
}
