package com.tagreader.repository.storage

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tagreader.repository.storage.entities.Item

@Database(entities = arrayOf(Item::class), version = 1)
abstract class ItemsDatabase : RoomDatabase() {

    abstract fun getItemsDao() : ItemsDao

    companion object {

        private val DB_NAME = "items_db"

        @Volatile
        private var INSTANCE: ItemsDatabase? = null

        fun getInstance(context: Context): ItemsDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context)
                            .also { database ->
                                INSTANCE = database
                            }
                }

        private fun buildDatabase(context: Context) =
                Room
                        .databaseBuilder(context.applicationContext, ItemsDatabase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .build()

    }
}