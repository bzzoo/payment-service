package com.app.paymentservice.payment.application.port.`in`

import com.app.paymentservice.payment.domain.CheckoutResult
import reactor.core.publisher.Mono

interface CheckoutUseCase {

    fun checkout(command: CheckoutCommand) : Mono<CheckoutResult>
}