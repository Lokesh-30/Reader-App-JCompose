package com.lokesh.readerapp.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.lokesh.readerapp.R
import com.lokesh.readerapp.components.GradientBackground
import com.lokesh.readerapp.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    GradientBackground {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val scale = remember {
                Animatable(0f)
            }
            LaunchedEffect(key1 = true) {
                scale.animateTo(0.9f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = {
                            OvershootInterpolator(8f).getInterpolation(it)
                        }
                    )
                )
                delay(2000L)
                if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
                    navController.navigate(Screens.LoginScreen)
                else navController.navigate(Screens.HomeScreen)
            }
            Image(
                painter = painterResource(id = R.drawable.ic_splash_logo),
                contentDescription = stringResource(
                    R.string.dis_app_icon
                ),
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale.value)
            )
        }
    }
}