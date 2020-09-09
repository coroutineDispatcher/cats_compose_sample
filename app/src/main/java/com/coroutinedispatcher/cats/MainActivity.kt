package com.coroutinedispatcher.cats

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.setContent
import com.coroutinedispatcher.cats.ui.CatsTheme
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
                    CatsApp(
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
fun CatsApp(
    navigationViewModel: NavigationViewModel,
    networkClient: NetworkClient
) {
    val currentState = navigationViewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Cats <3") }
            )
        },
        bodyContent = {
            Crossfade(current = currentState) { screenState ->
                when (screenState.value) {
                    NavigationViewModel.NavigationState.Home -> {
                        HomeScreen(
                            navigateTo = navigationViewModel::navigateTo,
                            networkClient = networkClient
                        )
                    }
                    NavigationViewModel.NavigationState.ImageDetails -> {
                    }
                }
            }
        }
    )
}
