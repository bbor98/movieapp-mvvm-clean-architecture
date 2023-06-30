package com.borabor.movieapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.movieapp.databinding.ItemTrendingTvBinding
import com.borabor.movieapp.databinding.ItemTvBinding
import com.borabor.movieapp.domain.model.Tv

class TvAdapter(
    private val isGrid: Boolean = false,
    private val isTrending: Boolean = false,
    private val isCredits: Boolean = false,
    private val onTrendingFabClick: ((Int) -> Unit)? = null
) : ListAdapter<Tv, RecyclerView.ViewHolder>(DiffCallback) {
    inner class HorizontalViewHolder private constructor(val view: ItemTvBinding) : RecyclerView.ViewHolder(view.root) {
        constructor(parent: ViewGroup) : this(ItemTvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class TrendingViewHolder private constructor(val view: ItemTrendingTvBinding) : RecyclerView.ViewHolder(view.root) {
        constructor(parent: ViewGroup) : this(ItemTrendingTvBinding.inflate(LayoutInflater.from(parent.context), parent, false)) {
            view.fabTrailer.setOnClickListener {
                onTrendingFabClick?.invoke(getItem(adapterPosition).id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isTrending) TrendingViewHolder(parent) else HorizontalViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HorizontalViewHolder -> {
                holder.view.apply {
                    isGrid = this@TvAdapter.isGrid
                    isCredits = this@TvAdapter.isCredits
                    tv = getItem(position)
                }
            }

            is TrendingViewHolder -> holder.view.tv = getItem(position)
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Tv>() {
        override fun areItemsTheSame(oldItem: Tv, newItem: Tv): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Tv, newItem: Tv): Boolean = oldItem == newItem
    }
}