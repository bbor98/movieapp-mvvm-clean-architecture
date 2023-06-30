package com.borabor.movieapp.presentation.ui.search

import androidx.lifecycle.viewModelScope
import com.borabor.movieapp.domain.model.Movie
import com.borabor.movieapp.domain.model.MovieList
import com.borabor.movieapp.domain.model.Person
import com.borabor.movieapp.domain.model.PersonList
import com.borabor.movieapp.domain.model.Tv
import com.borabor.movieapp.domain.model.TvList
import com.borabor.movieapp.domain.usecase.GetSearchResults
import com.borabor.movieapp.presentation.ui.base.BaseViewModel
import com.borabor.movieapp.util.MediaType
import com.borabor.movieapp.util.Resource
import com.borabor.movieapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val getSearchResults: GetSearchResults) : BaseViewModel() {

    private val _isSearchInitialized = MutableStateFlow(false)
    val isSearchInitialized get() = _isSearchInitialized.asStateFlow()

    private val _query = MutableStateFlow("")
    val query get() = _query.asStateFlow()

    private val _movieResults = MutableStateFlow(emptyList<Movie>())
    val movieResults get() = _movieResults.asStateFlow()

    private val _movieTotalResults = MutableStateFlow(0)
    val movieTotalResults get() = _movieTotalResults.asStateFlow()

    private val _tvResults = MutableStateFlow(emptyList<Tv>())
    val tvResults get() = _tvResults.asStateFlow()

    private val _tvTotalResults = MutableStateFlow(0)
    val tvTotalResults get() = _tvTotalResults.asStateFlow()

    private val _personResults = MutableStateFlow(emptyList<Person>())
    val personResults get() = _personResults.asStateFlow()

    private val _personTotalResults = MutableStateFlow(0)
    val personTotalResults get() = _personTotalResults.asStateFlow()

    private var pageMovie = 1
    private var pageTv = 1
    private var pagePerson = 1

    private var isQueryChanged = false

    private suspend fun fetchSearchResults(mediaType: MediaType) {
        val page = when (mediaType) {
            MediaType.MOVIE -> pageMovie
            MediaType.TV -> pageTv
            MediaType.PERSON -> pagePerson
        }

        getSearchResults(mediaType, query.value, page).collect { response ->
            when (response) {
                is Resource.Success -> {
                    when (mediaType) {
                        MediaType.MOVIE -> with(response.data as MovieList) {
                            _movieResults.value = if (isQueryChanged) results else _movieResults.value + results
                            _movieTotalResults.value = totalResults
                        }

                        MediaType.TV -> with(response.data as TvList) {
                            _tvResults.value = if (isQueryChanged) results else _tvResults.value + results
                            _tvTotalResults.value = totalResults
                        }

                        MediaType.PERSON -> with(response.data as PersonList) {
                            _personResults.value = if (isQueryChanged) results else _personResults.value + results
                            _personTotalResults.value = totalResults
                        }
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
        isQueryChanged = false

        when (type as MediaType) {
            MediaType.MOVIE -> pageMovie++
            MediaType.TV -> pageTv++
            MediaType.PERSON -> pagePerson++
        }

        viewModelScope.launch {
            coroutineScope { fetchSearchResults(type) }
            setUiState()
        }
    }

    fun fetchInitialSearch(query: String) {
        _uiState.value = UiState.loadingState(isInitial)
        _isSearchInitialized.value = true
        _query.value = query

        isQueryChanged = true
        isInitial = true

        pageMovie = 1
        pageTv = 1
        pagePerson = 1

        initRequests()
    }

    fun clearSearchResults() {
        _isSearchInitialized.value = false
        _query.value = ""

        _movieResults.value = emptyList()
        _tvResults.value = emptyList()
        _personResults.value = emptyList()
    }

    fun initRequests() {
        viewModelScope.launch {
            coroutineScope {
                launch {
                    fetchSearchResults(MediaType.MOVIE)
                    fetchSearchResults(MediaType.TV)
                    fetchSearchResults(MediaType.PERSON)
                }
            }
            setUiState()
        }
    }
}