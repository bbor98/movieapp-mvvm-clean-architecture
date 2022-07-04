package com.borabor.movieapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ImageListDTO(
    @SerializedName("backdrops")
    val backdrops: List<ImageDTO>?,
    @SerializedName("logos")
    val logos: List<ImageDTO>?,
    @SerializedName("posters")
    val posters: List<ImageDTO>?,
    @SerializedName("profiles")
    val profiles: List<ImageDTO>?,
    @SerializedName("stills")
    val stills: List<ImageDTO>?,
)

data class ImageDTO(
    @SerializedName("file_path")
    val filePath: String
)