package com.borabor.movieapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class VideoList(
    val results: List<Video>
) {
    fun filterVideos(onlyTrailers: Boolean = false) = results.filter {
        it.site == "YouTube" && (if (onlyTrailers) it.type == "Trailer" else (it.type == "Trailer" || it.type == "Teaser"))
    }

    companion object {
        val empty = VideoList(results = emptyList())
    }
}

@Parcelize
data class Video(
    val key: String,
    val name: String,
    val publishedAt: String,
    val site: String,
    val type: String
) : Parcelable