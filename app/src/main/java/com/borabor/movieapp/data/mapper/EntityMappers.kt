package com.borabor.movieapp.data.mapper

import com.borabor.movieapp.data.local.entity.FavoriteMovieEntity
import com.borabor.movieapp.data.local.entity.FavoriteTvEntity
import com.borabor.movieapp.domain.model.FavoriteMovie
import com.borabor.movieapp.domain.model.FavoriteTv

fun FavoriteMovie.toFavoriteMovieEntity(): FavoriteMovieEntity = FavoriteMovieEntity(
    id = id,
    posterPath = posterPath,
    releaseDate = releaseDate,
    runtime = runtime,
    title = title,
    voteAverage = voteAverage,
    voteCount = voteCount,
    date = date
)

fun FavoriteMovieEntity.toFavoriteMovie(): FavoriteMovie = FavoriteMovie(
    id = id,
    posterPath = posterPath,
    releaseDate = releaseDate,
    runtime = runtime,
    title = title,
    voteAverage = voteAverage,
    voteCount = voteCount,
    date = date
)

fun FavoriteTv.toFavoriteTvEntity(): FavoriteTvEntity = FavoriteTvEntity(
    id = id,
    episodeRunTime = episodeRunTime,
    firstAirDate = firstAirDate,
    name = name,
    posterPath = posterPath,
    voteAverage = voteAverage,
    voteCount = voteCount,
    date = date
)

fun FavoriteTvEntity.toFavoriteTv(): FavoriteTv = FavoriteTv(
    id = id,
    episodeRunTime = episodeRunTime,
    firstAirDate = firstAirDate,
    name = name,
    posterPath = posterPath,
    voteAverage = voteAverage,
    voteCount = voteCount,
    date = date
)