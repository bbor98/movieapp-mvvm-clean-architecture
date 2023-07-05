package com.borabor.movieapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class EpisodeDetailDTO(
    @SerializedName("air_date")
    val airDate: String?,
    @SerializedName("credits")
    val credits: CreditsDTO,
    @SerializedName("images")
    val images: ImageListDTO,
    @SerializedName("name")
    val name: String,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("still_path")
    val stillPath: String?,
    @SerializedName("videos")
    val videos: VideoListDTO,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)