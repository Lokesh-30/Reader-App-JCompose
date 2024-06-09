package com.lokesh.readerapp.screens

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