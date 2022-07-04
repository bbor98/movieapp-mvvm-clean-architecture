package com.borabor.movieapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ImageList(
    val backdrops: List<Image>?,
    val posters: List<Image>?,
    val profiles: List<Image>?,
    val stills: List<Image>?
) {
    companion object {
        val empty = ImageList(
            backdrops = null,
            posters = null,
            profiles = null,
            stills = null
        )
    }
}

@Parcelize
data class Image(
    val filePath: String
) : Parcelable