package com.lokesh.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lokesh.readerapp.screens.details.DetailScreen
import com.lokesh.readerapp.screens.home.HomeScreen
import com.lokesh.readerapp.screens.home.viewmodel.HomeViewModel
import com.lokesh.readerapp.screens.login.view.LoginScreen
import com.lokesh.readerapp.screens.login.viewmodel.LoginViewModel
import com.lokesh.readerapp.screens.search.SearchScreen
import com.lokesh.readerapp.screens.search.viewmodel.SearchViewModel
import com.lokesh.readerapp.screens.splash.SplashScreen
import com.lokesh.readerapp.screens.stats.StatsScreen
import com.lokesh.readerapp.screens.update.UpdateScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SplashScreen) {
        composable<Screens.SplashScreen> {
            SplashScreen(navController)
        }

        composable<Screens.LoginScreen> {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController, viewModel)
        }

        composable<Screens.HomeScreen> {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(navController, viewModel)
        }

        composable<Screens.StatsScreen> {
            val viewModel: HomeViewModel = hiltViewModel()
            StatsScreen(navController, viewModel)
        }

        composable<Screens.DetailScreen> {
            val args = it.toRoute<Screens.DetailScreen>()
            val viewModel: SearchViewModel = hiltViewModel()
            DetailScreen(navController, args, viewModel)
        }

        composable<Screens.UpdateScreen> {
            val viewModel = hiltViewModel<HomeViewModel>()
            val args = it.toRoute<Screens.UpdateScreen>()
            UpdateScreen(navController, args, viewModel)
        }

        composable<Screens.SearchScreen> {
            val viewModel: SearchViewModel = hiltViewModel()
            SearchScreen(navController, viewModel)
        }
    }
}