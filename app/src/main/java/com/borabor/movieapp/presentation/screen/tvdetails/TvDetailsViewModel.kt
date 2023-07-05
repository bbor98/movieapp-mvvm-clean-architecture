package com.borabor.movieapp.presentation.screen.tvdetails

import androidx.lifecycle.viewModelScope
import com.borabor.movieapp.domain.model.FavoriteTv
import com.borabor.movieapp.domain.model.TvDetail
import com.borabor.movieapp.domain.usecase.AddFavorite
import com.borabor.movieapp.domain.usecase.CheckFavorites
import com.borabor.movieapp.domain.usecase.DeleteFavorite
import com.borabor.movieapp.domain.usecase.GetDetails
import com.borabor.movieapp.presentation.base.BaseViewModel
import com.borabor.movieapp.presentation.screen.UiState
import com.borabor.movieapp.util.Detail
import com.borabor.movieapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvDetailsViewModel @Inject constructor(
    private val getDetails: GetDetails,
    private val checkFavorites: CheckFavorites,
    private val deleteFavorite: DeleteFavorite,
    private val addFavorite: AddFavorite
) : BaseViewModel() {

    private val _details = MutableStateFlow(TvDetail.empty)
    val details get() = _details.asStateFlow()

    private val _isInFavorites = MutableStateFlow(false)
    val isInFavorites get() = _isInFavorites.asStateFlow()

    private lateinit var favoriteTv: FavoriteTv

    private fun fetchTvDetails() {
        viewModelScope.launch {
            getDetails(Detail.TV, detailId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        (response.data as TvDetail).apply {
                            _details.value = this

                            favoriteTv = FavoriteTv(
                                id = id,
                                episodeRunTime = if (episodeRunTime.isEmpty()) 0 else episodeRunTime[0],
                                firstAirDate = firstAirDate,
                                name = name,
                                posterPath = posterPath,
                                voteAverage = voteAverage,
                                voteCount = voteCount,
                                date = System.currentTimeMillis()
                            )
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

    private fun checkFavorites() {
        viewModelScope.launch {
            _isInFavorites.value = checkFavorites(Detail.TV, detailId)
        }
    }

    fun updateFavorites() {
        viewModelScope.launch {
            if (_isInFavorites.value) {
                deleteFavorite(detailType = Detail.TV, tv = favoriteTv)
                _isInFavorites.value = false
            } else {
                addFavorite(detailType = Detail.TV, tv = favoriteTv)
                _isInFavorites.value = true
            }
        }
    }

    fun initRequest(tvId: Int) {
        detailId = tvId
        checkFavorites()
        fetchTvDetails()
    }
}