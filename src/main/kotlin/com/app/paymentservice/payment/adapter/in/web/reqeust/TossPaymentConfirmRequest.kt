package com.app.paymentservice.payment.adapter.`in`.web.reqeust

data class TossPaymentConfirmRequest(
        val paymentKey: String,
        val orderId: String,
        val amount: Long,
)