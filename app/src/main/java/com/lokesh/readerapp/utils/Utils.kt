package com.lokesh.readerapp.utils

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.lokesh.readerapp.model.Book
import java.text.DateFormat

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

    fun formatDate(timestamp: Timestamp?): String {
        return timestamp?.toDate()?.let {
            DateFormat.getDateInstance()
                .format(it).toString()
        } ?: ""
    }

    fun getName() = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: "Unknown"
}