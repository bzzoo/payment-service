package com.app.paymentservice.payment.application.service

import com.app.paymentservice.payment.application.port.`in`.CheckoutCommand
import com.app.paymentservice.payment.application.port.`in`.CheckoutUseCase
import com.app.paymentservice.payment.test.PaymentDataBaseHelper
import com.app.paymentservice.payment.test.PaymentTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import reactor.test.StepVerifier
import java.util.*

@SpringBootTest
@Import(PaymentTestConfiguration::class)
class CheckoutServiceTest(
        @Autowired private val checkoutUseCase: CheckoutUseCase,
        @Autowired private val paymentDataBaseHelper: PaymentDataBaseHelper
){

    @BeforeEach

    @Test
    fun `should save PaymentEvent and PaymentOrder successfully` (){
        val orderId = UUID.randomUUID().toString()
        val checkoutCommand = CheckoutCommand(
                cartId = 1,
                buyerId = 1,
                productIds = listOf(1,2,3),
                idempotencyKey = orderId
        )

        StepVerifier.create(checkoutUseCase.checkout(checkoutCommand))
                .expectNextMatches{
                    it.amount.toInt() == 60000 && it.orderId == orderId
                }
                .verifyComplete()

        val paymentEvent = paymentDataBaseHelper.getPayments(orderId)!!

        assertThat(paymentEvent.orderId).isEqualTo(orderId)
        assertThat(paymentEvent.totalAmount()).isEqualTo(60000)
        assertThat(paymentEvent.paymentOrders.size).isEqualTo(checkoutCommand.productIds.size)
        assertFalse(paymentEvent.isPaymentDone())
        assertTrue(paymentEvent.paymentOrders.all { !it.isLedgerUpdated()})
        assertTrue(paymentEvent.paymentOrders.all { !it.isWalletUpdated()})
    }

    @Test
    fun `should fail to save PaymentEvent and PaymentOrder when trying to save for the second time`(){
        val orderId = UUID.randomUUID().toString()
        val checkoutCommand = CheckoutCommand(
                cartId = 1,
                buyerId = 1,
                productIds = listOf(1,2,3),
                idempotencyKey = orderId
        )

        checkoutUseCase.checkout(checkoutCommand).block()
        org.junit.jupiter.api.assertThrows<DataIntegrityViolationException> {
            checkoutUseCase.checkout(checkoutCommand).block()
        }
    }
}