package com.lokesh.readerapp.network.service

import com.lokesh.readerapp.model.BookResponse
import com.lokesh.readerapp.model.Item
import com.lokesh.readerapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ApiServices {

    @GET(Constants.Endpoints.GET_BOOK)
    suspend fun getBooks(@Query("q") query: String): BookResponse

    @GET(Constants.Endpoints.GET_BOOK_INFO)
    suspend fun getBookInfo(@Path("bookId") bookId: String): Item

}