package com.app.paymentservice.payment.adapter.`in`.web.view

import com.app.paymentservice.common.IdempotencyCreator
import com.app.paymentservice.common.WebAdapter
import com.app.paymentservice.payment.adapter.`in`.web.reqeust.CheckoutRequest
import com.app.paymentservice.payment.application.port.`in`.CheckoutCommand
import com.app.paymentservice.payment.application.port.`in`.CheckoutUseCase

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono

@WebAdapter
@Controller
class CheckoutController (
  private val checkoutUseCase: CheckoutUseCase
){

  @GetMapping("/")
  fun checkoutPage(request: CheckoutRequest, model: Model): Mono<String> {
    val checkoutCommand = CheckoutCommand(
      cartId = request.cartId,
      buyerId = request.buyerId,
      productIds = request.productIds,
      idempotencyKey = IdempotencyCreator.create(request)
    )

    return checkoutUseCase.checkout(checkoutCommand)
      .map {
        model.addAttribute("orderId", it.orderId)
        model.addAttribute("orderName", it.orderName)
        model.addAttribute("amount", it.amount)
        "checkout"
      }
  }
}