package com.app.paymentservice.payment.test

import com.app.paymentservice.payment.domain.PaymentEvent

interface PaymentDataBaseHelper {

    fun getPayments(orderId: String): PaymentEvent?
}
