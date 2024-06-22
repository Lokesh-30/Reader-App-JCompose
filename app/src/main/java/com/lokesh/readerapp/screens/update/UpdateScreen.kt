package com.lokesh.readerapp.screens.update

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.lokesh.readerapp.R
import com.lokesh.readerapp.components.ReaderTopBar
import com.lokesh.readerapp.model.Book
import com.lokesh.readerapp.navigation.Screens
import com.lokesh.readerapp.screens.home.viewmodel.HomeViewModel
import com.lokesh.readerapp.utils.Constants
import com.lokesh.readerapp.utils.Utils
import com.portfolio.ratingbar.RatingBar
import com.portfolio.ratingbar.StepSize

@Composable
fun UpdateScreen(
    navigation: NavHostController = NavHostController(LocalContext.current),
    args: Screens.UpdateScreen = Screens.UpdateScreen(""),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val bookData by viewModel.data
    var comments by rememberSaveable {
        mutableStateOf("")
    }
    var rated = 0.0f
    val context = LocalContext.current
    Scaffold(
        topBar = {
            ReaderTopBar(
                navigation = navigation,
                showNavigation = true,
                title = stringResource(R.string.update_screen),
                onBackClicked = {
                    navigation.navigateUp()
                }
            )
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(3.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bookData.isLoading == true) {
                    LinearProgressIndicator()
                } else {
                    val book = bookData.data?.first {
                        it.googleBookId == args.bookId
                    }
                    var isStartedReading by rememberSaveable {
                        mutableStateOf(false)
                    }
                    var isFinishedReading by rememberSaveable {
                        mutableStateOf(false)
                    }
                    BookDetail(book)
                    OutlinedTextField(
                        value = comments,
                        onValueChange = {
                            comments = it
                        },
                        label = {
                            Text(text = stringResource(R.string.enter_your_thoughts))
                        },
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                    TextButton(
                        onClick = {
                            isStartedReading = !isStartedReading
                        },
                        enabled = book?.startReading == null
                    ) {
                        if (book?.startReading == null) {
                            if (isStartedReading) {
                                Text(
                                    text = stringResource(R.string.started_reading),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            } else Text(
                                text = stringResource(R.string.start_reading),
                                style = MaterialTheme.typography.titleMedium.copy(color = Color.Cyan),
                                modifier = Modifier.wrapContentWidth()
                            )
                        } else {
                            Text(
                                text = "Started On ${Utils.formatDate(book.startReading)}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.wrapContentWidth()
                            )
                        }
                    }
                    TextButton(
                        onClick = {
                            isFinishedReading = !isFinishedReading
                        },
                        enabled = book?.finishedReading == null
                    ) {
                        if (book?.finishedReading == null) {
                            if (isFinishedReading) {
                                Text(
                                    text = stringResource(R.string.finished_reading),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            } else Text(
                                text = stringResource(R.string.mark_as_read),
                                style = MaterialTheme.typography.titleMedium.copy(color = Color.Cyan),
                                modifier = Modifier.wrapContentWidth()
                            )
                        } else Text(
                            text = "Finished on ${Utils.formatDate(book.finishedReading)}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.wrapContentWidth()
                        )
                    }
                    Rating { rating ->
                        rated = rating
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            val notes = book?.notes != comments
                            val rating = book?.ratings != rated.toDouble()
                            val finishedTimeStamp =
                                if (isFinishedReading) Timestamp.now() else book?.finishedReading
                            val startedTimeStamp =
                                if (isStartedReading) Timestamp.now() else book?.startReading

                            val updateBook =
                                notes || rating || isStartedReading || isFinishedReading

                            val newBookData = hashMapOf(
                                "finished_reading" to finishedTimeStamp,
                                "start_reading" to startedTimeStamp,
                                "notes" to comments,
                                "ratings" to rated.toDouble()
                            ).toMap()

                            if (updateBook) {
                                book?.id?.let { bookId ->
                                    FirebaseFirestore.getInstance()
                                        .collection(Constants.Table.SAVED_BOOKS)
                                        .document(bookId)
                                        .update(newBookData)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.updated_successfully),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            }
                        }) {
                            Text(text = stringResource(R.string.update))
                        }
                        var openAlert by rememberSaveable {
                            mutableStateOf(false)
                        }
                        if (openAlert)
                            DeleteAlert(book, navigation) {
                                openAlert = it
                            }
                        Button(
                            onClick = {
                                openAlert = true
                            }
                        ) {
                            Text(text = stringResource(R.string.delete))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteAlert(
    book: Book?,
    navigation: NavHostController,
    onDismiss: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss(false)
                    book?.id?.let { bookId ->
                        FirebaseFirestore.getInstance()
                            .collection(Constants.Table.SAVED_BOOKS)
                            .document(bookId)
                            .delete()
                            .addOnSuccessListener {
                                navigation.navigateUp()
                            }
                    }
                }
            ) {
                Text(text = stringResource(R.string.delete))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(
                    R.string.des_delete_book_icon
                )
            )
        },
        title = {
            Text(
                text = stringResource(R.string.delete_book),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.are_you_sure_you_want_to_delete_this_book),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss(false)
                }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}

@Composable
private fun Rating(rated: (Float) -> Unit) {
    var rating by rememberSaveable {
        mutableFloatStateOf(0.0f)
    }
    Text(
        text = stringResource(R.string.how_do_you_like_the_book_let_us_know),
        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center)
    )
    RatingBar(
        value = rating,
        numOfStars = 5,
        painterEmpty = painterResource(id = R.drawable.ic_star_outline),
        painterFilled = painterResource(id = R.drawable.ic_star_filled),
        stepSize = StepSize.HALF,
        onValueChange = {
            rating = it
        },
        onRatingChanged = {
            rated(it)
        }
    )
}

@Composable
fun BookDetail(
    data: Book? = Book()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = rememberImagePainter(
                    data = data?.imageUrl ?: ""
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
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = data?.title ?: "Unknown",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
                Text(
                    text = data?.author.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = data?.publishedDate ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            }
        }
    }
}