package com.lokesh.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lokesh.readerapp.screens.home.HomeScreen
import com.lokesh.readerapp.screens.login.view.LoginScreen
import com.lokesh.readerapp.screens.splash.SplashScreen

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
            HomeScreen(navController)
        }
    }
}