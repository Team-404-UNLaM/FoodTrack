package com.team404.foodtrack.domain.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.team404.foodtrack.data.database.MarketFavorites

@Dao
interface MarketFavoritesDao {

    @Query("SELECT * FROM MARKET_FAVORITES")
    fun search() : List<MarketFavorites>

    @Insert
    fun insert(marketFavorites: MarketFavorites) : Long

    @Delete
    fun delete(marketFavorites: MarketFavorites) : Long
}