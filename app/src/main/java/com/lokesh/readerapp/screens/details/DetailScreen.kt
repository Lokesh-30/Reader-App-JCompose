package com.lokesh.readerapp.screens.details

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lokesh.readerapp.R
import com.lokesh.readerapp.components.ReaderTopBar
import com.lokesh.readerapp.model.Book
import com.lokesh.readerapp.model.Item
import com.lokesh.readerapp.navigation.Screens
import com.lokesh.readerapp.screens.search.viewmodel.SearchViewModel
import com.lokesh.readerapp.utils.Constants
import com.lokesh.readerapp.utils.FirebaseResult
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    navigation: NavHostController,
    args: Screens.DetailScreen,
    viewModel: SearchViewModel
) {
    val bookInfo by viewModel.bookInfo.collectAsState()
    viewModel.bookInfo(args.bookId)
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    Scaffold(
        topBar = {
            ReaderTopBar(navigation = navigation) {
                navigation.navigateUp()
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        DetailContent(
            paddingValues = paddingValues,
            book = bookInfo,
            navigation = navigation,
            snackBarHostState = snackBarHostState
        )
    }
}

@Composable
fun DetailContent(
    paddingValues: PaddingValues,
    book: Item? = null,
    navigation: NavHostController,
    snackBarHostState: SnackbarHostState
) {
    val bookInfo = book?.volumeInfo
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
                .verticalScroll(scrollState),
        ) {
            Image(
                painter = rememberImagePainter(data = bookInfo?.imageLinks?.smallThumbnail),
                contentDescription = stringResource(
                    id = R.string.des_book_poster
                ),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(200.dp)
                    .width(150.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = bookInfo?.title ?: stringResource(id = R.string.unknown),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.authors, bookInfo?.authors.toString()),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Page count: ${bookInfo?.pageCount}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Categories : ${bookInfo?.categories.toString()}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Published : ${bookInfo?.publishedDate}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = bookInfo?.description ?: stringResource(R.string.not_available),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(10.dp),
                    maxLines = 25,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Button(onClick = {
                    val myBook = Book(
                        title = bookInfo?.title,
                        author = bookInfo?.authors?.get(0),
                        imageUrl = bookInfo?.imageLinks?.smallThumbnail,
                        description = bookInfo?.description,
                        publishedDate = bookInfo?.publishedDate,
                        ratings = bookInfo?.averageRating,
                        pageCount = bookInfo?.pageCount.toString(),
                        googleBookId = book?.id,
                        userId = FirebaseAuth.getInstance().currentUser?.uid
                    )
                    saveToFirebase(myBook) { result ->
                        when (result) {
                            FirebaseResult.SUCCESS -> {
                                navigation.navigateUp()
                            }

                            FirebaseResult.ERROR -> {
                                scope.launch {
                                    snackBarHostState.showSnackbar(context.getString(R.string.something_went_wrong))
                                }
                            }
                        }
                    }
                }) {
                    Text(text = stringResource(R.string.btn_save))
                }
                Spacer(modifier = Modifier.width(15.dp))
                Button(onClick = {

                }) {
                    Text(text = stringResource(R.string.btn_cancel))
                }
            }
        }
    }
}

fun saveToFirebase(book: Book, result: (FirebaseResult) -> Unit) {
    val dataBase = FirebaseFirestore.getInstance()
    val dbCollection = dataBase.collection(Constants.Table.SAVED_BOOKS)
    dbCollection.add(book)
        .addOnSuccessListener { ref ->
            val id = ref.id
            dbCollection.document(id).update(
                mapOf("id" to id)
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result(FirebaseResult.SUCCESS)
                }
            }.addOnFailureListener {
                result(FirebaseResult.ERROR)
            }
        }
}