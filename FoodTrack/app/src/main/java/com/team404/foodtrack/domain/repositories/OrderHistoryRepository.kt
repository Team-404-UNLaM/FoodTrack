package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.Order
import com.team404.foodtrack.mockServer.MockServer

class OrderHistoryRepository(private val mockServer: MockServer) {

    fun search() : List<Order> {
        return mockServer.searchOrdersHistory()
    }

    fun searchByOrderId(id:Long) : Order {
        return mockServer.searchOrderById(id)
    }

}