package com.borabor.movieapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class MovieListDTO(
    @SerializedName("results")
    val results: List<MovieDTO>,
    @SerializedName("total_results")
    val totalResults: Int
)

data class MovieDTO(
    @SerializedName("character")
    val character: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("job")
    val job: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double
)