package com.tagreader.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.tagreader.extensions.threadToAndroid
import com.tagreader.repository.storage.ItemsDatabase
import com.tagreader.repository.storage.entities.Item
import io.reactivex.Single

class MainViewModel(application: Application): AndroidViewModel(application) {

    class ExistsException : Exception()

    var itemList = MutableLiveData<List<Item>>()
    val errorMessage = MutableLiveData<String>()

    fun loadStoredTags() {
        Single
                .fromCallable { itemsDao.listAll() }
                .threadToAndroid()
                .subscribe({ items -> itemList.value = items }, { e -> })
    }

    fun addtag(tag: String) {
        Single
                .fromCallable { itemsDao.countOccurrences(tag) }
                .doOnSuccess { count -> if (count > 0) throw ExistsException() }
                .map { itemsDao.add(Item(tagName = tag)) }
                .threadToAndroid()
                .subscribe({ value -> loadStoredTags() }, { e ->
                    when {
                        e is ExistsException -> errorMessage.value = "Tag already exists"
                        else -> errorMessage.value = e.message
                    }
                })
    }

    private val itemsDao by lazy { database.getItemsDao() }
    private val database by lazy { ItemsDatabase.getInstance(application.applicationContext) }
}