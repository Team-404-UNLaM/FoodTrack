package com.team404.foodtrack.domain.mappers

import com.team404.foodtrack.data.Order
import com.team404.foodtrack.data.database.MinifiedOrder

class MinifiedOrderMapper {

    fun map(order: Order) : MinifiedOrder {
        return MinifiedOrder(
            0,
            order.marketId!!,
            order.date!!,
            order.consumptionModeId!!,
            order.paymentMethodId!!,
            order.totalPrice!!,
            order.discountedPrice?: 0.0
        )
    }
}