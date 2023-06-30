package com.borabor.movieapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.ItemFavoriteTvBinding
import com.borabor.movieapp.domain.model.FavoriteTv

class FavoriteTvAdapter(
    private val onItemClicked: (item: FavoriteTv) -> Unit
) : ListAdapter<FavoriteTv, FavoriteTvAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(val view: ItemFavoriteTvBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.ivRemove.setOnClickListener { onItemClicked(getItem(adapterPosition)) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_favorite_tv, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.tv = getItem(position)
    }

    object DiffCallback : DiffUtil.ItemCallback<FavoriteTv>() {
        override fun areItemsTheSame(oldItem: FavoriteTv, newItem: FavoriteTv): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: FavoriteTv, newItem: FavoriteTv): Boolean = oldItem == newItem
    }
}