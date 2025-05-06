package com.bookmarketplace.repository

import com.bookmarketplace.model.CustomerModel
import org.springframework.data.repository.CrudRepository

interface CustomerRepository: CrudRepository<CustomerModel, Int> {
    fun findByNameContainingIgnoreCase(name: String): List<CustomerModel>
    fun existsByEmail(email: String): Boolean
}
