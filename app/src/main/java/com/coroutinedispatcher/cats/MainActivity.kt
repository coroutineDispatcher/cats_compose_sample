package com.coroutinedispatcher.cats

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import com.coroutinedispatcher.cats.ui.CatsTheme
import com.coroutinedispatcher.cats.ui.customWebView.WebPageScreen
import com.coroutinedispatcher.cats.ui.home.HomeScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val navigationViewModel by viewModels<NavigationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val injector = (application as CatsApplication).injector
        setContent {
            CatsTheme {
                Surface(color = MaterialTheme.colors.background) {
                    CatsAppScafold(
                        navigationViewModel = navigationViewModel,
                        networkClient = injector.networkClient
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        navigationViewModel.goBack()
    }
}

@ExperimentalCoroutinesApi
@Composable
fun CatsAppScafold(
    navigationViewModel: NavigationViewModel,
    networkClient: NetworkClient
) {
    val currentState = navigationViewModel.state.collectAsState()
    Scaffold(
        backgroundColor = Color.White,
        topBar = {
            when (currentState.value) {
                NavigationViewModel.NavigationState.Home -> TopAppBar(title = { Text(text = "Cats <3") })
                is NavigationViewModel.NavigationState.ImageDetails -> TopAppBarWithBackButton(
                    currentState.value
                ) {
                    navigationViewModel.goBack()
                }
            }
        },
        bodyContent = {
            Crossfade(
                current = currentState
            ) { screenState ->
                when (val screenStateValue = screenState.value) {
                    NavigationViewModel.NavigationState.Home -> {
                        HomeScreen(
                            navigateTo = navigationViewModel::navigateTo,
                            networkClient = networkClient
                        )
                    }
                    is NavigationViewModel.NavigationState.ImageDetails -> {
                        WebPageScreen(screenStateValue.urlToRender)
                    }
                }
            }
        }
    )
}

@ExperimentalCoroutinesApi
@Composable
fun TopAppBarWithBackButton(
    navigationState: NavigationViewModel.NavigationState,
    action: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Cats <3") },
        navigationIcon = {
            if (navigationState != NavigationViewModel.NavigationState.Home)
                IconButton(onClick = { action() }) {
                    Icon(vectorResource(R.drawable.ic_baseline_arrow_white_24))
                }
        }
    )
}
