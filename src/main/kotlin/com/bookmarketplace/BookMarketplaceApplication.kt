package com.bookmarketplace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class BookMarketplaceApplication

fun main(args: Array<String>) {
	runApplication<BookMarketplaceApplication>(*args)
}
