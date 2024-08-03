package com.app.paymentservice.payment.adapter.out

import com.app.paymentservice.common.WebAdapter
import com.app.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import com.app.paymentservice.payment.application.port.out.PaymentExecutorPort
import com.app.paymentservice.payment.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

@WebAdapter
class PaymentExecutorWebAdapter(

) : PaymentExecutorPort{
    override fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult> {
        TODO("Not yet implemented")
    }
}