package com.bookmarketplace.exception

class AuthenticationException(override val message: String, val errorCode: String): Exception() {
}
