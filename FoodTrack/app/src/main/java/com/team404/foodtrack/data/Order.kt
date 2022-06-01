package com.team404.foodtrack.data

class Order(
    val id: Long?,
    val marketId: Long?,
    val products: MutableMap<Long, Int>?,
    val totalPrice: Double?) {

    data class Builder(
        var id: Long? = null,
        var marketId: Long? = null,
        var products: MutableMap<Long, Int>? = null,
        var totalPrice: Double? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun marketId(marketId: Long) = apply { this.marketId = marketId }
        fun products(products: MutableMap<Long, Int>) = apply { this.products = products }
        fun totalPrice(totalPrice: Double) = apply { this.totalPrice = totalPrice }
        fun build() = Order(id, marketId, products, totalPrice)
    }
}