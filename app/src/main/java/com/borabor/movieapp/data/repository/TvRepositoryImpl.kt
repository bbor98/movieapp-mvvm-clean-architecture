package com.borabor.movieapp.data.repository

import com.borabor.movieapp.data.local.dao.TvDao
import com.borabor.movieapp.data.mapper.toEpisodeDetail
import com.borabor.movieapp.data.mapper.toFavoriteTv
import com.borabor.movieapp.data.mapper.toFavoriteTvEntity
import com.borabor.movieapp.data.mapper.toSeasonDetail
import com.borabor.movieapp.data.mapper.toTvDetail
import com.borabor.movieapp.data.mapper.toTvList
import com.borabor.movieapp.data.mapper.toVideoList
import com.borabor.movieapp.data.remote.api.TvApi
import com.borabor.movieapp.domain.model.EpisodeDetail
import com.borabor.movieapp.domain.model.FavoriteTv
import com.borabor.movieapp.domain.model.SeasonDetail
import com.borabor.movieapp.domain.model.TvDetail
import com.borabor.movieapp.domain.model.TvList
import com.borabor.movieapp.domain.model.VideoList
import com.borabor.movieapp.domain.repository.TvRepository
import com.borabor.movieapp.util.Resource
import com.borabor.movieapp.util.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvRepositoryImpl @Inject constructor(
    private val api: TvApi,
    private val safeApiCall: SafeApiCall,
    private val dao: TvDao
) : TvRepository {
    override suspend fun getTvList(listId: String, page: Int): Resource<TvList> = safeApiCall.execute {
        api.getTvList(listId, page).toTvList()
    }

    override suspend fun getTrendingTvs(): Resource<TvList> = safeApiCall.execute {
        api.getTrendingTvs().toTvList()
    }

    override suspend fun getTrendingTvTrailers(tvId: Int): Resource<VideoList> = safeApiCall.execute {
        api.getTrendingTvTrailers(tvId).toVideoList()
    }

    override suspend fun getTvsByGenre(genreId: Int, page: Int): Resource<TvList> = safeApiCall.execute {
        api.getTvsByGenre(genreId, page).toTvList()
    }

    override suspend fun getTvSearchResults(query: String, page: Int): Resource<TvList> = safeApiCall.execute {
        api.getTvSearchResults(query, page).toTvList()
    }

    override suspend fun getTvDetails(tvId: Int): Resource<TvDetail> = safeApiCall.execute {
        api.getTvDetails(tvId).toTvDetail()
    }

    override suspend fun getSeasonDetails(tvId: Int, seasonNumber: Int): Resource<SeasonDetail> = safeApiCall.execute {
        api.getSeasonDetails(tvId, seasonNumber).toSeasonDetail()
    }

    override suspend fun getEpisodeDetails(tvId: Int, seasonNumber: Int, episodeNumber: Int): Resource<EpisodeDetail> = safeApiCall.execute {
        api.getEpisodeDetails(tvId, seasonNumber, episodeNumber).toEpisodeDetail()
    }

    override suspend fun getFavoriteTvs(): List<FavoriteTv> = dao.getAllTvs().map { it.toFavoriteTv() }

    override suspend fun tvExists(tvId: Int): Boolean = dao.tvExists(tvId)

    override suspend fun insertTv(tv: FavoriteTv) {
        dao.insertTv(tv.toFavoriteTvEntity())
    }

    override suspend fun deleteTv(tv: FavoriteTv) {
        dao.deleteTv(tv.toFavoriteTvEntity())
    }
}