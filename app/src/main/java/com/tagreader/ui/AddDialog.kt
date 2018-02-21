package com.tagreader.ui

import android.app.DialogFragment
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.tagreader.R
import kotlinx.android.synthetic.main.dialog_add.*

class AddDialog: DialogFragment() {

    interface Callback {
        fun onResult(tag: String)
    }

    private lateinit var callback: (String) -> Unit

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(context).inflate(R.layout.dialog_add, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        addButton.setOnClickListener { invokeAdd() }
    }

    fun setCallback(callback: (String) -> Unit) {
        this.callback = callback
    }

    private fun invokeAdd() {
        if (!validate()) {
            input.setError(getString(R.string.error_input_invalid))
            return
        }
        callback?.let { callback ->
            callback.invoke(inputText.text.toString())
            dismiss()
        }
    }

    private fun validate(): Boolean {
        val text = inputText.text.toString();
        return when {
            TextUtils.isEmpty(text) -> false
            text.indexOf('#') != 0 -> false
            else -> true
        }
    }

    private val inputText by lazy { view.findViewById(R.id.input) as EditText }
    private val addButton by lazy { view.findViewById(R.id.button_add) as Button }
}