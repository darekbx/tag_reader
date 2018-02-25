package com.tagreader.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.Toast
import com.tagreader.R
import com.tagreader.repository.storage.entities.Item
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
    private val urlFormat = "https://www.wykop.pl/tag/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(menuListener)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        with(viewModel) {
            itemList.observe(this@MainActivity, Observer { itemList ->
                itemList?.let { itemList ->
                    loadList(itemList)
                }
            })
            refresher.observe(this@MainActivity, Observer { refresh() })
            errorMessage.observe(this@MainActivity, Observer { message ->
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            })
            loadStoredTags()
        }

        setListActions()
    }

    private fun setListActions() {
        listView.setOnItemLongClickListener { adapterView, view, i, l ->
            viewModel.delete(itemsAdapter.getItem(i))
            false
        }
        listView.setOnItemClickListener { adapterView, view, i, l -> handleClick(i) }
    }

    private fun handleClick(i: Int) {
        val item = itemsAdapter.getItem(i)
        viewModel.updateOpened(item)
        openBrowser(item)
    }

    private fun openBrowser(item: Item) {
        val intent = Intent(Intent.ACTION_VIEW).apply { data = createTagUri(item) }
        startActivity(intent)
    }

    private fun createTagUri(item: Item) =
            Uri.parse(urlFormat + item.tagName)

    fun refresh() {
        itemsAdapter.notifyDataSetInvalidated()
    }

    fun loadList(items: List<Item>) {
        if (listView.adapter == null) {
            listView.adapter = itemsAdapter
        }
        with (itemsAdapter) {
            clear()
            addAll(items)
            notifyDataSetChanged()
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

    private val itemsAdapter by lazy { ItemsAdapter(this) }
    private val listView by lazy { findViewById(R.id.list) as ListView }
}
