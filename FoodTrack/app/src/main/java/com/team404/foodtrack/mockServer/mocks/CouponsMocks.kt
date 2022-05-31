package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.data.MarketTypes

object CouponsMocks {

    fun getCouponsMocks() : List<Coupon> {

        val coupon1 = Coupon.Builder()
            .id(111111L)
            .name("Primera compra")
            .description("Realiza tu primera compra en cualquier comercio adherido y recibi un descuento del 30% sin tope de reintegro")
            .minimumPrice(0.0)
            .discount(0.3)
            .tags(listOf(MarketTypes.ALL))
            .build()

        val coupon2 = Coupon.Builder()
            .id(222222L)
            .name("Panza llena corazon contento")
            .description("Realiza una consumision en cualquier comercio adherido y recibi un descuento del 20% con un tope de reintegro de $500")
            .minimumPrice(500.0)
            .discount(0.2)
            .maximumDiscount(500.0)
            .tags(listOf(MarketTypes.ALL))
            .build()

        val coupon3 = Coupon.Builder()
            .id(333333L)
            .name("Esta para unas birras")
            .description("Sali con tu familia, amigos, pareja, que del resto nos encargamos nosotros. Usando este cupon tenes un descuento del 25% sin tope de reintegro")
            .minimumPrice(200.0)
            .discount(0.25)
            .tags(listOf(MarketTypes.BAR))
            .build()

        val coupon4 = Coupon.Builder()
            .id(444444L)
            .name("Full cena")
            .description("15% de descuento en restaurantes adheridos")
            .minimumPrice(1500.0)
            .discount(0.15)
            .tags(listOf(MarketTypes.RESTAURANT))
            .build()

        return listOf(coupon1, coupon2, coupon3, coupon4)
    }
}