package com.borabor.movieapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.ItemSeasonBinding
import com.borabor.movieapp.domain.model.Season

class SeasonAdapter(
    private val tvId: Int
) : ListAdapter<Season, SeasonAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(val view: ItemSeasonBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_season, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.apply {
            tvId = this@SeasonAdapter.tvId
            season = getItem(position)
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Season>() {
        override fun areItemsTheSame(oldItem: Season, newItem: Season): Boolean = oldItem.seasonNumber == newItem.seasonNumber
        override fun areContentsTheSame(oldItem: Season, newItem: Season): Boolean = oldItem == newItem
    }
}