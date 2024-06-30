package com.lokesh.readerapp.navigation

import kotlinx.serialization.Serializable

object Screens {
    @Serializable
    object SplashScreen

    @Serializable
    object LoginScreen

    @Serializable
    object HomeScreen

    @Serializable
    object SearchScreen

    @Serializable
    data class UpdateScreen(val bookId: String?)

    @Serializable
    object StatsScreen

    @Serializable
    data class DetailScreen(val bookId: String)
}