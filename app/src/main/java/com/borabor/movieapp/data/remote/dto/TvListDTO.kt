package com.borabor.movieapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class TvListDTO(
    @SerializedName("results")
    val results: List<TvDTO>,
    @SerializedName("total_results")
    val totalResults: Int
)

data class TvDTO(
    @SerializedName("character")
    val character: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("job")
    val job: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double
)