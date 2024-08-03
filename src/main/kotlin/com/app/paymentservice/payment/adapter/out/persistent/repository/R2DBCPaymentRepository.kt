package com.app.paymentservice.payment.adapter.out.persistent.repository

import com.app.paymentservice.payment.domain.PaymentEvent
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Mono
import java.math.BigInteger

@Repository
class R2DBCPaymentRepository(
        private val databaseClient: DatabaseClient,
        private val transactionalOperator: TransactionalOperator,
) : PaymentRepository{

    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return insertPaymentEvent(paymentEvent)
                .flatMap { selectPaymentEventId() }
                .flatMap {paymentEventId-> insertPyamentOrders(paymentEvent, paymentEventId) }
                .`as`(transactionalOperator::transactional)
                .then()
    }

    private fun insertPyamentOrders(paymentEvent: PaymentEvent, paymentEventId: Long): Mono<Long> {
        val valueClauses = paymentEvent.paymentOrders.joinToString(", ") { paymentOrder ->
            "($paymentEventId, ${paymentOrder.sellerId}, '${paymentOrder.orderId}', ${paymentOrder.paymentEventId}, ${paymentOrder.amount}, `${paymentOrder.paymentStatus}`)"
        }
        return databaseClient.sql(INSERT_PAYMENT_ORDER_QUERY(valueClauses))
                .fetch()
                .rowsUpdated()
    }

    private fun selectPaymentEventId() = databaseClient.sql(LSAT_INSERT_ID_QUERY)
            .fetch()
            .first()
            .map { (it["LAST_INSERT_ID()"] as BigInteger).toLong() }

    private fun insertPaymentEvent(paymentEvent: PaymentEvent) : Mono<Long> {
        return databaseClient.sql(INSERT_PAYMENT_EVENT_QUERY)
                .bind("buyerId", paymentEvent.buyerId)
                .bind("orderName", paymentEvent.orderName)
                .bind("orderId", paymentEvent.orderId)
                .fetch()
                .rowsUpdated()
    }

    companion object {
        val INSERT_PAYMENT_EVENT_QUERY = """
            INSERT INTO payment_events (buyer_id, order_name, order_id)
            VALUES (:buyerId, :oderName, :orderId)
        """.trimIndent()

        val LSAT_INSERT_ID_QUERY = """
            SELECT LAST_INSERT_ID()
        """.trimIndent()

        val INSERT_PAYMENT_ORDER_QUERY = fun (valueClauses: String) = """
            INSERT INTO payment_orders (payment_event_id, seller_id, order_id, product_id, amount, payment_order_status)
            VALUES $valueClauses
        """.trimIndent()
    }
}