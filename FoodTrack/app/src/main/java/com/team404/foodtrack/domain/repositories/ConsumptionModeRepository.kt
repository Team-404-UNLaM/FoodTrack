package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.ConsumptionMode
import com.team404.foodtrack.mockServer.MockServer

class ConsumptionModeRepository(private val mockServer: MockServer) {
    fun search() : List<ConsumptionMode> {
        return mockServer.searchConsumptionModes()
    }

    fun searchById(id: Long) : ConsumptionMode {
        return mockServer.searchConsumptionModeById(id)
    }
}