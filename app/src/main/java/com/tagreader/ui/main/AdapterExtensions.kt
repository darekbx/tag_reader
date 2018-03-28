package com.tagreader.ui.main;

import android.databinding.BindingAdapter;
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

@BindingAdapter("android:textStyle")
fun setTextStyle(view: TextView, style:String) {
        when (style) {
                "bold" -> view.typeface = Typeface.DEFAULT_BOLD
                else -> view.typeface = Typeface.DEFAULT
        }
}

@BindingAdapter("app:src")
fun setSrc(view: ImageView, preview:String?) {
        preview?.let {
                Glide
                        .with(view)
                        .load(preview)
                        .into(view)
        }
}