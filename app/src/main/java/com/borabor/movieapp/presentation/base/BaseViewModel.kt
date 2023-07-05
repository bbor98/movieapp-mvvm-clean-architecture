package com.borabor.movieapp.presentation.base

import androidx.lifecycle.ViewModel
import com.borabor.movieapp.presentation.screen.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.properties.Delegates

abstract class BaseViewModel : ViewModel() {

    protected var detailId by Delegates.notNull<Int>()

    protected val areResponsesSuccessful by lazy { mutableListOf<Boolean>() }

    protected var isInitial = false

    protected lateinit var errorText: String

    protected val _uiState = MutableStateFlow(UiState.loadingState())
    val uiState get() = _uiState.asStateFlow()

    protected fun setUiState() {
        _uiState.value = if (areResponsesSuccessful.contains(false)) UiState.errorState(isInitial, errorText) else UiState.successState()
        areResponsesSuccessful.clear()
    }

    fun retryConnection(action: () -> Unit) {
        _uiState.value = UiState.loadingState(isInitial)
        action()
    }
}