package com.team404.foodtrack.domain.mappers

import com.team404.foodtrack.data.Order
import com.team404.foodtrack.data.database.MinifiedOrder

class OrderMapper {

    fun map(minifiedOrder: MinifiedOrder) : Order {
        return Order.Builder()
            .id(minifiedOrder.id)
            .marketId(minifiedOrder.marketId)
            .date(minifiedOrder.date)
            .consumptionModeId(minifiedOrder.consumptioModeId)
            .paymentMethodId(minifiedOrder.paymentMethodId)
            .totalPrice(minifiedOrder.totalPrice)
            .discountedPrice(minifiedOrder.discountedPrice)
            .build()
    }
}