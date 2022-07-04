package com.borabor.movieapp.domain.model

data class FavoriteMovie(
    val id: Int,
    val posterPath: String?,
    val releaseDate: String?,
    val runtime: Int?,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int,
    val date: Long
)