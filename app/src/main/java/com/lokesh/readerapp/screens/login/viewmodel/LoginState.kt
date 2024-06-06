package com.lokesh.readerapp.screens.login.viewmodel

sealed class LoginState {
    data object Idle: LoginState()
    data object Loading: LoginState()
    data class Success(val msg: String? = ""): LoginState()
    data class Error(val msg: String?): LoginState()
}