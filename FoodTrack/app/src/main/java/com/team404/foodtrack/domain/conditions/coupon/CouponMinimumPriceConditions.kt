package com.team404.foodtrack.domain.conditions.coupon

import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.data.MarketTypes

class CouponMinimumPriceConditions : CouponCondition {

    override fun evaluate(coupon: Coupon, orderType: List<MarketTypes>, orderPrice: Double): Boolean {
        val couponMinimumPrice = coupon.minimumPrice

        if (couponMinimumPrice != null) {
            return couponMinimumPrice < orderPrice
        }
        return false
    }
}