package com.tagreader.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.tagreader.R
import com.tagreader.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val menuListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_add -> {
                showAddDialog()
                return@OnNavigationItemSelectedListener true
            }
        }
        return@OnNavigationItemSelectedListener false
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(menuListener)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        with(viewModel) {
            itemList.observe(this@MainActivity, Observer { itemList ->
                itemList?.let { itemList ->

                }
            })
            errorMessage.observe(this@MainActivity, Observer { message ->
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            })
            loadStoredTags()
        }
    }

    fun showAddDialog() {
        with(AddDialog()) {
            setCallback({ addTag(it) })
            show(createTransaction(), AddDialog::class.simpleName)
        }
    }

    private fun createTransaction() =
            fragmentManager
                    .beginTransaction()
                    .addToBackStack(null)

    fun addTag(tag: String) {
        viewModel.addtag(tag)
    }
}
