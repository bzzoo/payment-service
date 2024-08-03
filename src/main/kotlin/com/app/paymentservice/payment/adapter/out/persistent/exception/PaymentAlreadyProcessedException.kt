package com.app.paymentservice.payment.adapter.out.persistent.exception

import com.app.paymentservice.payment.domain.PaymentStatus

class PaymentAlreadyProcessedException(
        val status: PaymentStatus,
        message: String,
) :  RuntimeException(message)