package com.team404.foodtrack.data

class Order(
    val id: Long?,
    val marketId: Long?,
    val date: String?,
    val consumptioModeId: Long?,
    val products: MutableMap<Long, Int>?,
    val totalPrice: Double?) {

    data class Builder(
        var id: Long? = null,
        var marketId: Long? = null,
        var date: String? = null,
        var consumptionModeId: Long? = null,
        var products: MutableMap<Long, Int>? = null,
        var totalPrice: Double? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun marketId(marketId: Long) = apply { this.marketId = marketId }
        fun date(date: String) = apply { this.date = date }
        fun consumptionModeId(consumptionModeId: Long) = apply { this.consumptionModeId = consumptionModeId }
        fun products(products: MutableMap<Long, Int>) = apply { this.products = products }
        fun totalPrice(totalPrice: Double) = apply { this.totalPrice = totalPrice }
        fun build() = Order(id, marketId, date, consumptionModeId, products, totalPrice)
    }
}