package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.ConsumptionMode
import com.team404.foodtrack.data.PaymentMethod

object PaymentMethodMocks {

    fun getPaymentMethodMocks() : List<PaymentMethod> {

        val paymentMethod1 = PaymentMethod.Builder()
            .id(111111L)
            .name("Efectivo")
            .paymentMethodImg("https://www.pngall.com/wp-content/uploads/12/Money-Bag-Vector-PNG-HD-Image.png")
            .build()

        val paymentMethod2 = PaymentMethod.Builder()
            .id(222222L)
            .name("Mercado Pago")
            .paymentMethodImg("https://forsedi.facturacfdi.mx/admindigital/images/mercadopago-icon.png")
            .build()

        val paymentMethod3 = PaymentMethod.Builder()
            .id(333333L)
            .name("Tarjeta de débito")
            .paymentMethodImg("https://www.shareicon.net/data/2017/05/11/885937_visa_512x512.png")
            .build()

        val paymentMethod4 = PaymentMethod.Builder()
            .id(444444L)
            .name("Tarjeta de crédito")
            .paymentMethodImg("https://i.pinimg.com/originals/94/8e/3a/948e3adaddb478e84b342e6619ca00be.png")
            .build()

        val paymentMethod5 = PaymentMethod.Builder()
            .id(555555L)
            .name("Bitcoin")
            .paymentMethodImg("https://bitcoingold.org/wp-content/uploads/Icon-Bitcoin-Gold-Black-300x300.png")
            .build()

        val paymentMethod6 = PaymentMethod.Builder()
            .id(666666L)
            .name("Ethereum ")
            .paymentMethodImg("https://cdn.iconscout.com/icon/free/png-256/ethereum-15-646027.png")
            .build()

        return listOf(paymentMethod1, paymentMethod2, paymentMethod3, paymentMethod4, paymentMethod5, paymentMethod6)
    }
}