package com.app.paymentservice.payment.adapter.`in`.web.view

import com.app.paymentservice.common.WebAdapter
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

@Controller
@WebAdapter
@RequestMapping("/v1/toss")
class PaymentController {

    @GetMapping("/success")
    fun successPage(): Mono<String> {
        return Mono.just("success")
    }

    @GetMapping("/fail")
    fun pailPage(): Mono<String>{
        return Mono.just("fail")
    }
}