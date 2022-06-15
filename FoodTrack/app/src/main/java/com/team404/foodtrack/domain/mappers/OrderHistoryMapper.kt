package com.team404.foodtrack.domain.mappers

import com.team404.foodtrack.data.Order
import com.team404.foodtrack.data.OrderHistory
import com.team404.foodtrack.domain.repositories.MarketRepository

class OrderHistoryMapper(private val marketRepository: MarketRepository) {

    fun map(order: Order) : OrderHistory {
        val market = marketRepository.searchById(order.marketId!!)

        return OrderHistory(
            order,
            market,
            ""
        )
    }
}