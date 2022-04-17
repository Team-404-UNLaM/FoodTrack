package com.team404.foodtrack.domain.mappers

import com.team404.foodtrack.data.Market
import com.team404.foodtrack.data.database.MarketFavorites

class MarketFavoritesMapper {

    fun map(market: Market) : MarketFavorites {
        return MarketFavorites(
            market.id!!,
            market.name!!,
            market.address!!.city,
            market.stars!!
        )
    }

}