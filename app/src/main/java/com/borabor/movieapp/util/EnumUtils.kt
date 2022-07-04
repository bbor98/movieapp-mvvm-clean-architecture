package com.borabor.movieapp.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class ExternalPlatform(
    val url: String? = null,
    val packageName: String? = null
) {
    IMDB(
        url = "https://www.imdb.com/title/",
        packageName = "com.imdb.mobile"
    ),
    FACEBOOK(
        url = "https://www.facebook.com/",
        packageName = "com.facebook.katana"
    ),
    INSTAGRAM(
        url = "https://www.instagram.com/",
        packageName = "com.instagram.android"
    ),
    TWITTER(
        url = "https://twitter.com/",
        packageName = "com.twitter.android"
    ),
    HOMEPAGE
}

enum class ImageQuality(val imageBaseUrl: String) {
    LOW("https://image.tmdb.org/t/p/w300"),
    MEDIUM("https://image.tmdb.org/t/p/w500"),
    HIGH("https://image.tmdb.org/t/p/w780"),
    ORIGINAL("https://image.tmdb.org/t/p/original")
}

@Parcelize
enum class IntentType : Parcelable {
    LIST, VIDEOS, CAST, IMAGES, RECOMMENDATIONS, PERSON_CREDITS, SEARCH, GENRE
}

@Parcelize
enum class MediaType : Parcelable {
    MOVIE, TV, PERSON
}