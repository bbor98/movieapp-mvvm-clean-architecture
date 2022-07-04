package com.borabor.movieapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class SeasonDetailDTO(
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("credits")
    val credits: CreditsDTO,
    @SerializedName("episodes")
    val episodes: List<EpisodeDTO>,
    @SerializedName("images")
    val images: ImageListDTO,
    @SerializedName("name")
    val name: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("season_number")
    val seasonNumber: Int,
    @SerializedName("videos")
    val videos: VideoListDTO
)

data class EpisodeDTO(
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("episode_number")
    val episodeNumber: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("season_number")
    val seasonNumber: Int,
    @SerializedName("still_path")
    val stillPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)