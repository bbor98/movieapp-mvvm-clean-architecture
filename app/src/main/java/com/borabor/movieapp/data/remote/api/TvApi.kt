package com.borabor.movieapp.data.remote.api

import com.borabor.movieapp.data.remote.dto.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvApi {

    @GET("tv/{list_id}")
    suspend fun getTvList(
        @Path("list_id") listId: String,
        @Query("page") page: Int
    ): TvListDTO

    @GET("trending/tv/week")
    suspend fun getTrendingTvs(): TvListDTO

    @GET("tv/{tv_id}/videos")
    suspend fun getTrendingTvTrailers(
        @Path("tv_id") tvId: Int
    ): VideoListDTO

    @GET("discover/tv")
    suspend fun getTvsByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): TvListDTO

    @GET("search/tv")
    suspend fun getTvSearchResults(
        @Query("query") query: String,
        @Query("page") page: Int
    ): TvListDTO

    @GET("tv/{tv_id}?&append_to_response=credits,videos,images,recommendations,external_ids")
    suspend fun getTvDetails(
        @Path("tv_id") tvId: Int
    ): TvDetailDTO

    @GET("tv/{tv_id}/season/{season_number}?&append_to_response=credits,videos,images")
    suspend fun getSeasonDetails(
        @Path("tv_id") tvId: Int,
        @Path("season_number") seasonNumber: Int
    ): SeasonDetailDTO

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}?&append_to_response=credits,videos,images")
    suspend fun getEpisodeDetails(
        @Path("tv_id") tvId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): EpisodeDetailDTO
}