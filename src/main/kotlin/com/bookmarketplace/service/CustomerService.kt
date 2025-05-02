package com.bookmarketplace.service

import com.bookmarketplace.model.CustomerModel
import com.bookmarketplace.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository
) {

    fun create(customer: CustomerModel) {
        customerRepository.save(customer)
    }

    fun get(id: Int): CustomerModel {
        return customerRepository.findById(id).orElseThrow()
    }

    fun getAll(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContainingIgnoreCase(it)
        }
        return customerRepository.findAll().toList()
    }

    fun update(customer: CustomerModel) {
        if(!customerRepository.existsById(customer.id!!)){
            throw Exception()
        }

        customerRepository.save(customer)
    }

    fun delete(id: Int) {
        if(!customerRepository.existsById(id)){
            throw Exception()
        }

        customerRepository.deleteById(id)
    }
}
