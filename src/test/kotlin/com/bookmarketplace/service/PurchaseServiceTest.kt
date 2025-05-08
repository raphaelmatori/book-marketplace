package com.bookmarketplace.service

import com.bookmarketplace.events.PurchaseEvent
import com.bookmarketplace.model.PurchaseModel
import com.bookmarketplace.model.BookModel
import com.bookmarketplace.model.CustomerModel
import com.bookmarketplace.enums.BookStatus
import com.bookmarketplace.enums.CustomerStatus
import com.bookmarketplace.enums.Profile
import com.bookmarketplace.repository.PurchaseRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import io.mockk.junit5.MockKExtension
import java.math.BigDecimal
import java.util.*

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {
    private lateinit var purchaseRepository: PurchaseRepository
    private lateinit var applicationEventPublisher: ApplicationEventPublisher
    private lateinit var purchaseService: PurchaseService

    private fun defaultCustomer() = CustomerModel(
        id = 1,
        name = "Customer",
        email = "c@c.com",
        status = CustomerStatus.ACTIVE,
        password = "pass",
        roles = setOf(Profile.CUSTOMER)
    )

    private fun defaultBook() = BookModel(
        id = 1,
        name = "Book",
        price = BigDecimal.TEN,
        customer = defaultCustomer(),
        status = BookStatus.ACTIVE
    )

    private fun defaultPurchase() = PurchaseModel(
        id = 1,
        customer = defaultCustomer(),
        books = mutableListOf(defaultBook()),
        invoice = "INV-001",
        price = BigDecimal.TEN
    )

    @BeforeEach
    fun setUp() {
        purchaseRepository = mockk(relaxed = true)
        applicationEventPublisher = mockk(relaxed = true)
        purchaseService = PurchaseService(purchaseRepository, applicationEventPublisher)
    }

    @Test
    fun `should create a purchase and publish event`() {
        val purchase = defaultPurchase()
        every { purchaseRepository.save(purchase) } returns purchase
        every { applicationEventPublisher.publishEvent(any()) } just Runs

        purchaseService.create(purchase)

        verify { purchaseRepository.save(purchase) }
        verify { applicationEventPublisher.publishEvent(match { it is PurchaseEvent && it.purchaseModel == purchase }) }
    }

    @Test
    fun `should update a purchase`() {
        val purchase = defaultPurchase()
        every { purchaseRepository.save(purchase) } returns purchase
        purchaseService.update(purchase)
        verify { purchaseRepository.save(purchase) }
    }
}
