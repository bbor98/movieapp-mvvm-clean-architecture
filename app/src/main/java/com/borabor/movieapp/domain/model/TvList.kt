package com.borabor.movieapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class TvList(
    val results: List<Tv>,
    val totalResults: Int
) {
    companion object {
        val empty = TvList(
            results = emptyList(),
            totalResults = 0
        )
    }
}

@Parcelize
data class Tv(
    val character: String?,
    val firstAirDate: String?,
    val id: Int,
    val job: String?,
    val name: String,
    val overview: String?,
    val posterPath: String?,
    val voteAverage: Double
) : Parcelable