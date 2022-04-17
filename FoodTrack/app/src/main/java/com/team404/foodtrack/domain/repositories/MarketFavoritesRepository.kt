package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.domain.persistence.MarketFavoritesDao

class MarketFavoritesRepository(private val marketFavoritesDao: MarketFavoritesDao) {

    fun search() : List<MarketFavorites> {
        return marketFavoritesDao.search()
    }

    fun insert(marketFavorites: MarketFavorites) : Long {
        return marketFavoritesDao.insert(marketFavorites)
    }

    fun delete(marketFavorites: MarketFavorites) : Long {
        return marketFavoritesDao.delete(marketFavorites)
    }
}