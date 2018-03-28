package com.tagreader.ui.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.tagreader.R
import com.tagreader.model.Entry
import com.tagreader.viewmodel.DetailsViewModel

class DetailsActivity : AppCompatActivity() {

    companion object {
        val TAG_NAME_KEY = "tag_name"
    }

    private lateinit var viewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val layoutManager = LinearLayoutManager(this)
        entriesList.setLayoutManager(layoutManager)

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        with(viewModel) {
            entries.observe(this@DetailsActivity, Observer { entries ->
                entriesList.adapter = EntryAdapter(applicationContext, entries ?: emptyList(), { openBrowser(it) })
                progress.visibility = View.GONE
            })
            errorMessage.observe(this@DetailsActivity, Observer { message ->
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            })
            if (intent.hasExtra(TAG_NAME_KEY)) {
                load(intent.getStringExtra(TAG_NAME_KEY))
            }
        }
    }

    private fun openBrowser(entry: Entry) {
        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(entry.url) }
        startActivity(intent)
    }

    private val entriesList by lazy { findViewById(R.id.entries_list) as RecyclerView }
    private val progress by lazy { findViewById(R.id.progress) as View }
}