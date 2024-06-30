package com.lokesh.readerapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.lokesh.readerapp.network.repo.BookRepository
import com.lokesh.readerapp.network.repo.FirebaseRepository
import com.lokesh.readerapp.network.service.ApiServices
import com.lokesh.readerapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBookApi(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideBookRepository(apiServices: ApiServices): BookRepository {
        return BookRepository(apiServices = apiServices)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.Urls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
    }

    @Singleton
    @Provides
    fun provideFireBaseRepository() = FirebaseRepository(
        queryBook = FirebaseFirestore.getInstance().collection(Constants.Table.SAVED_BOOKS)
    )
}