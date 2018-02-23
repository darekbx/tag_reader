package com.tagreader.ui

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tagreader.R
import com.tagreader.databinding.AdapterItemBinding
import com.tagreader.repository.storage.entities.Item

@BindingAdapter("android:textStyle")
fun setTextStyle(view: TextView, style:String) {
    when (style) {
        "bold" -> view.typeface = Typeface.DEFAULT_BOLD
        else -> view.typeface = Typeface.DEFAULT
    }
}

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