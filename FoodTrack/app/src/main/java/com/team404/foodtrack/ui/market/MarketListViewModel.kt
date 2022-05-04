package com.team404.foodtrack.ui.market

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.utils.SnackbarBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MarketListViewModel(
    private val marketRepository: MarketRepository
) : ViewModel() {

    val marketList = MutableLiveData<MutableList<Market>>()

    fun getMarketList(view : View) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = marketRepository.search()

            if (response != null) {
                withContext(Dispatchers.Main) {
                    marketList.value = response as MutableList<Market>
                }
            } else {
                SnackbarBuilder.showErrorMessage(view)
            }
        }
    }
}