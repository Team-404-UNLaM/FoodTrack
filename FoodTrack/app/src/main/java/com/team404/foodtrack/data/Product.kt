package com.team404.foodtrack.data

class Product(
    val id: Long?,
    val name: String?,
    val description: String?,
    val productImg: String?,
    val price: Double?,
    val marketId: Long?) {

    data class Builder(
        var id: Long? = null,
        var name: String? = null,
        var description: String? = null,
        var productImg: String? = null,
        var price: Double? = null,
        var marketId: Long? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun description(description: String) = apply { this.description = description }
        fun productImg(productImg: String) = apply { this.productImg = productImg }
        fun price(price: Double) = apply { this.price = price }
        fun marketId(marketId: Long) = apply { this.marketId = marketId }
        fun build() = Product(id, name, description, productImg, price, marketId)
    }
}