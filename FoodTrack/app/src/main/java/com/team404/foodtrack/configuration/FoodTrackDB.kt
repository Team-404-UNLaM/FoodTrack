package com.team404.foodtrack.configuration

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.data.database.MinifiedOrder
import com.team404.foodtrack.domain.persistence.MarketFavoritesDao
import com.team404.foodtrack.domain.persistence.MinifiedOrderDao

@Database(
    entities = [MarketFavorites::class, MinifiedOrder::class],
    version = 1,
    exportSchema = false
)

abstract class FoodTrackDB : RoomDatabase() {
    abstract fun marketFavoritesDao(): MarketFavoritesDao
    abstract fun minifiedOrderDao() : MinifiedOrderDao

    companion object {
        @Volatile
        private var INSTANCE: FoodTrackDB? = null

        fun getDatabase(context: Context): FoodTrackDB{
            val tempInstance = INSTANCE

            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodTrackDB::class.java,
                    "foodtrackdb"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}