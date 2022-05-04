package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.Menu
import com.team404.foodtrack.mockServer.MockServer

class MenuRepository(private val mockServer: MockServer) {

    fun searchMenuByMarketId(id: Long) : Menu {
        return mockServer.searchMenuByMarketId(id)
    }
}