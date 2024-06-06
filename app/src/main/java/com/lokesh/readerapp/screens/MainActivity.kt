package com.lokesh.readerapp.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lokesh.readerapp.components.ReaderApp
import com.lokesh.readerapp.navigation.AppNavigation
import com.lokesh.readerapp.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ReaderApp {
                    AppNavigation()
                }
            }
        }
    }
}