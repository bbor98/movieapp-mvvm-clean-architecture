package com.borabor.movieapp.data.repository

import com.borabor.movieapp.data.local.dao.MovieDao
import com.borabor.movieapp.data.mapper.*
import com.borabor.movieapp.data.remote.api.MovieApi
import com.borabor.movieapp.domain.model.FavoriteMovie
import com.borabor.movieapp.domain.model.MovieDetail
import com.borabor.movieapp.domain.model.MovieList
import com.borabor.movieapp.domain.model.VideoList
import com.borabor.movieapp.domain.repository.MovieRepository
import com.borabor.movieapp.util.Resource
import com.borabor.movieapp.util.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val safeApiCall: SafeApiCall,
    private val dao: MovieDao
) : MovieRepository {
    override suspend fun getMovieList(listId: String, page: Int, region: String?): Resource<MovieList> = safeApiCall.execute {
        api.getMovieList(listId, page, region).toMovieList()
    }

    override suspend fun getTrendingMovies(): Resource<MovieList> = safeApiCall.execute {
        api.getTrendingMovies().toMovieList()
    }

    override suspend fun getTrendingMovieTrailers(movieId: Int): Resource<VideoList> = safeApiCall.execute {
        api.getTrendingMovieTrailers(movieId).toVideoList()
    }

    override suspend fun getMoviesByGenre(genreId: Int, page: Int): Resource<MovieList> = safeApiCall.execute {
        api.getMoviesByGenre(genreId, page).toMovieList()
    }

    override suspend fun getMovieSearchResults(query: String, page: Int): Resource<MovieList> = safeApiCall.execute {
        api.getMovieSearchResults(query, page).toMovieList()
    }

    override suspend fun getMovieDetails(movieId: Int): Resource<MovieDetail> = safeApiCall.execute {
        api.getMovieDetails(movieId).toMovieDetail()
    }

    override suspend fun getFavoriteMovies(): List<FavoriteMovie> = dao.getAllMovies().map { it.toFavoriteMovie() }

    override suspend fun movieExists(movieId: Int): Boolean = dao.movieExists(movieId)

    override suspend fun insertMovie(movie: FavoriteMovie) {
        dao.insertMovie(movie.toFavoriteMovieEntity())
    }

    override suspend fun deleteMovie(movie: FavoriteMovie) {
        dao.deleteMovie(movie.toFavoriteMovieEntity())
    }
}