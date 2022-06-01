package com.team404.foodtrack.domain.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.ui.camera.CameraViewModel

@Suppress("UNCHECKED_CAST")
class CameraViewModelFactory(private val marketRepository: MarketRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CameraViewModel(marketRepository) as T
    }
}