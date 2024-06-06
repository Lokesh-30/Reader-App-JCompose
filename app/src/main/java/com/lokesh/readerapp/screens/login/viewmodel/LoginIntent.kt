package com.lokesh.readerapp.screens.login.viewmodel

sealed class LoginIntent {
    data class Login(val email: String, val passcode: String): LoginIntent()
    data class CreateAccount(val email: String, val passcode: String): LoginIntent()
}