package com.lokesh.readerapp.utils

import com.lokesh.readerapp.model.Book

object Utils {
    val dummyList = (1..10).map {
        Book(
            "${it}",
            "Book ${it}",
            "Auth ${it}",
            "This is ${it}",
            ""
        )
    }
}