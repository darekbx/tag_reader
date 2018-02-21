package com.tagreader.repository.storage

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.tagreader.repository.storage.entities.Item

@Dao
interface ItemsDao {

    @Query("SELECT * FROM item")
    fun listAll(): List<Item>

    @Query("SELECT COUNT(id) FROM item WHERE tagname = :tag")
    fun countOccurrences(tag: String) : Int

    @Insert
    fun add(item: Item): Long

    @Delete
    fun delete(item: Item)
}