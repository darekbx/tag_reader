package com.tagreader.extensions

import android.arch.lifecycle.MutableLiveData

class ActionLiveData: MutableLiveData<Double>() {

    fun invokeAction() {
        postValue(Math.random())
    }
}