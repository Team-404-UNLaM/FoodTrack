package com.team404.foodtrack.domain.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.team404.foodtrack.data.database.MinifiedOrder

@Dao
interface MinifiedOrderDao {

    @Query("SELECT * FROM `ORDER`")
    suspend fun search() : List<MinifiedOrder>

    @Query("SELECT * FROM `ORDER` WHERE id = :orderId")
    suspend fun searchByMarketId(orderId: Long) : MinifiedOrder?

    @Insert
    suspend fun insert(minifiedOrder: MinifiedOrder) : Long
}