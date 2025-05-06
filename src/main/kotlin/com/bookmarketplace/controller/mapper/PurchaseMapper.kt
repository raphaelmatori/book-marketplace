package com.bookmarketplace.controller.mapper

import com.bookmarketplace.controller.request.PostPurchaseRequest
import com.bookmarketplace.model.PurchaseModel
import com.bookmarketplace.service.BookService
import com.bookmarketplace.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val bookService: BookService,
    private val customerService: CustomerService
) {
    fun toModel(request: PostPurchaseRequest): PurchaseModel {
        val customer = customerService.findById(request.customerId)
        val books = bookService.findAllByIds(request.bookIds)

        return PurchaseModel(
            customer = customer,
            books = books.toMutableList(),
            price = books.sumOf { it.price }
        )
    }
}
