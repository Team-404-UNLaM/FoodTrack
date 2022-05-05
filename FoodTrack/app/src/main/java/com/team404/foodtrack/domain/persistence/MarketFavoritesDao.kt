package com.team404.foodtrack.domain.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.team404.foodtrack.data.database.MarketFavorites

@Dao
interface MarketFavoritesDao {

    @Query("SELECT * FROM MARKET_FAVORITES")
    suspend fun search() : List<MarketFavorites>

    @Query("SELECT * FROM MARKET_FAVORITES WHERE marketId = :marketId")
    suspend fun searchByMarketId(marketId: Long) : MarketFavorites?

    @Insert
    suspend fun insert(marketFavorites: MarketFavorites)

    @Delete
    suspend fun delete(marketFavorites: MarketFavorites)
}