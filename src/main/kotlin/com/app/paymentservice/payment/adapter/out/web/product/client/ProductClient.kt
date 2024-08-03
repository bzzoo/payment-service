package com.app.paymentservice.payment.adapter.out.web.product.client

import com.app.paymentservice.payment.domain.Product
import reactor.core.publisher.Flux

interface ProductClient {

    fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}