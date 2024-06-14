package com.lokesh.readerapp.screens.home.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lokesh.readerapp.model.Book
import com.lokesh.readerapp.network.repo.FirebaseRepository
import com.lokesh.readerapp.utils.DataOrException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    val data = mutableStateOf(DataOrException(listOf<Book>(), false, Exception("")))

    init {
        getAllBooks()
    }

    private fun getAllBooks() {
        viewModelScope.launch {
            data.value.isLoading = true
            data.value = firebaseRepository.getAllBooks()
            data.value.isLoading = false
        }
    }

}