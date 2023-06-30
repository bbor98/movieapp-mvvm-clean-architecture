package com.borabor.movieapp.domain.usecase

import com.borabor.movieapp.domain.model.VideoList
import com.borabor.movieapp.domain.repository.MovieRepository
import com.borabor.movieapp.domain.repository.TvRepository
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.MediaType
import com.borabor.movieapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTrendingVideos @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    operator fun invoke(
        mediaType: MediaType,
        id: Int
    ): Flow<Resource<VideoList>> = flow {
        emit(
            when (mediaType) {
                MediaType.MOVIE -> movieRepository.getTrendingMovieTrailers(id)
                MediaType.TV -> tvRepository.getTrendingTvTrailers(id)
                else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
            }
        )
    }
}