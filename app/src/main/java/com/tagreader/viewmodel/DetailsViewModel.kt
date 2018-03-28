package com.tagreader.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.tagreader.extensions.threadToAndroid
import com.tagreader.model.Entry
import com.tagreader.model.TagWrapper
import com.tagreader.repository.filestorage.JsonCache
import io.reactivex.Single

class DetailsViewModel : ViewModel() {

    val entries = MutableLiveData<List<Entry>>()
    val errorMessage = MutableLiveData<String>()

    fun load(tag: String) {
        Single
                .fromCallable { JsonCache(tag).load() }
                .map { json -> Gson().fromJson(json, TagWrapper::class.java) }
                .filter { wrapper -> wrapper.items != null }
                .toSingle()
                .threadToAndroid()
                .subscribe(
                        { wrapper -> entries.postValue(wrapper.items.toList()) },
                        { e -> errorMessage.postValue(e.message) })
    }
}