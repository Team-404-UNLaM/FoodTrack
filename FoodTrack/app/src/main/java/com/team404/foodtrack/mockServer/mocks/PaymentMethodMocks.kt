package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.ConsumptionMode
import com.team404.foodtrack.data.PaymentMethod

object PaymentMethodMocks {

    fun getPaymentMethodMocks() : List<PaymentMethod> {

        val paymentMethod1 = PaymentMethod.Builder()
            .id(111111L)
            .name("Efectivo")
            .paymentMethodImg("https://i.etsystatic.com/20162739/r/il/531799/1951524577/il_fullxfull.1951524577_h99n.jpg")
            .build()

        val paymentMethod2 = PaymentMethod.Builder()
            .id(222222L)
            .name("Mercado Pago")
            .paymentMethodImg("https://www.mgscreativa.com/images/jamp/page/logo-mercadopago29.png")
            .build()

        val paymentMethod3 = PaymentMethod.Builder()
            .id(333333L)
            .name("Tarjeta de débito")
            .paymentMethodImg("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDNItOubopRjMLl6IdNOCjPYaiS9WYp92gdQ&usqp=CAU")
            .build()

        val paymentMethod4 = PaymentMethod.Builder()
            .id(444444L)
            .name("Tarjeta de crédito")
            .paymentMethodImg("https://logowik.com/content/uploads/images/credit-card2790.jpg")
            .build()

        val paymentMethod5 = PaymentMethod.Builder()
            .id(555555L)
            .name("Bitcoin")
            .paymentMethodImg("https://i.etsystatic.com/11603536/r/il/834030/1426442143/il_fullxfull.1426442143_jjt8.jpg")
            .build()

        val paymentMethod6 = PaymentMethod.Builder()
            .id(666666L)
            .name("Ethereum ")
            .paymentMethodImg("https://st3.depositphotos.com/5466018/15994/v/600/depositphotos_159947638-stock-illustration-coin-crypto-currency-ethereum-icon.jpg")
            .build()

        return listOf(paymentMethod1, paymentMethod2, paymentMethod3, paymentMethod4, paymentMethod5, paymentMethod6)
    }
}