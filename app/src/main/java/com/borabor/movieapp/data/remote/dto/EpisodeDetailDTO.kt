package com.borabor.movieapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class EpisodeDetailDTO(
    @SerializedName("credits")
    val credits: CreditsDTO,
    @SerializedName("images")
    val images: ImageListDTO,
    @SerializedName("videos")
    val videos: VideoListDTO
)