package com.borabor.movieapp.domain.model

data class External(
    val facebookId: String?,
    val imdbId: String?,
    val instagramId: String?,
    val twitterId: String?
) {
    companion object {
        val empty = External(
            facebookId = null,
            imdbId = null,
            instagramId = null,
            twitterId = null
        )
    }
}