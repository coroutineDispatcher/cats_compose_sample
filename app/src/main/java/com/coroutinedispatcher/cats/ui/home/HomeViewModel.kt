package com.coroutinedispatcher.cats.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coroutinedispatcher.cats.NetworkClient
import com.coroutinedispatcher.cats.model.Breed
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(private val networkClient: NetworkClient) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    private val postsExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _state.value = State.Error(throwable.message)
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Success(val breedsList: List<Breed>) : State()
        data class Error(val errorMessage: String?) : State()
    }

    fun getCatBreeds() {
        viewModelScope.launch(Dispatchers.IO + postsExceptionHandler) {
            _state.value = State.Loading
            val breedsResponse = networkClient.fetchBreeds()
            val breeds = breedsResponse.map { breed ->
                val imageResponse = networkClient.fetchImage(breed.id)
                breed.imageUrl = imageResponse[0].url
                return@map breed
            }

            _state.value = State.Success(breeds)
        }
    }
}
