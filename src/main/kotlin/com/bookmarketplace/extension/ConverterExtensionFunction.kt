package com.bookmarketplace.extension

import com.bookmarketplace.controller.request.PostCustomerRequest
import com.bookmarketplace.controller.request.PutCustomerRequest
import com.bookmarketplace.model.CustomerModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(name = this.name, email = this.email)
}

fun PutCustomerRequest.toCustomerModel(id: Int): CustomerModel {
    return CustomerModel(id = id, name = this.name, email = this.email)
}
