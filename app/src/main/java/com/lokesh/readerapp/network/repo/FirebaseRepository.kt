package com.lokesh.readerapp.network.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.lokesh.readerapp.model.Book
import com.lokesh.readerapp.utils.DataOrException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val queryBook: Query
) {
    suspend fun getAllBooks(): DataOrException<List<Book>, Boolean, Exception> {
        val dataOrException = DataOrException<List<Book>, Boolean, Exception>()

        try {
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(Book::class.java) ?: Book()
            }.filter {
                it.userId == FirebaseAuth.getInstance().currentUser?.uid
            }
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
        }
        return dataOrException
    }
}