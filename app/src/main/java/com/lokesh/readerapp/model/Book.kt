package com.lokesh.readerapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Book(
    @Exclude
    var id: String? = null,

    var title: String? = null,

    var author: String? = null,

    var notes: String? = null,

    @get:PropertyName("image_url")
    @set:PropertyName("image_url")
    var imageUrl: String? = null,

    var categories: String? = null,

    @get:PropertyName("published_date")
    @set:PropertyName("published_date")
    var publishedDate: String? = null,

    var ratings: Double? = null,

    var description: String? = null,

    @get:PropertyName("page_count")
    @set:PropertyName("page_count")
    var pageCount: String? = null,

    @get:PropertyName("start_reading")
    @set:PropertyName("start_reading")
    var startReading: Timestamp? = null,

    @get:PropertyName("finished_reading")
    @set:PropertyName("finished_reading")
    var finishedReading: String? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,

    @get:PropertyName("google_book_id")
    @set:PropertyName("google_book_id")
    var googleBookId: String? = null
)
