package com.app.paymentservice.payment.application.port.out

import com.app.paymentservice.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface SavePaymentPort {

    fun save(paymentEvent: PaymentEvent) : Mono<Void>
}
