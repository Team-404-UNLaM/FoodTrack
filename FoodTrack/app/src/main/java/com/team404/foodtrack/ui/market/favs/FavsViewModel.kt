package com.team404.foodtrack.ui.market.favs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavsViewModel(
    private val marketFavoritesRepository: MarketFavoritesRepository
) : ViewModel() {

    val marketsFavorites : MutableState<List<MarketFavorites>> = mutableStateOf(listOf())

    init{
        CoroutineScope(Dispatchers.IO).launch{
            marketsFavorites.value = marketFavoritesRepository.search()
        }
    }

    fun deleteMarketFromFavorites(marketFavorites: MarketFavorites){
        CoroutineScope(Dispatchers.IO).launch {
            marketFavoritesRepository.delete(marketFavorites)
            updateMarketsFavorites()
        }
    }

    fun updateMarketsFavorites(){
        CoroutineScope(Dispatchers.IO).launch {
            marketsFavorites.value = marketFavoritesRepository.search()
        }
    }
}