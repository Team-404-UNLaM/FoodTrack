package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.Coupon

object CouponsMocks {

    fun getCouponsMocks() : List<Coupon> {

        val coupon1 = Coupon.Builder()
            .id(111111L)
            .name("Primera compra")
            .description("Realiza tu primera compra en cualquier comercio adherido y recibi un descuento del 30% sin tope de reintegro")
            .discunt(0.3)
            .build()

        val coupon2 = Coupon.Builder()
            .id(222222L)
            .name("Panza llena corazon contento")
            .description("Realiza una consumision en cualquier comercio adherido y recibi un descuento del 20% con un tope de reintegro de $500")
            .discunt(0.2)
            .maximumDiscount(500.0)
            .build()

        val coupon3 = Coupon.Builder()
            .id(333333L)
            .name("Esta para unas birras")
            .description("Sali con tu familia, amigos, pareja, que del resto nos encargamos nosotros. Usando este cupon tenes un descuento del 25% sin tope de reintegro")
            .discunt(0.25)
            .build()

        return listOf(coupon1, coupon2, coupon3)
    }
}