package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.domain.persistence.MarketFavoritesDao

class MarketFavoritesRepository(private val marketFavoritesDao: MarketFavoritesDao) {

    suspend fun search() : List<MarketFavorites> {
        return marketFavoritesDao.search()
    }

    suspend fun insert(marketFavorites: MarketFavorites) {
        return marketFavoritesDao.insert(marketFavorites)
    }

    suspend fun delete(marketFavorites: MarketFavorites) {
        return marketFavoritesDao.delete(marketFavorites)
    }
}