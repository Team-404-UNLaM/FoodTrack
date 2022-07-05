package com.team404.foodtrack.ui.history

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team404.foodtrack.data.OrderHistory
import com.team404.foodtrack.domain.services.OrdersHistoryService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersHistoryViewModel(
    private val ordersHistoryService: OrdersHistoryService
) : ViewModel() {

    val ordersHistoryList = MutableLiveData<MutableList<OrderHistory>>()

    fun getMarketList(view : View) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ordersHistoryService.search()

            withContext(Dispatchers.Main) {
                ordersHistoryList.value = response as MutableList<OrderHistory>
            }
        }
    }

    fun getFilteredMarketList(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ordersHistoryService.searchByName(name)

            withContext(Dispatchers.Main) {
                ordersHistoryList.value = response as MutableList<OrderHistory>
            }
        }
    }
}