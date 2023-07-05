package com.borabor.movieapp.presentation.screen.favorites.favoritetvs

import androidx.lifecycle.viewModelScope
import com.borabor.movieapp.domain.model.FavoriteTv
import com.borabor.movieapp.domain.usecase.AddFavorite
import com.borabor.movieapp.domain.usecase.DeleteFavorite
import com.borabor.movieapp.domain.usecase.GetFavorites
import com.borabor.movieapp.presentation.base.BaseViewModel
import com.borabor.movieapp.util.Detail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteTvsViewModel @Inject constructor(
    private val getFavorites: GetFavorites,
    private val deleteFavorite: DeleteFavorite,
    private val addFavorite: AddFavorite
) : BaseViewModel() {

    private val _favoriteTvs = MutableStateFlow(emptyList<FavoriteTv>())
    val favoriteTvs get() = _favoriteTvs.asStateFlow()

    fun fetchFavoriteTvs() {
        viewModelScope.launch {
            getFavorites(Detail.TV).collect {
                _favoriteTvs.value = it as List<FavoriteTv>
            }
        }
    }

    fun removeTvFromFavorites(tv: FavoriteTv) {
        viewModelScope.launch {
            deleteFavorite(detailType = Detail.TV, tv = tv)
            fetchFavoriteTvs()
        }
    }

    fun addTvToFavorites(tv: FavoriteTv) {
        viewModelScope.launch {
            addFavorite(detailType = Detail.TV, tv = tv)
            fetchFavoriteTvs()
        }
    }

    init {
        fetchFavoriteTvs()
    }
}