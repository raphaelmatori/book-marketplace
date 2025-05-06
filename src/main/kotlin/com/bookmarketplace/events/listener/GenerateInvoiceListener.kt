package com.bookmarketplace.events.listener

import com.bookmarketplace.events.PurchaseEvent
import com.bookmarketplace.service.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.*

@Component
class GenerateInvoiceListener(
    private val purchaseService: PurchaseService
) {
    @Async
    @EventListener
    fun listen(purchaseEvent: PurchaseEvent) {
        val invoice = UUID.randomUUID().toString()
        val purchaseModel = purchaseEvent.purchaseModel.copy(invoice = invoice)
        purchaseService.update(purchaseModel)
    }
}
