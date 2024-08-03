package com.app.paymentservice.payment.test

import com.app.paymentservice.payment.domain.PaymentEvent
import com.app.paymentservice.payment.domain.PaymentOrder
import com.app.paymentservice.payment.domain.PaymentStatus
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal


class R2DBCPaymentDatabaseHelper(
        private val databaseClient: DatabaseClient,
        private val transactionalOperator: TransactionalOperator
) : PaymentDataBaseHelper {

    override fun getPayments(orderId: String): PaymentEvent? {
        return databaseClient.sql(SELECT_PAYMENT_QUERY)
                .bind("orderId", orderId)
                .fetch()
                .all()
                .groupBy { it["payment_event_id"] as Long }
                .flatMap { groupedFlux ->
                    groupedFlux.collectList().map { results ->
                        PaymentEvent(
                                id = groupedFlux.key(),
                                orderId = results.first()["order_id"] as String,
                                orderName = results.first()["order_name"] as String,
                                buyerId = results.first()["buyer_id"] as Long,
                                isPaymentDone = if (((results.first()["is_payment_done"] as Byte).toInt() == 1)) true else false,
                                paymentOrders = results.map { result ->
                                    PaymentOrder(
                                            id = result["id"] as Long,
                                            paymentEventId = groupedFlux.key(),
                                            sellerId = result["seller_id"] as Long,
                                            orderId = result["order_id"] as String,
                                            productId = result["product_id"] as Long,
                                            amount = result["amount"] as BigDecimal,
                                            paymentStatus = PaymentStatus.get(result["payment_order_status"] as String),
                                            isLedgerUpdated = if (((result["ledger_updated"]) as Byte).toInt() == 1) true else false,
                                            isWalletUpdated = if (((result["wallet_updated"]) as Byte).toInt() == 1) true else false,
                                    )
                                }
                        )
                    }
                }.toMono().block()
    }

    companion object {
        val SELECT_PAYMENT_QUERY = """
            SELECT * FROM payment_events pe
            INNER JOIN payment_orders po ON pe.order_id= po.order_id
            WHERE pe.order_id = :orderId
        """.trimIndent()
    }
}