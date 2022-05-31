package com.team404.foodtrack.domain.conditions.coupon

import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.data.MarketTypes

class CouponTagsCondition : CouponCondition {

    override fun evaluate(coupon: Coupon, orderType: List<MarketTypes>, orderPrice: Double): Boolean {
        return coupon.tags?.any{ it in orderType || it == MarketTypes.ALL } ?: false
    }
}