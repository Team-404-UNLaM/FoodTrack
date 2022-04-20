package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.Market
import com.team404.foodtrack.mockServer.MockServer

class MarketRepository(private val mockServer: MockServer) {

    fun search() : List<Market> {
        return mockServer.searchMarkets()
    }

    fun searchById(id: Long) : Market {
        return mockServer.searchMarketById(id)
    }

    fun searchByName(name: String) : List<Market> {
        return mockServer.searchMarkets(name)
    }
}