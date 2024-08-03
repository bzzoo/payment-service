package com.app.paymentservice.payment.domain

import com.app.paymentservice.payment.domain.PaymentStatus

data class PendingPaymentOrder (
        val paymentOrderId: Long,
        val status: PaymentStatus,
        val amount: Long,
        val failedCount: Byte,
        val threshold: Byte
)
