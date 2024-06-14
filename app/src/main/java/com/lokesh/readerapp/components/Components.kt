package com.lokesh.readerapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.lokesh.readerapp.R
import com.lokesh.readerapp.model.Book
import com.lokesh.readerapp.navigation.Screens

@Composable
fun ReaderApp(content: @Composable () -> Unit) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 5.dp
    ) {
        content()
    }
}

@Composable
fun GradientBackground(content: @Composable () -> Unit) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(137, 5, 253),
                        Color(216, 5, 253)
                    )
                )
            )
    ) {
        content()
    }
}

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(
        text = "Reader Hub",
        style = MaterialTheme.typography.headlineLarge,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(
    title: String = "Sample Text",
    showNavigation: Boolean = false,
    showProfile: Boolean = false,
    navigation: NavHostController,
    onBackClicked: ()-> Unit = {}
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
        navigationIcon = {
            if (showNavigation) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.des_back_button),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable {
                        onBackClicked()
                    }.scale(1.5f).padding(start = 5.dp)
                )
            }
        },
        actions = {
            if (showProfile) {
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
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeListCardItem(
    data: Book,
    onclick: (String?) -> Unit
) {
    var isLiked by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier.width(150.dp),
        onClick = {
            onclick(data.id)
        },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (
                cover,
                title,
                author,
                like,
                ratings,
                status
            ) = createRefs()
            Image(
                painter = rememberImagePainter(data = data.imageUrl),
                contentDescription = stringResource(
                    R.string.des_book_poster
                ),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .constrainAs(cover) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .height(150.dp)
                    .clickable {
                        isLiked = !isLiked
                    }
            )
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(R.string.des_add_to_wishlist),
                tint = if (isLiked) Color.Red else Color.White,
                modifier = Modifier
                    .constrainAs(like) {
                        end.linkTo(parent.end)
                    }
                    .padding(10.dp)
                    .size(20.dp)
            )
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .constrainAs(ratings) {
                        top.linkTo(like.bottom)
                        end.linkTo(like.end)
                        start.linkTo(like.start)
                    },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(R.string.des_ratings),
                        modifier = Modifier.size(18.dp)
                    )
                    val rating = if (data.ratings == null) "0.0" else data.ratings.toString()
                    Text(
                        text = rating,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                    )
                }
            }
            Text(
                text = "Reading",
                modifier = Modifier
                    .padding(2.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .constrainAs(status) {
                        bottom.linkTo(cover.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            )
            Text(
                text = data.title ?: stringResource(R.string.unknown),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(parent.start)
                        top.linkTo(cover.bottom)
                    }
                    .padding(start = 10.dp, top = 5.dp, end = 10.dp)
            )
            Text(
                text = stringResource(
                    R.string.author_placeholder,
                    data.author ?: stringResource(R.string.unknown)
                ),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(author) {
                        start.linkTo(parent.start)
                        top.linkTo(title.bottom)
                    }
                    .padding(vertical = 5.dp, horizontal = 10.dp)
            )
        }
    }
}

@Composable
fun AccountForm(loading: MutableState<Boolean>, isLogin: Boolean = true, onAction: (String, String) -> Unit) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val passcode = rememberSaveable {
        mutableStateOf("")
    }
    val passcodeVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val passcodeFocusRequest = FocusRequester.Default
    val keyboard = LocalSoftwareKeyboardController.current
    val isValid = remember(email.value, passcode.value) {
        email.value.isNotEmpty() && passcode.value.isNotEmpty()
    }
    Spacer(modifier = Modifier.height(60.dp))
    ReaderLogo()
    OutlinedTextField(
        value = email.value,
        onValueChange = {
            email.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        singleLine = true,
        label = {
            Text(text = stringResource(R.string.email))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        keyboardActions = KeyboardActions {
            passcodeFocusRequest.requestFocus()
        }
    )
    OutlinedTextField(
        value = passcode.value,
        onValueChange = {
            passcode.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(passcodeFocusRequest),
        singleLine = true,
        label = {
            Text(text = stringResource(R.string.password))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        keyboardActions = KeyboardActions { keyboard?.hide() },
        visualTransformation = if (passcodeVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(
                onClick = {
                    passcodeVisibility.value = !passcodeVisibility.value
                }
            ) {
                Icon(
                    painter = painterResource(id = if (passcodeVisibility.value) R.drawable.ic_eye_close else R.drawable.ic_eye_open),
                    contentDescription = stringResource(R.string.dis_passcode_visibility)
                )
            }
        }
    )
    Spacer(modifier = Modifier.height(30.dp))
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        onClick = {
            if (isValid) {
                onAction(email.value, passcode.value)
            }
        }
    ) {
        if (loading.value) CircularProgressIndicator(modifier = Modifier.size(25.dp), color = Color.White)
        else Text(
            text = if (isLogin) stringResource(R.string.login) else stringResource(R.string.create_account),
            style = MaterialTheme.typography.titleLarge
        )
    }
}