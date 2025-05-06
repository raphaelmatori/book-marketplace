package com.bookmarketplace.controller.response

import com.bookmarketplace.enums.Errors

data class ErrorResponse(
    var httpCode: Int,
    var message: String,
    var internalCode: String,
    var errors: List<FieldErrorResponse>?
)
