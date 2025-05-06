package com.bookmarketplace.controller.response

import com.bookmarketplace.enums.CustomerStatus

data class CustomerResponse (
    var id: Int? = null,
    var name: String,
    var email: String,
    var status: CustomerStatus
)
