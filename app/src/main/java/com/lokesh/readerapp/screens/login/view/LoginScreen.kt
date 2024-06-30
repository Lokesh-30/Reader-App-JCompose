package com.lokesh.readerapp.screens.login.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lokesh.readerapp.R
import com.lokesh.readerapp.components.AccountForm
import com.lokesh.readerapp.components.ReaderApp
import com.lokesh.readerapp.navigation.Screens
import com.lokesh.readerapp.screens.login.viewmodel.LoginIntent
import com.lokesh.readerapp.screens.login.viewmodel.LoginState
import com.lokesh.readerapp.screens.login.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navigation: NavHostController, viewModel: LoginViewModel) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val loading = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = state) {
        when (state) {
            is LoginState.Success -> {
                loading.value = false
                navigation.navigate(Screens.HomeScreen)
                viewModel.resetState()
            }

            is LoginState.Error -> {
                Toast.makeText(context, (state as LoginState.Error).msg, Toast.LENGTH_SHORT).show()
                loading.value = false
            }

            LoginState.Loading -> {
                loading.value = true
            }

            else -> {
                loading.value = false
            }
        }
    }

    ReaderApp {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isLogin = rememberSaveable {
                mutableStateOf(true)
            }
            if (isLogin.value)
                AccountForm(loading = loading, true) { email, passcode ->
                    viewModel.loginIntent.trySend(LoginIntent.Login(email, passcode))
                }
            else AccountForm(loading = loading, false) { email, passcode ->
                viewModel.loginIntent.trySend(LoginIntent.CreateAccount(email, passcode))
            }

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isLogin.value) {
                    Text(
                        text = stringResource(R.string.new_user),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        modifier = Modifier
                            .clickable {
                                isLogin.value = !isLogin.value
                            },
                        text = stringResource(R.string.sign_up),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Blue,
                            fontWeight = FontWeight.Bold
                        )
                    )
                } else Text(
                    modifier = Modifier
                        .clickable {
                            isLogin.value = !isLogin.value
                        },
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
