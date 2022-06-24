package com.team404.foodtrack.utils

import com.team404.foodtrack.data.Coupon

fun buildCouponValidForMessage(coupon: Coupon): String {
    var message = "Cupon válido"

    if (coupon.minimumPrice != null) {
        message = message.plus(" para pedidos mayores a $${coupon.minimumPrice}")
    }

    if (coupon.maximumDiscount != null) {
        message = message.plus(" con un tope de reintegro de $${coupon.minimumPrice}")
    }

    if (coupon.minimumPrice == null && coupon.maximumDiscount == null) {
        message = message.plus(" para cualquier valor de pedido")
    }

    return message
}