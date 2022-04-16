package com.team404.foodtrack.mockServer

import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.mockServer.mocks.CouponsMocks
import com.team404.foodtrack.mockServer.mocks.MarketsMocks

class MockServer {

    fun searchMarkets() : List<Market> {
        return MarketsMocks.getMarketsMocks()
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