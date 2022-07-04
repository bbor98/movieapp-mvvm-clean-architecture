package com.borabor.movieapp.domain.usecase

import com.borabor.movieapp.domain.repository.MovieRepository
import com.borabor.movieapp.domain.repository.TvRepository
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavorites @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    operator fun invoke(mediaType: MediaType): Flow<List<Any>> = flow {
        emit(
            when (mediaType) {
                MediaType.MOVIE -> movieRepository.getFavoriteMovies()
                MediaType.TV -> tvRepository.getFavoriteTvs()
                else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
            }
        )
    }
}