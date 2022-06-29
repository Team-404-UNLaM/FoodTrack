package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.ConsumptionMode

object ConsumptionModeMocks {

    fun getConsumptionModeMocks() : List<ConsumptionMode> {

        val consumptionMode1 = ConsumptionMode.Builder()
            .id(111111L)
            .name("Consumo en el local")
            .consumptionModeImg("https://i.pinimg.com/564x/79/42/4b/79424bc66709013581ff88a1858b3d42.jpg")
            .build()

        val consumptionMode2 = ConsumptionMode.Builder()
            .id(222222L)
            .name("Retiro por el local")
            .consumptionModeImg("https://cdn-icons-png.flaticon.com/512/1942/1942908.png")
            .build()

        return listOf(consumptionMode1, consumptionMode2)
    }
}