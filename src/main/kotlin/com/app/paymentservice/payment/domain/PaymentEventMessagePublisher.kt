package com.app.paymentservice.payment.domain

import com.app.paymentservice.payment.domain.PaymentEventMessage
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalEventPublisher
import reactor.core.publisher.Mono

@Component
class PaymentEventMessagePublisher (
  publisher: ApplicationEventPublisher
) {

  private val transactionalEventPublisher = TransactionalEventPublisher(publisher)

  fun publishEvent(paymentEventMessage: PaymentEventMessage): Mono<PaymentEventMessage> {
    return transactionalEventPublisher.publishEvent(paymentEventMessage)
      .thenReturn(paymentEventMessage)
  }
}