package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.ConsumptionMode

object ConsumptionModeMocks {

    fun getConsumptionModeMocks() : List<ConsumptionMode> {

        val consumptionMode1 = ConsumptionMode.Builder()
            .id(111111L)
            .name("Consumo en el local")
            .consumptionModeImg("https://i.pinimg.com/originals/8d/28/d5/8d28d5a65a932ef9d259b8f3540ecb4b.png")
            .build()

        val consumptionMode2 = ConsumptionMode.Builder()
            .id(222222L)
            .name("Retiro por el local")
            .consumptionModeImg("https://cdn-icons-png.flaticon.com/512/1942/1942908.png")
            .build()

        return listOf(consumptionMode1, consumptionMode2)
    }
}