package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.Order

object OrdersHistoryMocks {

    fun getOrdersHistoryMocks() : List<Order> {

        val order1 = Order.Builder()
            .id(111111L)
            .marketId(111111L)
            .totalPrice(2350.50)
            .date("11 de junio")
            .consumptionModeId(111111L)
            .build()

        val order2 = Order.Builder()
            .id(111111L)
            .marketId(333333L)
            .totalPrice(1250.00)
            .date("3 de junio")
            .consumptionModeId(111111L)
            .build()

        val order3 = Order.Builder()
            .id(111111L)
            .marketId(222222L)
            .totalPrice(600.00)
            .date("19 de mayo")
            .consumptionModeId(222222L)
            .build()

        val order4 = Order.Builder()
            .id(111111L)
            .marketId(444444L)
            .totalPrice(750.50)
            .date("11 de mayo")
            .consumptionModeId(111111L)
            .build()

        val order5 = Order.Builder()
            .id(111111L)
            .marketId(111111L)
            .totalPrice(2350.50)
            .date("15 de abril")
            .consumptionModeId(111111L)
            .build()

        val order6 = Order.Builder()
            .id(111111L)
            .marketId(222222L)
            .totalPrice(2350.50)
            .date("31 de julio del 2021")
            .consumptionModeId(222222L)
            .build()

        return listOf(order1, order2, order3, order4, order5, order6)
    }
}