package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.Product
import com.team404.foodtrack.mockServer.MockServer

class ProductRepository(private val mockServer: MockServer) {

    fun searchById(id: Long) : Product {
        return mockServer.searchProductById(id)
    }

    fun searchByIds(ids: List<Long>) : List<Product> {
        return mockServer.searchProductsById(ids)
    }
}