package com.coroutinedispatcher.cats

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
class NavigationViewModel : ViewModel() {
    private val _state: MutableStateFlow<NavigationState> = MutableStateFlow(NavigationState.Home)
    val state: StateFlow<NavigationState> = _state

    sealed class NavigationState {
        object Home : NavigationState()
        object ImageDetails : NavigationState()
    }

    fun navigateTo(state: NavigationState) {
        _state.value = state
    }

    fun goBack() {
        _state.value = NavigationState.Home
    }
}
