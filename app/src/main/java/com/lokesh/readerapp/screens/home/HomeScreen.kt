package com.lokesh.readerapp.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.lokesh.readerapp.R
import com.lokesh.readerapp.components.ReaderApp
import com.lokesh.readerapp.navigation.Screens

@Composable
fun HomeScreen(navigation: NavHostController) {
    ReaderApp {
        Scaffold(
            topBar = {
                ReaderTopBar(navigation = navigation)
            },
            floatingActionButton = {
                FabContent {
                    // Not handled
                }
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(
    title: String = "Sample Text",
    showProfile: Boolean = true,
    navigation: NavHostController
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                if (showProfile) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_splash_logo),
                        contentDescription = stringResource(
                            R.string.app_logo
                        ),
                        modifier = Modifier
                            .size(25.dp)
                            .scale(1.2f)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    FirebaseAuth.getInstance().signOut().run {
                        navigation.navigate(Screens.LoginScreen)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_exit),
                    contentDescription = stringResource(
                        R.string.logout_icon
                    ),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}

@Composable
fun FabContent(onClick: (String) -> Unit = {}) {
    FloatingActionButton(
        onClick = {
            onClick("")
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_new_book),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    )
}
