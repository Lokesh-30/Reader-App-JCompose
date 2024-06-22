package com.lokesh.readerapp.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.lokesh.readerapp.R
import com.lokesh.readerapp.components.HomeListCardItem
import com.lokesh.readerapp.components.ReaderTopBar
import com.lokesh.readerapp.model.Book
import com.lokesh.readerapp.model.Item
import com.lokesh.readerapp.navigation.Screens
import com.lokesh.readerapp.screens.home.viewmodel.HomeViewModel
import com.lokesh.readerapp.screens.search.viewmodel.SearchViewModel
import com.lokesh.readerapp.utils.Utils

@Composable
fun StatsScreen(
    navigation: NavHostController = NavHostController(LocalContext.current),
    viewModel: HomeViewModel
) {
    val bookList by viewModel.data
    val readingCount =
        bookList.data?.filter { it.startReading != null && it.finishedReading == null }?.size ?: 0
    val readBooks = bookList.data?.filter { it.finishedReading != null }
    Scaffold(
        topBar = {
            ReaderTopBar(
                navigation = navigation,
                showNavigation = true,
                title = stringResource(R.string.book_stats),
            ) {
                navigation.navigateUp()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "Hi ${Utils.getName()}",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.Black)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = "Your Stats",
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.Black)
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                    Text(text = "You're reading : $readingCount book")
                    Text(text = "You've read : ${readBooks?.size ?: 0} book")
                }
            }
            if (readBooks != null) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    contentPadding = PaddingValues(top = 5.dp, bottom = 15.dp)
                ) {
                    items(items = readBooks, key = { it.id ?: "" }) {
                        StatListItem(data = it) { bookId ->
                            navigation.navigate(Screens.UpdateScreen(bookId = bookId))
                        }
                    }
                }
            } else {
                Text(text = stringResource(R.string.no_books_found))
            }
        }
    }
}

@Composable
private fun StatListItem(
    data: Book,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(data.googleBookId.toString())
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberImagePainter(
                    data = data.imageUrl
                ),
                contentDescription = stringResource(
                    id = R.string.des_book_poster
                ),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(120.dp)
                    .width(100.dp)
            )
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = data.title ?: "Unknown",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    if ((data.ratings ?: 0.0) > 4.0)
                        Image(
                            modifier = Modifier.weight(0.1f),
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = stringResource(R.string.des_liked_icon),
                            colorFilter = ColorFilter.tint(color = Color.Red)
                        )
                }
                Text(
                    text = data.author.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = data.publishedDate ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = data.categories.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            }
        }
    }
}