package com.borabor.movieapp.domain.model

data class EpisodeDetail(
    val credits: Credits,
    val images: ImageList,
    val videos: VideoList
) {
    companion object {
        val empty = EpisodeDetail(
            credits = Credits.empty,
            images = ImageList.empty,
            videos = VideoList.empty
        )
    }
}