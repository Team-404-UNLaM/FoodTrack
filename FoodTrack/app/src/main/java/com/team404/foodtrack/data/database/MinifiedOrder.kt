package com.team404.foodtrack.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ORDER")
data class MinifiedOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val marketId: Long,
    val date: String,
    val consumptioModeId: Long,
    val paymentMethodId: Long,
    val totalPrice: Double,
    val discountedPrice: Double
)
