package com.team404.foodtrack.domain.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team404.foodtrack.domain.services.OrdersHistoryService
import com.team404.foodtrack.ui.history.OrdersHistoryViewModel

@Suppress("UNCHECKED_CAST")
class OrdersHistoryViewModelFactory(private val ordersHistoryService: OrdersHistoryService) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrdersHistoryViewModel(ordersHistoryService) as T
    }
}