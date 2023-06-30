package com.borabor.movieapp.domain.repository

import com.borabor.movieapp.domain.model.EpisodeDetail
import com.borabor.movieapp.domain.model.FavoriteTv
import com.borabor.movieapp.domain.model.SeasonDetail
import com.borabor.movieapp.domain.model.TvDetail
import com.borabor.movieapp.domain.model.TvList
import com.borabor.movieapp.domain.model.VideoList
import com.borabor.movieapp.util.Resource

interface TvRepository {
    suspend fun getTvList(listId: String, page: Int): Resource<TvList>
    suspend fun getTrendingTvs(): Resource<TvList>
    suspend fun getTrendingTvTrailers(tvId: Int): Resource<VideoList>
    suspend fun getTvsByGenre(genreId: Int, page: Int): Resource<TvList>
    suspend fun getTvSearchResults(query: String, page: Int): Resource<TvList>
    suspend fun getTvDetails(tvId: Int): Resource<TvDetail>
    suspend fun getSeasonDetails(tvId: Int, seasonNumber: Int): Resource<SeasonDetail>
    suspend fun getEpisodeDetails(tvId: Int, seasonNumber: Int, episodeNumber: Int): Resource<EpisodeDetail>
    suspend fun getFavoriteTvs(): List<FavoriteTv>
    suspend fun tvExists(tvId: Int): Boolean
    suspend fun insertTv(tv: FavoriteTv)
    suspend fun deleteTv(tv: FavoriteTv)
}