package com.borabor.movieapp.domain.model

data class SeasonDetail(
    val airDate: String?,
    val credits: Credits,
    val episodes: List<Episode>,
    val images: ImageList,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val seasonNumber: Int,
    val videos: VideoList,
) {
    companion object {
        val empty = SeasonDetail(
            airDate = null,
            credits = Credits.empty,
            episodes = emptyList(),
            images = ImageList.empty,
            name = "",
            overview = "",
            posterPath = null,
            seasonNumber = 0,
            videos = VideoList.empty
        )
    }
}

data class Episode(
    val airDate: String?,
    val episodeNumber: Int,
    val name: String,
    val seasonNumber: Int,
    val stillPath: String?,
    val voteAverage: Double,
    val voteCount: Int
) {
    companion object {
        val empty = Episode(
            airDate = null,
            episodeNumber = 0,
            name = "",
            seasonNumber = 0,
            stillPath = null,
            voteAverage = 0.0,
            voteCount = 0
        )
    }
}