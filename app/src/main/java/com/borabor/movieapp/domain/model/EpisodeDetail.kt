package com.borabor.movieapp.domain.model

data class EpisodeDetail(
    val airDate: String?,
    val credits: Credits,
    val images: ImageList,
    val name: String,
    val overview: String?,
    val runtime: Int?,
    val stillPath: String?,
    val videos: VideoList,
    val voteAverage: Double,
    val voteCount: Int
) {
    companion object {
        val empty = EpisodeDetail(
            airDate = null,
            credits = Credits.empty,
            images = ImageList.empty,
            name = "",
            overview = null,
            runtime = null,
            stillPath = null,
            videos = VideoList.empty,
            voteAverage = 0.0,
            voteCount = 0
        )
    }
}