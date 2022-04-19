package com.team404.foodtrack.utils

import com.team404.foodtrack.data.Market

fun filterMarketsByName(name: String, marketList: MutableList<Market>): MutableList<Market> {
    val marketListFiltered = mutableListOf<Market>()

    marketList.forEach { market ->
        val transformedMarketName = market.name?.let {
            transformToLowercaseAndReplaceSpaceWithDash(
                it
            )
        }

        if (transformedMarketName != null) {
            if (transformedMarketName.contains(name)) {
                marketListFiltered.add(market)
            }
        }

        }

    return marketListFiltered
}