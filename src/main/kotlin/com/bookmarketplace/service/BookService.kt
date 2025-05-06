package com.bookmarketplace.service

import com.bookmarketplace.enums.BookStatus
import com.bookmarketplace.enums.Errors
import com.bookmarketplace.exception.NotFoundException
import com.bookmarketplace.model.BookModel
import com.bookmarketplace.model.CustomerModel
import com.bookmarketplace.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    val bookRepository: BookRepository
) {
    fun create(book: BookModel) {
        bookRepository.save(book)
    }

    fun findAll(pageable: Pageable): Page<BookModel> {
        return bookRepository.findAll(pageable)
    }

    fun findActives(pageable: Pageable): Page<BookModel> {
        return bookRepository.findByStatus(BookStatus.ACTIVE, pageable)
    }

    fun findById(id: Int): BookModel {
        return bookRepository.findById(id).orElseThrow { NotFoundException(Errors.BM20000.message.format(id), Errors.BM20000.code) }
    }

    fun delete(id: Int) {
        val book = findById(id)
        book.status = BookStatus.DELETED
        update(book)
    }

    fun update(book: BookModel) {
        bookRepository.save(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        var books = bookRepository.findByCustomer(customer)
        for(book in books) {
            book.status = BookStatus.DELETED
        }
        bookRepository.saveAll(books)
    }

    fun findAllByIds(bookIds: Set<Int>): List<BookModel> {
        return bookRepository.findAllById(bookIds).toList()
    }

    fun purchase(books: MutableList<BookModel>) {
        books.map { it.status = BookStatus.SOLD }
        bookRepository.saveAll(books)
    }
}
