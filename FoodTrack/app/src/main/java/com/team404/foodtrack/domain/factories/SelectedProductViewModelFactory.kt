package com.team404.foodtrack.domain.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.ui.market.MarketListViewModel
import com.team404.foodtrack.ui.market.MarketViewModel
import com.team404.foodtrack.ui.order.SelectedProductViewModel

@Suppress("UNCHECKED_CAST")
class SelectedProductViewModelFactory() : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectedProductViewModel() as T
    }
}