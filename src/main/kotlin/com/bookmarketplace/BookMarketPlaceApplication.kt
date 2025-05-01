package com.bookmarketplace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookMarketPlaceApplication

fun main(args: Array<String>) {
	runApplication<BookMarketPlaceApplication>(*args)
}
