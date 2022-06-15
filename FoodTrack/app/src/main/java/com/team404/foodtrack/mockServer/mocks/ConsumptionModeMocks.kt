package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.ConsumptionMode

object ConsumptionModeMocks {

    fun getConsumptionModeMocks() : List<ConsumptionMode> {

        val consumptionMode1 = ConsumptionMode.Builder()
            .id(111111L)
            .name("Consumo en el local")
            .consumptionModeImg("https://bigpons.com.ar/productos/20210422181717.jpeg")
            .build()

        val consumptionMode2 = ConsumptionMode.Builder()
            .id(222222L)
            .name("Retiro por el local")
            .consumptionModeImg("https://bigpons.com.ar/productos/20210422181717.jpeg")
            .build()

        return listOf(consumptionMode1, consumptionMode2)
    }
}