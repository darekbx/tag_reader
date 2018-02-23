package com.tagreader.repository.storage.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
        @PrimaryKey(autoGenerate = true) var id: Int? = null,
        @ColumnInfo(name = "tagname") var tagName: String = "",
        @ColumnInfo(name = "itemscount") var itemsCount: Int = 0) {

    @Ignore
    var difference = 0

    @Ignore
    var isLoaded = false

    fun isDifferent() = difference > 0
    fun information() = "%,d / %,d"
            .format(difference,  itemsCount)
            .replace(',', ' ')

}