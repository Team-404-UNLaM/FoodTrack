package com.team404.foodtrack.data

class MarketData(
    val market: Market?,
    var isFavorite: Boolean) {

    data class Builder(
        var market: Market? = null,
        var isFavorite: Boolean = false) {

        fun market(market: Market) = apply { this.market = market }
        fun isFavorite(isFavorite: Boolean) = apply { this.isFavorite = isFavorite }
        fun build() = MarketData(market, isFavorite)
    }
}