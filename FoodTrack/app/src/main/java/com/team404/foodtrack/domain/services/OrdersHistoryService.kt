package com.team404.foodtrack.domain.services

import com.team404.foodtrack.data.OrderHistory
import com.team404.foodtrack.domain.mappers.OrderHistoryMapper
import com.team404.foodtrack.domain.repositories.OrderHistoryRepository
import com.team404.foodtrack.utils.transformToLowercaseAndReplaceSpaceWithDash

class OrdersHistoryService(private val ordersHistoryRepository: OrderHistoryRepository, private val orderHistoryMapper: OrderHistoryMapper) {

    fun search() : List<OrderHistory> {
        val ordersHistoryList = mutableListOf<OrderHistory>()
        val ordersFromServer = ordersHistoryRepository.search()

        ordersFromServer.forEach { order ->
            val orderHistory = orderHistoryMapper.map(order)
            ordersHistoryList.add(orderHistory)
        }

        return ordersHistoryList
    }

    fun searchByName(name: String) : List<OrderHistory> {
        val orderHistoryList = search()
        val orderHistoryListFiltered = mutableListOf<OrderHistory>()

        orderHistoryList.forEach { order ->
            val transformedMarketName = order.market.name.toString().let {
                transformToLowercaseAndReplaceSpaceWithDash(it)
            }

            if (transformedMarketName.contains(name)) {
                orderHistoryListFiltered.add(order)
            }

        }

        return orderHistoryListFiltered
    }

}