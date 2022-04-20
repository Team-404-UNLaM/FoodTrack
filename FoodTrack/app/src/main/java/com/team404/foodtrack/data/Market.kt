package com.team404.foodtrack.data

class Market(
    val id: Long?,
    val name: String?,
    val address: MarketAddress?,
    val marketImg: String?,
    val stars: Double?,
    val description: String?,
    val type: List<String>?) {

    data class Builder(
        var id: Long? = null,
        var name: String? = null,
        var address: MarketAddress? = null,
        var marketImg: String? = null,
        var stars: Double? = null,
        var description: String? = null,
        var type: List<String>? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun address(address: MarketAddress) = apply { this.address = address }
        fun marketImg(marketImg: String) = apply { this.marketImg = marketImg }
        fun stars(stars: Double) = apply { this.stars = stars }
        fun description(description: String) = apply { this.description = description }
        fun type(type: List<String>) = apply { this.type = type }
        fun build() = Market(id, name, address, marketImg, stars, description, type)
    }
}
