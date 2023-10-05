package com.iti.android_4.adapter.search_quote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android_4.R
import com.iti.android_4.databinding.SaveCardBinding
import com.iti.android_4.models.quotes.Result

class SearchQuoteRVAdapter(
    private val data: ArrayList<Result>
) :
    RecyclerView.Adapter<SearchQuoteRVAdapter.SearchQuotesAdapter>() {

    inner class SearchQuotesAdapter(private val binding: SaveCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val savedQuote = binding.tvSavedContent
        val savedAuthor = binding.tvSavedAuthor
        val savedDate = binding.tvSavedDate
        val savedButton = binding.btnSavedFavorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchQuotesAdapter {
        val binding =
            SaveCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchQuotesAdapter(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchQuotesAdapter, position: Int) {
        val itemData = data[position]

        holder.savedButton.setImageResource(R.drawable.ic_favorite_border)
        holder.savedQuote.text = itemData.content
        holder.savedAuthor.text = itemData.author
        holder.savedDate.text = itemData.dateAdded
    }
}