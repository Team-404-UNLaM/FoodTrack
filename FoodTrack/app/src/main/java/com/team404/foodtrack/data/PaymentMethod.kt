package com.team404.foodtrack.data

class PaymentMethod(
    val id: Long?,
    val name: String?,
    val paymentMethodImg: String?) {

    data class Builder(
        var id: Long? = null,
        var name: String? = null,
        var paymentMethodImg: String? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun paymentMethodImg(paymentMethodImg: String) = apply { this.paymentMethodImg = paymentMethodImg }
        fun build() = PaymentMethod(id, name, paymentMethodImg)
    }
}