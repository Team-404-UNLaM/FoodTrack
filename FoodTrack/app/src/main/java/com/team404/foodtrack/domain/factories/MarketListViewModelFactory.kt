package com.team404.foodtrack.domain.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.ui.market.MarketListViewModel

@Suppress("UNCHECKED_CAST")
class MarketListViewModelFactory(private val marketRepository: MarketRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MarketListViewModel(marketRepository) as T
    }
}