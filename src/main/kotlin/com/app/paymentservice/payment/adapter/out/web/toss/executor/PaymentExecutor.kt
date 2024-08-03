package com.app.paymentservice.payment.adapter.out.web.toss.executor

import com.app.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import com.app.paymentservice.payment.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

interface  PaymentExecutor{

    fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>
}
