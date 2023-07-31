package com.iti.android_4.adapter.save_quote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android_4.databinding.SaveCardBinding
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel

class SaveQuoteRVAdapter(private var savedQuotesArrayList: ArrayList<SavedQuoteLocalDataModel>?) :
    RecyclerView.Adapter<SaveQuoteRVAdapter.SavedQuotesAdapter>() {

    inner class SavedQuotesAdapter(private val binding: SaveCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val savedQuote = binding.tvSavedContent
        val savedAuthor = binding.tvSavedAuthor
        val savedDate = binding.tvSavedDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedQuotesAdapter {
        val binding =
            SaveCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedQuotesAdapter(binding)
    }

    override fun getItemCount(): Int {
        return savedQuotesArrayList!!.size
    }

    override fun onBindViewHolder(holder: SavedQuotesAdapter, position: Int) {
        val itemData = savedQuotesArrayList!![position]

        holder.savedQuote.text = itemData.quote
        holder.savedAuthor.text = itemData.author
        holder.savedDate.text = itemData.date
    }


}