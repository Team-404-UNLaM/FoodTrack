package com.team404.foodtrack.data

class Order(
    val id: Long?,
    val marketId: Long?,
    val products: MutableMap<Long, Int>?) {

    data class Builder(
        var id: Long? = null,
        var marketId: Long? = null,
        var products: MutableMap<Long, Int>? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun marketId(marketId: Long) = apply { this.marketId = marketId }
        fun products(products: MutableMap<Long, Int>) = apply { this.products = products }
        fun build() = Order(id, marketId, products)
    }
}