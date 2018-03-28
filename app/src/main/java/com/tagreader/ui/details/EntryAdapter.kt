package com.tagreader.ui.details

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tagreader.databinding.AdapterEntryBinding
import com.tagreader.model.Entry

class EntryAdapter(context: Context, val entries: List<Entry>, val itemClick: (Entry) -> Unit) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EntryViewHolder {
        val binding = AdapterEntryBinding.inflate(inflater, parent, false)
        return EntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryViewHolder?, position: Int) {
        holder?.bind(entries[position], itemClick)
    }

    override fun getItemCount() = entries.size

    val inflater by lazy {
        LayoutInflater.from(context)
    }

    class EntryViewHolder(val binding: AdapterEntryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: Entry, itemClick: (Entry) -> Unit) {
            binding.entry = entry
            binding.container.setOnClickListener { itemClick(entry) }
        }
    }
}