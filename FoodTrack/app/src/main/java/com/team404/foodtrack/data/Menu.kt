package com.team404.foodtrack.data

class Menu(
    val id: Long?,
    val items: List<MenuItem>?,
    val marketId: Long?) {

    data class Builder(
        var id: Long? = null,
        var items: List<MenuItem>? = null,
        var marketId: Long? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun items(items: List<MenuItem>) = apply { this.items = items }
        fun marketId(marketId: Long) = apply { this.marketId = marketId }
        fun build() = Menu(id, items, marketId)
    }
}