package com.lokesh.readerapp.utils

object Constants {
    object Urls {
        const val BASE_URL = "https://www.googleapis.com/books/v1/"
    }

    object Endpoints {
        const val GET_BOOK = "volumes"
        const val GET_BOOK_INFO = "volumes/{bookId}"
    }

    object Table {
        const val SAVED_BOOKS = "saved_books"
        const val USER = "users"
    }
}