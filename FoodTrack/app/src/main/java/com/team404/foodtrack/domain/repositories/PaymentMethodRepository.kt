package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.PaymentMethod
import com.team404.foodtrack.mockServer.MockServer

class PaymentMethodRepository(private val mockServer: MockServer) {
    fun search() : List<PaymentMethod> {
        return mockServer.searchPaymentMethods()
    }

    fun searchById(id: Long) : PaymentMethod {
        return mockServer.searchPaymentMethodById(id)
    }
}