package com.borabor.movieapp.data.mapper

import com.borabor.movieapp.data.local.entity.FavoriteMovieEntity
import com.borabor.movieapp.data.local.entity.FavoriteTvEntity
import com.borabor.movieapp.domain.model.FavoriteMovie
import com.borabor.movieapp.domain.model.FavoriteTv

internal fun FavoriteMovie.toFavoriteMovieEntity() = FavoriteMovieEntity(id, posterPath, releaseDate, runtime, title, voteAverage, voteCount, date)

internal fun FavoriteMovieEntity.toFavoriteMovie() = FavoriteMovie(id, posterPath, releaseDate, runtime, title, voteAverage, voteCount, date)

internal fun FavoriteTv.toFavoriteTvEntity() = FavoriteTvEntity(id, episodeRunTime, firstAirDate, name, posterPath, voteAverage, voteCount, date)

internal fun FavoriteTvEntity.toFavoriteTv() = FavoriteTv(id, episodeRunTime, firstAirDate, name, posterPath, voteAverage, voteCount, date)