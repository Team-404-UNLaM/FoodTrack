package com.team404.foodtrack.data

class Coupon(
    val id: Long?,
    val name: String?,
    val description: String?,
    val couponImg: String?,
    val minimumPrice: Double?,
    val discount: Double?,
    val maximumDiscount: Double?,
    val tags: List<MarketTypes>?) {

    data class Builder(
        var id: Long? = null,
        var name: String? = null,
        var description: String? = null,
        var couponImg: String? = null,
        var minimumPrice: Double? = null,
        var discount: Double? = null,
        var maximumDiscount: Double? = null,
        var tags: List<MarketTypes>? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun description(description: String) = apply { this.description = description }
        fun couponImg(couponImg: String) = apply { this.couponImg = couponImg }
        fun minimumPrice(minimumPrice: Double) = apply { this.minimumPrice = minimumPrice }
        fun discount(discount: Double) = apply { this.discount = discount }
        fun maximumDiscount(maximumDiscount: Double) = apply { this.maximumDiscount = maximumDiscount }
        fun tags(tags: List<MarketTypes>) = apply { this.tags = tags }
        fun build() = Coupon(id, name, description, couponImg, minimumPrice, discount, maximumDiscount, tags)
    }
}