package com.borabor.movieapp.domain.usecase

import com.borabor.movieapp.domain.repository.MovieRepository
import com.borabor.movieapp.domain.repository.TvRepository
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.Detail
import com.borabor.movieapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetList @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    operator fun invoke(
        detailType: Detail,
        listId: String?,
        page: Int? = null,
        region: String? = null
    ): Flow<Resource<Any>> = flow {
        emit(
            when (detailType) {
                Detail.MOVIE -> {
                    if (listId == null) movieRepository.getTrendingMovies()
                    else movieRepository.getMovieList(listId, page!!, region)
                }

                Detail.TV -> {
                    if (listId == null) tvRepository.getTrendingTvs()
                    else tvRepository.getTvList(listId, page!!)
                }

                else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_DETAIL_TYPE)
            }
        )
    }
}