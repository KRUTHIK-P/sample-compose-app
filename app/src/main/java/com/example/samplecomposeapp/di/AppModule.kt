package com.example.samplecomposeapp.di

import com.example.samplecomposeapp.data.api.ApiService
import com.example.samplecomposeapp.data.api.RetrofitInstance
import com.example.samplecomposeapp.data.repository.RepositoryImpl
import com.example.samplecomposeapp.usecase.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
Purpose: The @Module annotation is used to mark a class that will provide
dependencies to be injected by Hilt.

Explanation: A @Module class tells Hilt how to create instances of certain types.
The methods inside the @Module class that are annotated with @Provides or @Binds tell Hilt
how to instantiate or bind those dependencies.
 */

/**
Purpose: The @InstallIn annotation tells Hilt which component (or scope) the Module should
be installed in. This helps define the lifecycle and scope of the dependencies provided by the
module.

Explanation: @InstallIn(SingletonComponent::class) specifies that the dependencies
provided by this module should live for the lifetime of the entire application (singleton scope).
This means that the dependencies will be created once and shared throughout the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Purpose: The @Provides annotation tells Hilt how to create an instance of a type (dependency)
     * that needs to be injected.
     *
     * Explanation: This annotation is used inside a @Module class. Methods annotated
     * with @Provides are responsible for providing instances of classes that are injected by Hilt.
     */

    /**
     * The @Singleton annotation ensures that only one instance of a dependency is
     * created and shared throughout the app. This is commonly used for expensive or global
     * resources like network clients, databases, etc.
     */
    @Provides
    @Singleton
    fun provideApiService(): ApiService = RetrofitInstance.api

    @Provides
    @Singleton
    fun provideRepository(apiService: ApiService): Repository = RepositoryImpl(apiService)
}