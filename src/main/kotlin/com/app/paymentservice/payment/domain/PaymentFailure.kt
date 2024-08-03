package com.app.paymentservice.payment.domain

data class PaymentFailure (
  val errorCode: String,
  val message: String
)