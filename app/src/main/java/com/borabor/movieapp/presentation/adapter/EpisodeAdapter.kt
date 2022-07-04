package com.borabor.movieapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.ItemEpisodeBinding
import com.borabor.movieapp.domain.model.Episode

class EpisodeAdapter(
    private val backgroundColor: Int,
    private val onItemClicked: (episode: Episode) -> Unit,
) : ListAdapter<Episode, EpisodeAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(val view: ItemEpisodeBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.root.setOnClickListener {
                onItemClicked(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_episode, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.apply {
            backgroundColor = this@EpisodeAdapter.backgroundColor
            episode = getItem(position)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem.episodeNumber == newItem.episodeNumber
            }

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem == newItem
            }
        }
    }
}