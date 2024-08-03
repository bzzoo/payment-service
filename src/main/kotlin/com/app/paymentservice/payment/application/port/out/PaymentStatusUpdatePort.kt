package com.app.paymentservice.payment.application.port.out

import reactor.core.publisher.Mono

interface PaymentStatusUpdatePort {

    fun updatePaymentStatusToExecuting(orderId: String, paymentKye: String): Mono<Boolean>
    fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean>
}
