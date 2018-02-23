package com.tagreader.repository.storage

import android.arch.persistence.room.*
import com.tagreader.repository.storage.entities.Item

@Dao
interface ItemsDao {

    @Query("SELECT * FROM item")
    fun listAll(): List<Item>

    @Query("SELECT COUNT(id) FROM item WHERE tagname = :tag")
    fun countOccurrences(tag: String) : Int

    @Update
    fun update(item: Item)

    @Insert
    fun add(item: Item): Long

    @Delete
    fun delete(item: Item)
}