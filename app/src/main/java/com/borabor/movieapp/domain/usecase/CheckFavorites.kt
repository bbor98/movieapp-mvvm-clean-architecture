package com.borabor.movieapp.domain.usecase

import com.borabor.movieapp.domain.repository.MovieRepository
import com.borabor.movieapp.domain.repository.TvRepository
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.MediaType
import javax.inject.Inject

class CheckFavorites @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    suspend operator fun invoke(
        mediaType: MediaType,
        id: Int
    ): Boolean = when (mediaType) {
        MediaType.MOVIE -> movieRepository.movieExists(id)
        MediaType.TV -> tvRepository.tvExists(id)
        else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
    }
}