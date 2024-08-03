package com.app.paymentservice.payment.adapter.out.persistent

import com.app.paymentservice.common.PersistentAdapter
import com.app.paymentservice.payment.adapter.out.persistent.repository.PaymentRepository
import com.app.paymentservice.payment.adapter.out.persistent.repository.PaymentStatusUpdateRepository
import com.app.paymentservice.payment.adapter.out.persistent.repository.PaymentValidationRepository
import com.app.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand
import com.app.paymentservice.payment.application.port.out.PaymentStatusUpdatePort
import com.app.paymentservice.payment.application.port.out.PaymentValidationPort
import com.app.paymentservice.payment.application.port.out.SavePaymentPort
import com.app.paymentservice.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

@PersistentAdapter
class PaymentPersistentAdapter(
        private val paymentRepository: PaymentRepository,
        private val paymentStatusUpdateRepository: PaymentStatusUpdateRepository,
        private val paymentValidationRepository: PaymentValidationRepository,
) : SavePaymentPort, PaymentStatusUpdatePort, PaymentValidationPort {

    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }

    override fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean> {
        return paymentStatusUpdateRepository.updatePaymentStatusToExecuting(orderId, paymentKey)
    }

    override fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean> {
        return paymentStatusUpdateRepository.updatePaymentStatus(command)
    }

    override fun isValid(orderId: String, amount: Long): Mono<Boolean> {
        return  paymentValidationRepository.isValid(orderId, amount)
    }
}