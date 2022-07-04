package com.borabor.movieapp.domain.usecase

import com.borabor.movieapp.domain.model.FavoriteMovie
import com.borabor.movieapp.domain.model.FavoriteTv
import com.borabor.movieapp.domain.repository.MovieRepository
import com.borabor.movieapp.domain.repository.TvRepository
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.MediaType
import javax.inject.Inject

class AddFavorite @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    suspend operator fun invoke(
        mediaType: MediaType,
        movie: FavoriteMovie? = null,
        tv: FavoriteTv? = null
    ) {
        when (mediaType) {
            MediaType.MOVIE -> movieRepository.insertMovie(movie!!)
            MediaType.TV -> tvRepository.insertTv(tv!!)
            else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
        }
    }
}