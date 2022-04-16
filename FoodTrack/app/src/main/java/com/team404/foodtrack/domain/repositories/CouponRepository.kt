package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.mockServer.MockServer

class CouponRepository {

    fun search() : List<Coupon> {
        val mockServer = MockServer()
        return mockServer.searchCoupons()
    }

    fun searchById(id: Long) : Coupon {
        val mockServer = MockServer()
        return mockServer.searchCouponById(id)
    }
}