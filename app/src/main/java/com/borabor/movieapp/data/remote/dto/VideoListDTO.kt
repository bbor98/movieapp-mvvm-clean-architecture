package com.borabor.movieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoListDTO(
    @SerializedName("results")
    val results: List<VideoDTO>
)

data class VideoDTO(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("published_at")
    val publishedAt: String,
    @SerializedName("site")
    val site: String,
    @SerializedName("type")
    val type: String
)