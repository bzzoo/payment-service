package com.app.paymentservice.payment.adapter.out.persistent.repository

import com.app.paymentservice.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface PaymentRepository {

    fun save(paymentEvent: PaymentEvent) : Mono<Void>
}