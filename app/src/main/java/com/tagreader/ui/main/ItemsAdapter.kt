package com.tagreader.ui.main

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.tagreader.R
import com.tagreader.databinding.AdapterItemBinding
import com.tagreader.repository.storage.entities.Item

class ItemsAdapter(context: Context) : ArrayAdapter<Item>(context, R.layout.adapter_item) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?) =
            when (convertView) {
                null -> DataBindingUtil.inflate(layoutInflater, R.layout.adapter_item, parent, false)
                else -> DataBindingUtil.bind(convertView) as AdapterItemBinding
            }
                    .apply { item = getItem(position) }
                    .root

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }
}