package com.coroutinedispatcher.cats.ui.home

import androidx.compose.foundation.ClickableText
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Snackbar
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coroutinedispatcher.cats.NavigationViewModel
import com.coroutinedispatcher.cats.NetworkClient
import com.coroutinedispatcher.cats.model.Breed
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade
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
        HomeViewModel.State.Idle -> homeViewModel.getCatBreeds()
        HomeViewModel.State.Loading -> CenterLoadingIndicator()
        is HomeViewModel.State.Success -> CatBreedsList(
            breedsList = statesValue.breedsList,
            itemClickAction = navigateTo
        )
        is HomeViewModel.State.Error -> ErrorView { homeViewModel.getCatBreeds() }
    }
}

@Composable
fun ErrorView(retryAction: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(all = 2.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Snackbar(text = { Text(text = "Error loading data") }, action = {
            ClickableText(text = AnnotatedString("Retry"), onClick = {
                retryAction()
            })
        })
    }
}

@ExperimentalCoroutinesApi
@Composable
fun CatBreedsList(
    breedsList: List<Breed>,
    itemClickAction: (NavigationViewModel.NavigationState) -> Unit
) {
    LazyColumnFor(breedsList) { breed ->
        RippleIndication()
        Card(Modifier.padding(8.dp).clickable(onClick = {
            itemClickAction(NavigationViewModel.NavigationState.ImageDetails(breed.wikipediaUrl))
        })) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = breed.name, fontSize = 24.sp)
                CoilImageWithCrossfade(
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        .fillMaxHeight(fraction = 300f)
                        .fillMaxWidth(fraction = 300f),
                    data = breed.imageUrl,
                    loading = { CenterLoadingIndicator() }
                )
                Text(text = breed.description, fontSize = 16.sp, maxLines = 2)
            }
        }
    }
}

@Composable
fun CenterLoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalGravity = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}
