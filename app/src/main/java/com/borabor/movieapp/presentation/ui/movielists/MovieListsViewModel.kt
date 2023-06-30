package com.borabor.movieapp.presentation.ui.movielists

import androidx.lifecycle.viewModelScope
import com.borabor.movieapp.domain.model.Movie
import com.borabor.movieapp.domain.model.MovieList
import com.borabor.movieapp.domain.usecase.GetList
import com.borabor.movieapp.domain.usecase.GetTrendingVideos
import com.borabor.movieapp.presentation.ui.base.BaseViewModel
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.MediaType
import com.borabor.movieapp.util.Resource
import com.borabor.movieapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MovieListsViewModel @Inject constructor(
    private val getList: GetList,
    private val getTrendingVideos: GetTrendingVideos
) : BaseViewModel() {

    private val _trendingMovies = MutableStateFlow(emptyList<Movie>())
    val trendingMovies get() = _trendingMovies.asStateFlow()

    private val _popularMovies = MutableStateFlow(emptyList<Movie>())
    val popularMovies get() = _popularMovies.asStateFlow()

    private val _topRatedMovies = MutableStateFlow(emptyList<Movie>())
    val topRatedMovies get() = _topRatedMovies.asStateFlow()

    private val _nowPlayingMovies = MutableStateFlow(emptyList<Movie>())
    val nowPlayingMovies get() = _nowPlayingMovies.asStateFlow()

    private val _countryName = MutableStateFlow("")
    val countryName get() = _countryName.asStateFlow()

    private val _countryCode = MutableStateFlow("")
    val countyCode get() = _countryCode.asStateFlow()

    private val _upcomingMovies = MutableStateFlow(emptyList<Movie>())
    val upcomingMovies get() = _upcomingMovies.asStateFlow()

    private var pagePopular = 1
    private var pageTopRated = 1
    private var pageNowPlaying = 1
    private var pageUpcoming = 1

    private suspend fun fetchList(listId: String? = null) {
        val page = when (listId) {
            Constants.LIST_ID_POPULAR -> pagePopular
            Constants.LIST_ID_TOP_RATED -> pageTopRated
            Constants.LIST_ID_NOW_PLAYING -> pageNowPlaying
            Constants.LIST_ID_UPCOMING -> pageUpcoming
            else -> null
        }

        getList(
            mediaType = MediaType.MOVIE,
            listId = listId,
            page = page,
            region = if (listId == Constants.LIST_ID_NOW_PLAYING) _countryCode.value else null
        ).collect { response ->
            when (response) {
                is Resource.Success -> {
                    val movieList = (response.data as MovieList).results
                    when (listId) {
                        Constants.LIST_ID_POPULAR -> _popularMovies.value += movieList
                        Constants.LIST_ID_TOP_RATED -> _topRatedMovies.value += movieList
                        Constants.LIST_ID_NOW_PLAYING -> _nowPlayingMovies.value += movieList
                        Constants.LIST_ID_UPCOMING -> _upcomingMovies.value += movieList
                        else -> _trendingMovies.value = movieList
                    }
                    areResponsesSuccessful.add(true)
                    isInitial = false
                }

                is Resource.Error -> {
                    errorText = response.message
                    areResponsesSuccessful.add(false)
                }
            }
        }
    }

    fun onLoadMore(type: Any?) {
        _uiState.value = UiState.loadingState(isInitial)

        when (type as String) {
            Constants.LIST_ID_POPULAR -> pagePopular++
            Constants.LIST_ID_TOP_RATED -> pageTopRated++
            Constants.LIST_ID_NOW_PLAYING -> pageNowPlaying++
            Constants.LIST_ID_UPCOMING -> pageUpcoming++
        }

        viewModelScope.launch {
            coroutineScope { fetchList(type) }
            setUiState()
        }
    }

    fun getTrendingMovieTrailerKey(movieId: Int): String = runBlocking {
        var videoKey = ""

        coroutineScope {
            getTrendingVideos(MediaType.MOVIE, movieId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        videoKey = response.data.filterVideos(true).last().key
                        _uiState.value = UiState.successState()
                    }

                    is Resource.Error -> {
                        _uiState.value = UiState.errorState(false, response.message)
                    }
                }
            }
        }

        videoKey
    }

    fun getNowPlayingMoviesInSelectedRegion(countryName: String, countryCode: String) {
        _uiState.value = UiState.loadingState(isInitial)
        _nowPlayingMovies.value = emptyList()
        _countryName.value = countryName
        _countryCode.value = countryCode

        pageNowPlaying = 1

        viewModelScope.launch {
            coroutineScope { fetchList(Constants.LIST_ID_NOW_PLAYING) }
            setUiState()
        }
    }

    fun initRequests() {
        viewModelScope.launch {
            coroutineScope {
                launch {
                    fetchList()
                    fetchList(Constants.LIST_ID_POPULAR)
                    fetchList(Constants.LIST_ID_TOP_RATED)
                    fetchList(Constants.LIST_ID_UPCOMING)
                }
            }
            setUiState()
        }
    }

    init {
        initRequests()
    }
}