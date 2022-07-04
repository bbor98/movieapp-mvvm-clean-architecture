package com.borabor.movieapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class MovieList(
    val results: List<Movie>,
    val totalResults: Int
) {
    companion object {
        val empty = MovieList(
            results = emptyList(),
            totalResults = 0
        )
    }
}

@Parcelize
data class Movie(
    val character: String?,
    val id: Int,
    val job: String?,
    val overview: String?,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String,
    val voteAverage: Double
) : Parcelable