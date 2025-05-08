package com.bookmarketplace.service

import com.bookmarketplace.enums.BookStatus
import com.bookmarketplace.exception.NotFoundException
import com.bookmarketplace.model.BookModel
import com.bookmarketplace.model.CustomerModel
import com.bookmarketplace.enums.CustomerStatus
import com.bookmarketplace.enums.Profile
import com.bookmarketplace.repository.BookRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import io.mockk.junit5.MockKExtension
import java.util.*
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class BookServiceTest {
    private lateinit var bookRepository: BookRepository
    private lateinit var bookService: BookService

    private fun defaultCustomer() = CustomerModel(
        id = 1,
        name = "Customer",
        email = "c@c.com",
        status = CustomerStatus.ACTIVE,
        password = "pass",
        roles = setOf(Profile.CUSTOMER)
    )

    @BeforeEach
    fun setUp() {
        bookRepository = mockk()
        bookService = BookService(bookRepository)
    }

    @Test
    fun `should create a book`() {
        val book = BookModel(id = 1, name = "Book", price = BigDecimal.TEN, customer = defaultCustomer(), status = BookStatus.ACTIVE)
        every { bookRepository.save(book) } returns book
        bookService.create(book)
        verify { bookRepository.save(book) }
    }

    @Test
    fun `should return all books`() {
        val pageable = mockk<Pageable>()
        val books = listOf(BookModel(id = 1, name = "Book", price = BigDecimal.TEN, customer = defaultCustomer(), status = BookStatus.ACTIVE))
        val page = PageImpl(books)
        every { bookRepository.findAll(pageable) } returns page
        val result = bookService.findAll(pageable)
        assertEquals(1, result.totalElements)
        verify { bookRepository.findAll(pageable) }
    }

    @Test
    fun `should return active books`() {
        val pageable = mockk<Pageable>()
        val books = listOf(BookModel(id = 1, name = "Book", price = BigDecimal.TEN, customer = defaultCustomer(), status = BookStatus.ACTIVE))
        val page = PageImpl(books)
        every { bookRepository.findByStatus(BookStatus.ACTIVE, pageable) } returns page
        val result = bookService.findActives(pageable)
        assertEquals(1, result.totalElements)
        verify { bookRepository.findByStatus(BookStatus.ACTIVE, pageable) }
    }

    @Test
    fun `should find book by id`() {
        val book = BookModel(id = 1, name = "Book", price = BigDecimal.TEN, customer = defaultCustomer(), status = BookStatus.ACTIVE)
        every { bookRepository.findById(1) } returns Optional.of(book)
        val result = bookService.findById(1)
        assertEquals(book, result)
        verify { bookRepository.findById(1) }
    }

    @Test
    fun `should throw NotFoundException when book not found`() {
        every { bookRepository.findById(99) } returns Optional.empty()
        val ex = assertThrows(NotFoundException::class.java) {
            bookService.findById(99)
        }
        assertTrue(ex.message.contains("99"))
    }

    @Test
    fun `should delete a book by id`() {
        val book = BookModel(id = 1, name = "Book", price = BigDecimal.TEN, customer = defaultCustomer(), status = BookStatus.ACTIVE)
        every { bookRepository.findById(1) } returns Optional.of(book)

        val deletedBook = book.copy()
        deletedBook.status = BookStatus.DELETED
        every { bookRepository.save(any()) } returns deletedBook

        bookService.delete(1)
        assertEquals(BookStatus.DELETED, book.status)
        verify { bookRepository.save(book) }
    }

    @Test
    fun `should update a book`() {
        val book = BookModel(id = 1, name = "Book", price = BigDecimal.TEN, customer = defaultCustomer(), status = BookStatus.ACTIVE)
        every { bookRepository.save(book) } returns book
        bookService.update(book)
        verify { bookRepository.save(book) }
    }

    @Test
    fun `should delete books by customer`() {
        val customer = defaultCustomer()
        val books = mutableListOf(BookModel(id = 1, name = "Book", price = BigDecimal.TEN, customer = customer, status = BookStatus.ACTIVE))
        every { bookRepository.findByCustomer(customer) } returns books
        every { bookRepository.saveAll(books) } returns books
        bookService.deleteByCustomer(customer)
        assertEquals(BookStatus.DELETED, books[0].status)
        verify { bookRepository.saveAll(books) }
    }

    @Test
    fun `should find all books by ids`() {
        val books = listOf(BookModel(id = 1, name = "Book", price = BigDecimal.TEN, customer = defaultCustomer(), status = BookStatus.ACTIVE))
        every { bookRepository.findAllById(setOf(1)) } returns books
        val result = bookService.findAllByIds(setOf(1))
        assertEquals(books, result)
        verify { bookRepository.findAllById(setOf(1)) }
    }

    @Test
    fun `should purchase books`() {
        val books = mutableListOf(BookModel(id = 1, name = "Book", price = BigDecimal.TEN, customer = defaultCustomer(), status = BookStatus.ACTIVE))
        every { bookRepository.saveAll(books) } returns books
        bookService.purchase(books)
        assertEquals(BookStatus.SOLD, books[0].status)
        verify { bookRepository.saveAll(books) }
    }
}
