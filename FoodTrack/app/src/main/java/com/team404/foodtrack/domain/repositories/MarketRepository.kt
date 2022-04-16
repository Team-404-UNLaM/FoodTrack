package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.Market
import com.team404.foodtrack.mockServer.MockServer

class MarketRepository {

    fun search() : List<Market> {
        val mockServer = MockServer()
        return mockServer.searchMarkets()
    }

    fun searchById(id: Long) : Market {
        val mockServer = MockServer()
        return mockServer.searchMarketById(id)
    }
}