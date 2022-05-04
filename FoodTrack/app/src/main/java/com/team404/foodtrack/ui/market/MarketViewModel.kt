package com.team404.foodtrack.ui.market

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team404.foodtrack.data.MarketData
import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.domain.mappers.MarketFavoritesMapper
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.utils.SnackbarBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MarketViewModel(
    private val marketRepository: MarketRepository,
    private val marketFavoritesRepository: MarketFavoritesRepository
) : ViewModel() {

    private val marketFavoritesMapper: MarketFavoritesMapper = MarketFavoritesMapper()
    val marketData = MutableLiveData<MarketData>()

    fun getMarketData(view : View, marketId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val marketResponse = marketRepository.searchById(marketId)
            val marketFavoriteResponse = marketFavoritesRepository.searchByMarketId(marketId)

            if (marketResponse != null) {
                withContext(Dispatchers.Main) {
                    val isFavorite = marketFavoriteResponse != null
                    var newMarketData = MarketData.Builder()
                        .market(marketResponse)
                        .isFavorite(isFavorite)
                        .build()
                    marketData.value = newMarketData
                }
            } else {
                SnackbarBuilder.showErrorMessage(view)
            }
        }
    }

    fun changeMarketFavorite() {
        if (marketData != null && marketData.value != null && marketData.value!!.market != null) {
            CoroutineScope(Dispatchers.Default).launch {
                val market = marketData.value!!.market!!
                var isFavorite = false

                var marketFavorite: MarketFavorites? =
                    marketFavoritesRepository.searchByMarketId(market.id!!)

                    if (marketFavorite != null) {
                        marketFavoritesRepository.delete(marketFavorite)
                        marketData.value!!.isFavorite = false
                    } else {
                        marketFavorite = marketFavoritesMapper.map(market)
                        marketFavoritesRepository.insert(marketFavorite)
                        isFavorite = true
                    }
                withContext(Dispatchers.Main) {
                    val newMarketData = MarketData.Builder()
                        .market(market)
                        .isFavorite(isFavorite)
                        .build()
                    marketData.value = newMarketData
                }
            }
        }
    }
}