package com.team404.foodtrack.domain.services

import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.domain.conditions.coupon.CouponMinimumPriceConditions
import com.team404.foodtrack.domain.conditions.coupon.CouponTagsCondition
import com.team404.foodtrack.domain.repositories.CouponRepository
import com.team404.foodtrack.domain.repositories.MarketRepository

class CouponService(private val marketRepository: MarketRepository, private val couponRepository: CouponRepository) {

    private var couponMinimumPriceConditions: CouponMinimumPriceConditions = CouponMinimumPriceConditions()
    private var couponTagsCondition: CouponTagsCondition = CouponTagsCondition()


    fun searchCoupons() : List<Coupon> {
        return couponRepository.search()
    }

    fun searchCouponByid(id: Long) : Coupon {
        return couponRepository.searchById(id)
    }

    fun searchCouponsForOrder(marketId: Long, orderPrice: Double) : List<Coupon> {
        val market = marketRepository.searchById(marketId)
        val coupons = couponRepository.search()

        return coupons.filter { coupon ->
            couponMinimumPriceConditions.evaluate(coupon, market.type!!, orderPrice)  &&
            couponTagsCondition.evaluate(coupon, market.type, orderPrice)
        }
    }

}