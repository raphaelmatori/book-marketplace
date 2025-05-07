package com.bookmarketplace.controller.request

import com.bookmarketplace.validation.EmailAvailable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PostCustomerRequest (
    @field:NotEmpty(message = "Name must be present")
    var name: String,

    @field:Email(message = "E-mail must have a valid e-mail value")
    @EmailAvailable
    var email: String,

    @field:NotEmpty(message = "Password must be present")
    var password: String,
)
