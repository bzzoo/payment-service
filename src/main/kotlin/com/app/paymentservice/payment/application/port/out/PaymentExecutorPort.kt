package com.app.paymentservice.payment.application.port.out

import com.app.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import com.app.paymentservice.payment.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

interface PaymentExecutorPort {

    fun execute(command: PaymentConfirmCommand) : Mono<PaymentExecutionResult>
}
