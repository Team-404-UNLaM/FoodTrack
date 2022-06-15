package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.ConsumptionMode
import com.team404.foodtrack.data.PaymentMethod

object PaymentMethodMocks {

    fun getPaymentMethodMocks() : List<PaymentMethod> {

        val paymentMethod1 = PaymentMethod.Builder()
            .id(111111L)
            .name("Efectivo")
            .paymentMethodImg("https://bigpons.com.ar/productos/20210422181717.jpeg")
            .build()

        val paymentMethod2 = PaymentMethod.Builder()
            .id(222222L)
            .name("Mercado Pago")
            .paymentMethodImg("https://bigpons.com.ar/productos/20210422181717.jpeg")
            .build()

        val paymentMethod3 = PaymentMethod.Builder()
            .id(333333L)
            .name("Tarjeta de débito")
            .paymentMethodImg("https://bigpons.com.ar/productos/20210422181717.jpeg")
            .build()

        val paymentMethod4 = PaymentMethod.Builder()
            .id(444444L)
            .name("Tarjeta de crédito")
            .paymentMethodImg("https://bigpons.com.ar/productos/20210422181717.jpeg")
            .build()

        return listOf(paymentMethod1, paymentMethod2, paymentMethod3, paymentMethod4)
    }
}