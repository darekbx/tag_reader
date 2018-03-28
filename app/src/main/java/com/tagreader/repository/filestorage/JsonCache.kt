package com.tagreader.repository.filestorage

import android.os.Environment
import java.io.File

class JsonCache(val tag: String) {

    fun save(json: String) {
        val file = getFile()
        with(file) {
            mkdirs()
            if (exists())
                delete()
            appendText(json, Charsets.UTF_8)
        }
    }

    fun load(): String? {
        val file = getFile()
        with(file) {
            return when (exists()) {
                true -> readText(Charsets.UTF_8)
                else -> null
            }
        }
    }

    private fun getFile() = File(Environment.getExternalStorageDirectory(), "tags/$tag.cache")
}