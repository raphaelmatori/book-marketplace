package com.bookmarketplace.exception

class NotFoundException(override val message: String, val errorCode: String): Exception() {
}
