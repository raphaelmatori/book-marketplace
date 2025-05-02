package com.bookmarketplace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookMarketplaceApplication

fun main(args: Array<String>) {
	runApplication<BookMarketplaceApplication>(*args)
}
