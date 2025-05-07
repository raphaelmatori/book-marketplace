package com.bookmarketplace.controller

import com.bookmarketplace.controller.request.PostCustomerRequest
import com.bookmarketplace.controller.request.PutCustomerRequest
import com.bookmarketplace.controller.response.CustomerResponse
import com.bookmarketplace.extension.toCustomerModel
import com.bookmarketplace.extension.toResponse
import com.bookmarketplace.security.CustomUserDetails
import com.bookmarketplace.security.UserCanOnlyAccessTheirOwnResource
import com.bookmarketplace.service.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("customers")
class CustomerController(
    val customerService: CustomerService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid customer: PostCustomerRequest) {
        customerService.create(customer.toCustomerModel())
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    fun getAll(@RequestParam name: String?): List<CustomerResponse> {

        val authentication = SecurityContextHolder.getContext().authentication
        println("Roles: ${authentication.authorities.map { it.authority }}")

        return customerService.findAll(name).map { it.toResponse() }
    }

    @UserCanOnlyAccessTheirOwnResource
    @GetMapping("/{id}")
    fun get(@PathVariable id: Int): CustomerResponse {
        println(SecurityContextHolder.getContext().authentication.principal)
        return customerService.findById(id).toResponse()
    }

    @UserCanOnlyAccessTheirOwnResource
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
        val customerSaved = customerService.findById(id)
        customerService.update(customer.toCustomerModel(customerSaved))
    }

    @UserCanOnlyAccessTheirOwnResource
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        customerService.delete(id)
    }
}
