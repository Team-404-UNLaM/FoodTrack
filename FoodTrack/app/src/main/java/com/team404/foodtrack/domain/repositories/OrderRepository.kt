package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.database.MinifiedOrder
import com.team404.foodtrack.domain.persistence.MinifiedOrderDao

class OrderRepository(private val minifiedOrderDao: MinifiedOrderDao) {

    suspend fun search() : List<MinifiedOrder> {
        return minifiedOrderDao.search()
    }

    suspend fun searchByOrderId(orderId: Long) : MinifiedOrder? {
        return minifiedOrderDao.searchByMarketId(orderId)
    }

    suspend fun insert(minifiedOrder: MinifiedOrder) : Long {
        return minifiedOrderDao.insert(minifiedOrder)
    }
}