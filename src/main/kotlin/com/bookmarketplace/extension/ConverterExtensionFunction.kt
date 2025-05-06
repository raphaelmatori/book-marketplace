package com.bookmarketplace.extension

import com.bookmarketplace.controller.request.PostBookRequest
import com.bookmarketplace.controller.request.PostCustomerRequest
import com.bookmarketplace.controller.request.PutBookRequest
import com.bookmarketplace.controller.request.PutCustomerRequest
import com.bookmarketplace.controller.response.BookResponse
import com.bookmarketplace.controller.response.CustomerResponse
import com.bookmarketplace.enums.BookStatus
import com.bookmarketplace.enums.CustomerStatus
import com.bookmarketplace.model.BookModel
import com.bookmarketplace.model.CustomerModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(name = this.name, email = this.email, status = CustomerStatus.ACTIVE)
}

fun PutCustomerRequest.toCustomerModel(previousValue: CustomerModel): CustomerModel {
    return CustomerModel(
        id = previousValue.id,
        name = this.name,
        email = this.email,
        status = previousValue.status
    )
}

fun CustomerModel.toResponse(): CustomerResponse {
    return CustomerResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        status = this.status
    )
}

fun PostBookRequest.toBookModel(customer: CustomerModel): BookModel {
    return BookModel(
        name = this.name,
        price = this.price,
        status = BookStatus.ACTIVE,
        customer = customer
    )
}

fun PutBookRequest.toBookModel(previousValue: BookModel): BookModel {
    return BookModel(
        id = previousValue.id,
        name = this.name ?: previousValue.name,
        price = this.price ?: previousValue.price,
        status = previousValue.status,
        customer = previousValue.customer
    )
}

fun BookModel.toResponse(): BookResponse {
    return BookResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        customer = this.customer,
        status = this.status
    )
}
