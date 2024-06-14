package com.lokesh.readerapp.network.repo

import android.util.Log
import com.lokesh.readerapp.model.Item
import com.lokesh.readerapp.network.service.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val apiServices: ApiServices
) {
    suspend fun getBooks(query: String) = flow {
        emit(apiServices.getBooks(query = query).items)
    }.flowOn(Dispatchers.IO)

    suspend fun getBookInfo(bookId: String) = flow {
        emit(apiServices.getBookInfo(bookId))
    }.flowOn(Dispatchers.IO)
}