package com.lokesh.readerapp.screens.search.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lokesh.readerapp.model.Item
import com.lokesh.readerapp.network.repo.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    var bookList by mutableStateOf(listOf<Item>())

    private var _bookInfo = MutableStateFlow<Item?>(null)
    val bookInfo get() = _bookInfo.asStateFlow()

    private var queryState = MutableStateFlow("andr")

    init {
        queryState.debounce(500).onEach {
            if (it.isNotEmpty()) {
                bookRepository.getBooks(it).collect { list ->
                    if (list != null) {
                        bookList = list
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun searchQuery(query: String) {
        queryState.value = query
    }

    fun bookInfo(bookId: String) {
        viewModelScope.launch {
            bookRepository.getBookInfo(bookId).collect { info ->
                _bookInfo.value = info
            }
        }
    }
}