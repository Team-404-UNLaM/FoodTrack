package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.mockServer.MockServer

class CouponRepository(private val mockServer: MockServer) {

    fun search() : List<Coupon> {
        return mockServer.searchCoupons()
    }

    fun searchById(id: Long) : Coupon {
        return mockServer.searchCouponById(id)
    }
}