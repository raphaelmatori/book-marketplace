package com.bookmarketplace.model

import com.bookmarketplace.enums.BookStatus
import com.bookmarketplace.enums.Errors
import com.bookmarketplace.exception.BadRequestException
import jakarta.persistence.*
import java.math.BigDecimal

@Entity(name = "book")
data class BookModel (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerModel? = null
) {
    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
        set(value) {
            if(field == BookStatus.CANCELED || field == BookStatus.DELETED) {
                throw BadRequestException(Errors.BM10001.message.format(field), Errors.BM10001.code)
            }
            field = value
        }

    constructor(
        id: Int? = null,
        name: String,
        price: BigDecimal,
        customer: CustomerModel? = null,
        status: BookStatus?): this(id, name, price, customer) {
            this.status = status
        }
}
