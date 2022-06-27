package com.team404.foodtrack.domain.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.ui.market.favs.FavsViewModel

@Suppress("UNCHECKED_CAST")
class FavsMarketsViewModelFactory(
    private val marketsFavoritesRepository: MarketFavoritesRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavsViewModel(marketsFavoritesRepository) as T
    }
}