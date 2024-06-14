package com.lokesh.readerapp.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.lokesh.readerapp.R
import com.lokesh.readerapp.components.ReaderTopBar
import com.lokesh.readerapp.model.Item
import com.lokesh.readerapp.navigation.Screens
import com.lokesh.readerapp.screens.search.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    navigation: NavHostController,
    viewModel: SearchViewModel
) {
    Scaffold(
        topBar = {
            ReaderTopBar(
                navigation = navigation,
                showNavigation = true,
                title = stringResource(R.string.search_screen)
            ) {
                navigation.navigateUp()
            }
        }
    ) { paddingValues ->
        SearchContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
            navigation = navigation
        )
    }
}

@Composable
fun SearchContent(
    paddingValues: PaddingValues,
    viewModel: SearchViewModel,
    navigation: NavHostController
) {
    val list = viewModel.bookList
    Surface(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            SearchBar {
                viewModel.searchQuery(it)
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(top = 5.dp, bottom = 15.dp)
            ) {
                items(
                    items = list,
                    key = { it.id ?: "" }
                ) { item ->
                    SearchListItem(data = item) { bookId ->
                        navigation.navigate(Screens.DetailScreen(bookId))
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchListItem(
    data: Item,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(data.id.toString())
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
                    data = data.volumeInfo?.imageLinks?.smallThumbnail ?: ""
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
                Text(
                    text = data.volumeInfo?.title ?: "Unknown",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
                Text(
                    text = data.volumeInfo?.authors.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = data.volumeInfo?.publishedDate ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = data.volumeInfo?.categories.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    onSearch: (String) -> Unit
) {
    var query by rememberSaveable {
        mutableStateOf("")
    }
    /**
     * val keyboard = LocalSoftwareKeyboardController.current
     * val isValid = rememberSaveable(query) {
     *      query.trim().isNotEmpty()
     * }
     * val context = LocalContext.current
     */
    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            onSearch(it)
        },
        label = {
            Text(text = stringResource(R.string.search))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.des_search_bar)
            )
        },
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        /*
        keyboardActions = KeyboardActions(
            onSearch = {
                if (isValid) {
                    onSearch(query)
                    keyboard?.hide()
                } else Toast.makeText(
                    context,
                    context.getString(R.string.enter_valid_text), Toast.LENGTH_SHORT
                ).show()
            }
        ),
        */
        modifier = Modifier
            .fillMaxWidth()
    )
}