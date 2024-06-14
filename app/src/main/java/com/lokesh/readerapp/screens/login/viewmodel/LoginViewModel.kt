package com.lokesh.readerapp.screens.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.lokesh.readerapp.model.UserProfile
import com.lokesh.readerapp.utils.Constants
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val loginIntent = Channel<LoginIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state = _state.asStateFlow()

    private val auth = Firebase.auth

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            loginIntent.consumeAsFlow().collect {
                when (it) {
                    is LoginIntent.CreateAccount -> createAccount(it.email, it.passcode)
                    is LoginIntent.Login -> login(it.email, it.passcode)
                }
            }
        }
    }

    private fun login(email: String, passcode: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            auth.signInWithEmailAndPassword(email, passcode)
                .addOnFailureListener {
                    _state.value = LoginState.Error(it.message)
                }.addOnCanceledListener {
                    _state.value = LoginState.Error("Cancelled")
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        _state.value = LoginState.Success()
                    }
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = UserProfile(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life sucks!",
            profession = "Developer",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection(Constants.Table.USER).add(user)
    }

    private fun createAccount(email: String, passcode: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            auth.createUserWithEmailAndPassword(email, passcode)
                .addOnFailureListener {
                    _state.value = LoginState.Error(it.message)
                }.addOnCanceledListener {
                    _state.value = LoginState.Error("Cancelled")
                }.addOnCompleteListener {
                    _state.value = LoginState.Success()
                    val displayName = it.result?.user?.email?.split("@")?.get(0)
                    createUser(displayName)
                }
        }
    }


    fun resetState() {
        viewModelScope.launch {
            _state.value = LoginState.Idle
        }
    }
}