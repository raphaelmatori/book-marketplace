package com.bookmarketplace.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PutCustomerRequest (
    @field:NotEmpty(message = "Name must be present")
    var name: String,
    @field:Email(message = "E-mail must have a valid e-mail value")
    var email: String
)
