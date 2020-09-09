package com.coroutinedispatcher.cats.ui.home

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coroutinedispatcher.cats.NavigationViewModel
import com.coroutinedispatcher.cats.NetworkClient
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun HomeScreen(
    navigateTo: (NavigationViewModel.NavigationState) -> Unit,
    networkClient: NetworkClient
) {
    val homeViewModel: HomeViewModel = viewModel(null, object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            HomeViewModel(networkClient) as T
    })

    val state = homeViewModel.state.collectAsState()

    when (val statesValue = state.value) {
        HomeViewModel.State.Idle -> homeViewModel.getPosts()
        HomeViewModel.State.Loading -> {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalGravity = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        is HomeViewModel.State.Success -> {
            LazyColumnFor(items = statesValue.breedsList) { breed ->
                Card {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = breed.name, fontSize = 24.sp)
                        Text(text = breed.description, fontSize = 20.sp, maxLines = 2)
                        Divider()
                    }
                }
            }
        }
        is HomeViewModel.State.Error -> Snackbar(text = {
            Text(text = "Error loading data")
        })
    }
}
