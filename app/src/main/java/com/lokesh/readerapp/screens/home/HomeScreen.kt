package com.lokesh.readerapp.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.lokesh.readerapp.R
import com.lokesh.readerapp.components.HomeListCardItem
import com.lokesh.readerapp.components.ReaderApp
import com.lokesh.readerapp.components.ReaderTopBar
import com.lokesh.readerapp.navigation.Screens
import com.lokesh.readerapp.screens.Utils

@Composable
fun HomeScreen(navigation: NavHostController = NavHostController(LocalContext.current)) {
    ReaderApp {
        Scaffold(
            topBar = {
                ReaderTopBar(title = "Loki Collection", showProfile = true, navigation = navigation)
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
                HomeContent(navigation)
            }
        }
    }
}

@Composable
fun HomeContent(navigation: NavHostController) {
    Column(
        modifier = Modifier.padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.Top
    ) {
        LatestReading(navigation)
        Spacer(modifier = Modifier.height(15.dp))
        ReadingList(navigation)
    }
}

@Composable
fun LatestReading(navigation: NavHostController) {
    val name = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: "Unknown"
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TitleSection(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.latest_readings)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(0.25f)
                .clickable {
                    navigation.navigate(Screens.StatsScreen)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(R.string.profile_icon),
                tint = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier.size(35.dp)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip
            )
        }
    }
    LazyRow(
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = Utils.dummyList, key = {it.id ?: ""}) {
            HomeListCardItem(data = it) {
                navigation.navigate(Screens.DetailScreen)
            }
        }
    }
}

@Composable
fun ReadingList(navigation: NavHostController) {
    TitleSection(label = stringResource(R.string.reading_list))
    LazyRow(
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = Utils.dummyList, key = {it.id ?: ""}) {
            HomeListCardItem(data = it) {
                navigation.navigate(Screens.DetailScreen)
            }
        }
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String = "Sample text"
) {
    Surface(
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge
        )
    }
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
