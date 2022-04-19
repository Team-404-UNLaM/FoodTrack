package com.team404.foodtrack.mockServer

import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.mockServer.mocks.CouponsMocks
import com.team404.foodtrack.mockServer.mocks.MarketsMocks
import com.team404.foodtrack.utils.transformToLowercaseAndReplaceSpaceWithDash

class MockServer {

    fun searchMarkets() : List<Market> {
        return MarketsMocks.getMarketsMocks()
    }

    fun searchMarkets(name: String) : List<Market> {
        val marketList = MarketsMocks.getMarketsMocks()
        val marketListFiltered = mutableListOf<Market>()

        marketList.forEach { market ->
            val transformedMarketName = market.name?.let {
                transformToLowercaseAndReplaceSpaceWithDash(
                    it
                )
            }

            if (transformedMarketName != null) {
                if (transformedMarketName.contains(name)) {
                    marketListFiltered.add(market)
                }
            }

        }

        return marketListFiltered
    }

    fun searchMarketById(id: Long) : Market {
        val markets = MarketsMocks.getMarketsMocks()
        return markets.first { market -> market.id!! == id }
    }

    fun searchCoupons() : List<Coupon> {
        return CouponsMocks.getCouponsMocks()
    }

    fun searchCouponById(id: Long) : Coupon {
        val coupons = CouponsMocks.getCouponsMocks()
        return coupons.first { market -> market.id!! == id }
    }
}