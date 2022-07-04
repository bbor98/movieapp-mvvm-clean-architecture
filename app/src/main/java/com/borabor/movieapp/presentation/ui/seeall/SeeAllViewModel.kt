package com.borabor.movieapp.presentation.ui.seeall

import android.os.Parcelable
import androidx.lifecycle.viewModelScope
import com.borabor.movieapp.domain.model.MovieList
import com.borabor.movieapp.domain.model.PersonList
import com.borabor.movieapp.domain.model.TvList
import com.borabor.movieapp.domain.usecase.GetByGenre
import com.borabor.movieapp.domain.usecase.GetList
import com.borabor.movieapp.domain.usecase.GetSearchResults
import com.borabor.movieapp.presentation.ui.base.BaseViewModel
import com.borabor.movieapp.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeeAllViewModel @Inject constructor(
    private val getList: GetList,
    private val getSearchResults: GetSearchResults,
    private val getByGenre: GetByGenre
) : BaseViewModel() {

    private val _results = MutableStateFlow(setOf<Any>())
    val results get() = _results.asStateFlow()

    private var intentType: Parcelable? = null
    private var mediaType: Parcelable? = null
    private var detailId: Int? = null
    private var listId: String? = null

    private var region: String? = null
    private var page = 1

    private fun fetchList() {
        viewModelScope.launch {
            getList(
                mediaType = mediaType as MediaType,
                listId = listId,
                page = page,
                region = region
            ).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _results.value += when (mediaType) {
                            MediaType.MOVIE -> (response.data as MovieList).results
                            MediaType.TV -> (response.data as TvList).results
                            else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
                        }
                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.errorState(errorText = response.message)
                    }
                }
            }
        }
    }

    private fun fetchSearchResults() {
        viewModelScope.launch {
            getSearchResults(
                mediaType = mediaType as MediaType,
                query = listId!!,
                page = page
            ).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _results.value += when (mediaType) {
                            MediaType.MOVIE -> (response.data as MovieList).results
                            MediaType.TV -> (response.data as TvList).results
                            else -> (response.data as PersonList).results
                        }
                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.errorState(errorText = response.message)
                    }
                }
            }
        }
    }

    private fun fetchGenreResults() {
        viewModelScope.launch {
            getByGenre(
                mediaType = mediaType as MediaType,
                genreId = detailId!!,
                page = page
            ).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _results.value += when (mediaType) {
                            MediaType.MOVIE -> (response.data as MovieList).results
                            MediaType.TV -> (response.data as TvList).results
                            else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
                        }
                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.errorState(errorText = response.message)
                    }
                }
            }
        }
    }

    fun onLoadMore(type: Any?) {
        _uiState.value = UiState.loadingState()
        page++

        when (intentType) {
            IntentType.LIST -> fetchList()
            IntentType.SEARCH -> fetchSearchResults()
            IntentType.GENRE -> fetchGenreResults()
        }
    }

    fun initRequest(intentType: Parcelable?, mediaType: Parcelable?, detailId: Int?, listId: String?, region: String?) {
        this.intentType = intentType
        this.mediaType = mediaType
        this.detailId = detailId
        this.listId = listId
        this.region = region

        when (intentType) {
            IntentType.LIST -> fetchList()
            IntentType.SEARCH -> fetchSearchResults()
            IntentType.GENRE -> fetchGenreResults()
            else -> _uiState.value = UiState.successState()
        }
    }
}