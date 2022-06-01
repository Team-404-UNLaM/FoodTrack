package com.team404.foodtrack.ui.camera

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.team404.foodtrack.R
import com.team404.foodtrack.data.MarketData
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.utils.SnackbarBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraViewModel(
    private val marketRepository: MarketRepository,
) : ViewModel() {
    val marketData = MutableLiveData<MarketData>()

    fun getMarketData(view: View, marketId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val marketResponse = marketRepository.searchById(marketId)
                if (marketResponse != null) {
                    withContext(Dispatchers.Main) {

                        val newMarketData = MarketData.Builder()
                            .market(marketResponse)
                            .build()
                        marketData.value = newMarketData
                    }
                } else {
                    SnackbarBuilder.showErrorMessage(view)
                }
            } catch (e: NoSuchElementException) {
                Snackbar.make(view, "QR no asociado a Food Track", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}