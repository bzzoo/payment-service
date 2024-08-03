package com.app.paymentservice.payment.application.port.out


import com.app.paymentservice.payment.domain.PaymentExecutionFailure
import com.app.paymentservice.payment.domain.PaymentExtraDetails
import com.app.paymentservice.payment.domain.PaymentStatus

class PaymentStatusUpdateCommand(
        val paymentKey: String,
        val orderId: String,
        val status: PaymentStatus,
        val extraDetails: PaymentExtraDetails? = null,
        val failure: PaymentExecutionFailure? = null,
) {

    init {
        require(status == PaymentStatus.SUCCESS || status == PaymentStatus.FAILURE || status == PaymentStatus.UNKNOWN) {
            "결제 상태 (status: $status) 는 올바르지 않은 결제 상태입니다."
        }
        if (status == PaymentStatus.SUCCESS) {
            requireNotNull(extraDetails) {
                "PaymentStatus 값이 SUCCESS 라면 PaymentExtraDetails 는 null 이면 안됩니다."
            }
        } else if (status == PaymentStatus.FAILURE){
            requireNotNull(failure) { "PaymentStatus 값이 FAILURE 라면 PaymentExecutionFailure 는 null 이 되면 안됩니다. "}
        }
    }
}
