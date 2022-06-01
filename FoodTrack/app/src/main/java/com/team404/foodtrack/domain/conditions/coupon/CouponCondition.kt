package com.team404.foodtrack.domain.conditions.coupon

import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.data.MarketTypes

interface CouponCondition {
    fun evaluate(coupon: Coupon, orderType: List<MarketTypes>, orderPrice: Double) : Boolean
}