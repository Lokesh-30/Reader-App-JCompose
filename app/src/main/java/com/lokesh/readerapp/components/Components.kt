package com.lokesh.readerapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.lokesh.readerapp.R

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