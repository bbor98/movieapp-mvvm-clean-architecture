package com.borabor.movieapp.data.mapper

import com.borabor.movieapp.data.remote.dto.*
import com.borabor.movieapp.domain.model.*

internal fun CompanyDTO.toCompany() = Company(name, originCountry)

internal fun CountryDTO.toCountry() = Country(name)

internal fun CreatorDTO.toCreator() = Creator(name)

internal fun CreditsDTO.toCredits() = Credits(cast.map { it.toPerson() }, crew.map { it.toPerson() }, guestStars?.map { it.toPerson() })

internal fun EpisodeDTO.toEpisode() = Episode(airDate, episodeNumber, name, overview, seasonNumber, stillPath, voteAverage, voteCount)

internal fun EpisodeDetailDTO.toEpisodeDetail() = EpisodeDetail(credits.toCredits(), images.toImageList(), videos.toVideoList())

internal fun ExternalDTO.toExternal() = External(facebookId, imdbId, instagramId, twitterId)

internal fun GenreDTO.toGenre() = Genre(id, name)

internal fun ImageDTO.toImage() = Image(filePath)

internal fun ImageListDTO.toImageList() = ImageList(backdrops?.map { it.toImage() }, posters?.map { it.toImage() }, profiles?.map { it.toImage() }, stills?.map { it.toImage() })

internal fun LanguageDTO.toLanguage() = Language(englishName)

internal fun MovieDTO.toMovie() = Movie(character, id, job, overview, posterPath, releaseDate, title, voteAverage)

internal fun MovieCreditsDTO.toMovieCredits() = MovieCredits(cast.map { it.toMovie() }, crew.map { it.toMovie() })

internal fun MovieDetailDTO.toMovieDetail() = MovieDetail(
    budget,
    credits.toCredits(),
    externalIds.toExternal(),
    genres.map { it.toGenre() },
    homepage,
    id,
    images.toImageList(),
    originalTitle,
    overview,
    posterPath,
    productionCompanies.map { it.toCompany() },
    productionCountries.map { it.toCountry() },
    recommendations.toMovieList(),
    releaseDate,
    revenue,
    runtime,
    spokenLanguages.map { it.toLanguage() },
    status,
    title,
    videos.toVideoList(),
    voteAverage,
    voteCount
)

internal fun MovieListDTO.toMovieList() = MovieList(results.map { it.toMovie() }, totalResults)

internal fun PersonDTO.toPerson() = Person(character, department, id, job, knownForDepartment, name, profilePath)

internal fun PersonDetailDTO.toPersonDetail() = PersonDetail(
    alsoKnownAs,
    biography,
    birthday,
    deathday,
    externalIds.toExternal(),
    gender,
    homepage,
    id,
    images.toImageList(),
    knownForDepartment,
    movieCredits.toMovieCredits(),
    name,
    placeOfBirth,
    profilePath,
    tvCredits.toTvCredits()
)

internal fun PersonListDTO.toPersonList() = PersonList(results.map { it.toPerson() }, totalResults)

internal fun SeasonDTO.toSeason() = Season(airDate, episodeCount, name, posterPath, seasonNumber)

internal fun SeasonDetailDTO.toSeasonDetail() = SeasonDetail(
    airDate,
    credits.toCredits(),
    episodes.map { it.toEpisode() },
    images.toImageList(),
    name,
    overview,
    posterPath,
    seasonNumber,
    videos.toVideoList()
)

internal fun TvDTO.toTv() = Tv(character, firstAirDate, id, job, name, overview, posterPath, voteAverage)

internal fun TvCreditsDTO.toTvCredits() = TvCredits(cast.map { it.toTv() }, crew.map { it.toTv() })

internal fun TvDetailDTO.toTvDetail() = TvDetail(
    createdBy.map { it.toCreator() },
    credits.toCredits(),
    episodeRunTime,
    externalIds.toExternal(),
    firstAirDate,
    genres.map { it.toGenre() },
    homepage,
    id,
    images.toImageList(),
    inProduction,
    lastAirDate,
    name,
    networks.map { it.toCompany() },
    nextEpisodeToAir?.toEpisode(),
    numberOfEpisodes,
    numberOfSeasons,
    originalName,
    overview,
    posterPath,
    productionCompanies.map { it.toCompany() },
    productionCountries.map { it.toCountry() },
    recommendations.toTvList(),
    seasons.map { it.toSeason() },
    spokenLanguages.map { it.toLanguage() },
    status,
    videos.toVideoList(),
    voteAverage,
    voteCount
)

internal fun TvListDTO.toTvList() = TvList(results.map { it.toTv() }, totalResults)

internal fun VideoDTO.toVideo() = Video(key, name, publishedAt, site, type)

internal fun VideoListDTO.toVideoList() = VideoList(results.map { it.toVideo() })