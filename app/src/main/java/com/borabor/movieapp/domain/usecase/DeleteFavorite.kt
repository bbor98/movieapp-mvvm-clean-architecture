package com.borabor.movieapp.domain.usecase

import com.borabor.movieapp.domain.model.FavoriteMovie
import com.borabor.movieapp.domain.model.FavoriteTv
import com.borabor.movieapp.domain.repository.MovieRepository
import com.borabor.movieapp.domain.repository.TvRepository
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.Detail
import javax.inject.Inject

class DeleteFavorite @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    suspend operator fun invoke(
        detailType: Detail,
        movie: FavoriteMovie? = null,
        tv: FavoriteTv? = null
    ) {
        when (detailType) {
            Detail.MOVIE -> movieRepository.deleteMovie(movie!!)
            Detail.TV -> tvRepository.deleteTv(tv!!)
            else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_DETAIL_TYPE)
        }
    }
}