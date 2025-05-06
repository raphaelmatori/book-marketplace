package com.bookmarketplace.controller.response

import com.bookmarketplace.enums.BookStatus
import com.bookmarketplace.model.CustomerModel
import java.math.BigDecimal

data class BookResponse (
    var id: Int? = null,
    var name: String,
    var price: BigDecimal,
    var customer: CustomerModel? = null,
    var status: BookStatus? = null
)
