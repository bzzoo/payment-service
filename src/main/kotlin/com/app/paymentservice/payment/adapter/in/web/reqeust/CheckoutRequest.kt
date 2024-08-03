package com.app.paymentservice.payment.adapter.`in`.web.reqeust

import java.time.LocalDateTime

data class CheckoutRequest(
        val cartId: Long = 1,
        val productIds: List<Long> = listOf(1,2,3),
        val buyerId: Long = 1,
        val seed: String = LocalDateTime.now().toString()  //요청 규별
)