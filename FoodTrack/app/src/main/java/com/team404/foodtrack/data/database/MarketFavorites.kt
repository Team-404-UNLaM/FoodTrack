package com.team404.foodtrack.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MARKET_FAVORITES")
data class MarketFavorites(
    @PrimaryKey
    val marketId: Long,
    val name: String,
    val city: String,
    val starts: Double,
)
