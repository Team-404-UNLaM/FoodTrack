package com.team404.foodtrack.data

class Coupon(
    val id: Long?,
    val name: String?,
    val description: String?,
    val discount: Double?,
    val maximumDiscount: Double?) {

    data class Builder(
        var id: Long? = null,
        var name: String? = null,
        var description: String? = null,
        var discount: Double? = null,
        var maximumDiscount: Double? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun description(description: String) = apply { this.description = description }
        fun discunt(discount: Double) = apply { this.discount = discount }
        fun maximumDiscount(maximumDiscount: Double) = apply { this.maximumDiscount = maximumDiscount }
        fun build() = Coupon(id, name, description, discount, maximumDiscount)
    }
}