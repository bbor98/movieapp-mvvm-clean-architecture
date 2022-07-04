package com.borabor.movieapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ExternalDTO(
    @SerializedName("facebook_id")
    val facebookId: String?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("instagram_id")
    val instagramId: String?,
    @SerializedName("twitter_id")
    val twitterId: String?
)