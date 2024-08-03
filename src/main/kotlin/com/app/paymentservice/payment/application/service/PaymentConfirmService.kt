package com.app.paymentservice.payment.application.service

import com.app.paymentservice.common.UseCase
import com.app.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import com.app.paymentservice.payment.application.port.`in`.PaymentConfirmUseCase
import com.app.paymentservice.payment.application.port.out.PaymentExecutorPort
import com.app.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand
import com.app.paymentservice.payment.application.port.out.PaymentStatusUpdatePort
import com.app.paymentservice.payment.application.port.out.PaymentValidationPort
import com.app.paymentservice.payment.domain.PaymentConfirmationResult
import reactor.core.publisher.Mono

@UseCase
class PaymentConfirmService(
    private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
    private val paymentValidationPort: PaymentValidationPort,
    private val paymentExecutorPort: PaymentExecutorPort,
) : PaymentConfirmUseCase {

    override fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult> {
        return paymentStatusUpdatePort.updatePaymentStatusToExecuting(command.orderId, command.paymentKey)
            .filterWhen { paymentValidationPort.isValid(command.orderId, command.amount) }
            .flatMap { paymentExecutorPort.execute(command) }
            .flatMap {
                paymentStatusUpdatePort.updatePaymentStatus(
                    command = PaymentStatusUpdateCommand(
                        paymentKey = it.paymentKey,
                        orderId = it.orderId,
                        status = it.paymentStatus(),
                        extraDetails = it.extraDetails,
                        failure = it.failure
                    )
                ).thenReturn(it)
            }
            .map {
                PaymentConfirmationResult(
                    status = it.paymentStatus(),
                    failure = it.failure,
                )
            }
    }
}