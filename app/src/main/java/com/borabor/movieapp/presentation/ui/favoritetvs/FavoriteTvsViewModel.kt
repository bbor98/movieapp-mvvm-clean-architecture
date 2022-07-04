package com.borabor.movieapp.presentation.ui.favoritetvs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borabor.movieapp.domain.model.FavoriteTv
import com.borabor.movieapp.domain.usecase.AddFavorite
import com.borabor.movieapp.domain.usecase.DeleteFavorite
import com.borabor.movieapp.domain.usecase.GetFavorites
import com.borabor.movieapp.util.MediaType
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
) : ViewModel() {

    private val _favoriteTvs = MutableStateFlow(emptyList<FavoriteTv>())
    val favoriteTvs get() = _favoriteTvs.asStateFlow()

    fun fetchFavoriteTvs() {
        viewModelScope.launch {
            getFavorites(MediaType.TV).collect {
                _favoriteTvs.value = it as List<FavoriteTv>
            }
        }
    }

    fun removeTvFromFavorites(tv: FavoriteTv) {
        viewModelScope.launch {
            deleteFavorite(mediaType = MediaType.TV, tv = tv)
            fetchFavoriteTvs()
        }
    }

    fun addTvToFavorites(tv: FavoriteTv) {
        viewModelScope.launch {
            addFavorite(mediaType = MediaType.TV, tv = tv)
            fetchFavoriteTvs()
        }
    }

    init {
        fetchFavoriteTvs()
    }
}