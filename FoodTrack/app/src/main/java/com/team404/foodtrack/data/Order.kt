package com.team404.foodtrack.data

class Order(
    val id: Long?,
    val marketId: Long?,
    val date: String?,
    val consumptioModeId: Long?,
    val paymentMethodId: Long?,
    val appliedCouponId: Long?,
    val products: MutableMap<Long, Int>?,
    val totalPrice: Double?,
    val discountedPrice: Double?) {

    data class Builder(
        var id: Long? = null,
        var marketId: Long? = null,
        var date: String? = null,
        var consumptionModeId: Long? = null,
        var paymentMethodId: Long? = null,
        var appliedCouponId: Long? = null,
        var products: MutableMap<Long, Int>? = null,
        var totalPrice: Double? = null,
        var discountedPrice: Double? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun marketId(marketId: Long) = apply { this.marketId = marketId }
        fun date(date: String) = apply { this.date = date }
        fun consumptionModeId(consumptionModeId: Long) = apply { this.consumptionModeId = consumptionModeId }
        fun paymentMethodId(paymentMethodId: Long) = apply { this.paymentMethodId = paymentMethodId }
        fun appliedCouponId(appliedCouponId: Long?) = apply { this.appliedCouponId = appliedCouponId }
        fun products(products: MutableMap<Long, Int>) = apply { this.products = products }
        fun totalPrice(totalPrice: Double) = apply { this.totalPrice = totalPrice }
        fun discountedPrice(discountedPrice: Double?) = apply { this.discountedPrice = discountedPrice }
        fun build() = Order(id, marketId, date, consumptionModeId, paymentMethodId, appliedCouponId, products, totalPrice, discountedPrice)
    }
}