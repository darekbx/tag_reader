package com.tagreader.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tagreader.api.WykopService
import com.tagreader.extensions.ActionLiveData
import com.tagreader.extensions.threadToAndroid
import com.tagreader.model.TagWrapper
import com.tagreader.repository.filestorage.JsonCache
import com.tagreader.repository.storage.ItemsDatabase
import com.tagreader.repository.storage.entities.Item
import io.reactivex.Observable
import io.reactivex.Single

class MainViewModel(application: Application): AndroidViewModel(application) {

    class ExistsException : Exception()

    var itemList = MutableLiveData<List<Item>>()
    val errorMessage = MutableLiveData<String>()
    val refresher = ActionLiveData()

    fun loadStoredTags() {
        Single
                .fromCallable { itemsDao.listAll() }
                .threadToAndroid()
                .subscribe({ items ->
                    itemList.value = items
                    fetchCounters()
                }, { e -> showError(e) })
    }

    fun fetchCounters() {
        Observable
                .fromCallable { itemList.value }
                .flatMapIterable { list -> list }
                .flatMap { executeTag(it) }
                .threadToAndroid()
                .subscribe(
                        { item -> refresher.invokeAction() },
                        { e -> showError(e) })
    }

    fun updateOpened(item: Item) {
        Single
                .fromCallable { itemsDao.update(item) }
                .threadToAndroid()
                .subscribe({ }, { e -> showError(e) })
    }

    fun showError(e: Throwable) {
        e.printStackTrace()
        errorMessage.postValue(e.message)
    }

    fun executeTag(item: Item) =
            Observable
                    .fromCallable { service.getTag(item.tagName, 0, WykopService.API_KEY) }
                    .map { call -> call.execute() }
                    .map { response -> response.body() ?: throw IllegalStateException() }
                    .doOnNext { json -> JsonCache(item.tagName).save(json) }
                    .map { json -> Gson().fromJson(json, TagWrapper::class.java) }
                    .map { wrapper -> wrapper.meta.counters }
                    .map { counters ->
                        with(item) {
                            isLoaded = true
                            difference = counters.entries - itemsCount
                            itemsCount = counters.entries
                        }
                    }
                    .map { item }

    fun addtag(tag: String) {
        Single
                .fromCallable { itemsDao.countOccurrences(tag) }
                .doOnSuccess { count -> if (count > 0) throw ExistsException() }
                .map { itemsDao.add(Item(tagName = tag)) }
                .threadToAndroid()
                .subscribe({ value -> loadStoredTags() }, { e ->
                    when {
                        e is ExistsException -> errorMessage.value = "Tag already exists"
                        else -> showError(e)
                    }
                })
    }

    fun delete(item: Item) {
        Single
                .fromCallable { itemsDao.delete(item) }
                .threadToAndroid()
                .subscribe(
                        { loadStoredTags() },
                        { e -> errorMessage.postValue(e.message) })
    }

    private val itemsDao by lazy { database.getItemsDao() }
    private val database by lazy { ItemsDatabase.getInstance(application.applicationContext) }
    private val service by lazy { WykopService().apply { setup() }.wykopService }
}