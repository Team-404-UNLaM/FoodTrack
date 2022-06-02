package com.team404.foodtrack.ui.order

import com.team404.foodtrack.data.Product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectedProductViewModel() : ViewModel() {

    val selectedProducts = MutableLiveData<MutableMap<Long, List<Any>>>()

    fun initializeSelectedProducts(products: MutableMap<Long, List<Any>>? = null) {

        selectedProducts.value = mutableMapOf()

        if(products != null) {
            selectedProducts.value = products!!
        }
    }

    fun changeProductQuantity(product: Product, newProductQuantity: Int) {
        val newSelectedProduct = mutableMapOf<Long, List<Any>>()
        newSelectedProduct.putAll(selectedProducts.value!!)
        val productId = product.id
        val productSelectedData = listOf(product, newProductQuantity)

        if (newSelectedProduct[productId] == null) {
            newSelectedProduct.put(productId!!, productSelectedData)
        } else {
            if(newProductQuantity == 0) {
                newSelectedProduct.remove(productId)
            } else {
                newSelectedProduct[productId!!] = productSelectedData
            }
        }

        selectedProducts.value = newSelectedProduct
    }
}