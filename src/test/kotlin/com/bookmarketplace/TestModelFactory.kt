package com.bookmarketplace

import com.bookmarketplace.enums.BookStatus
import com.bookmarketplace.enums.CustomerStatus
import com.bookmarketplace.enums.Profile
import com.bookmarketplace.model.BookModel
import com.bookmarketplace.model.CustomerModel
import com.bookmarketplace.model.PurchaseModel
import java.math.BigDecimal

object TestModelFactory {
    fun customer(
        id: Int = 1,
        name: String = "Customer",
        email: String = "c@c.com",
        status: CustomerStatus = CustomerStatus.ACTIVE,
        password: String = "pass",
        roles: Set<Profile> = setOf(Profile.CUSTOMER)
    ) = CustomerModel(id, name, email, status, password, roles)

    fun book(
        id: Int = 1,
        name: String = "Book",
        price: BigDecimal = BigDecimal.TEN,
        customer: CustomerModel = customer(),
        status: BookStatus = BookStatus.ACTIVE
    ) = BookModel(id, name, price, customer, status)

    fun purchase(
        id: Int = 1,
        customer: CustomerModel = customer(),
        books: MutableList<BookModel> = mutableListOf(book()),
        invoice: String = "INV-001",
        price: BigDecimal = BigDecimal.TEN
    ) = PurchaseModel(id, customer, books, invoice, price)
} 