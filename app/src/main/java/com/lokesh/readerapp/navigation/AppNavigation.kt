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
import com.lokesh.readerapp.screens.search.SearchScreen
import com.lokesh.readerapp.screens.search.viewmodel.SearchViewModel
import com.lokesh.readerapp.screens.splash.SplashScreen
import com.lokesh.readerapp.screens.stats.StatsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SplashScreen) {
        composable<Screens.SplashScreen> {
            SplashScreen(navController)
        }

        composable<Screens.LoginScreen> {
            LoginScreen(navController)
        }

        composable<Screens.HomeScreen> {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(navController, viewModel)
        }

        composable<Screens.StatsScreen> {
            StatsScreen(navController)
        }

        composable<Screens.DetailScreen> {
            val args = it.toRoute<Screens.DetailScreen>()
            val viewModel: SearchViewModel = hiltViewModel()
            DetailScreen(navController, args, viewModel)
        }

        composable<Screens.SearchScreen> {
            val viewModel: SearchViewModel = hiltViewModel()
            SearchScreen(navController, viewModel)
        }
    }
}