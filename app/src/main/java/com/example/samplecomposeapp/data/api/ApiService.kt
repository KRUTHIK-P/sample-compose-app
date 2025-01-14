package com.example.samplecomposeapp.data.api

import com.example.samplecomposeapp.data.model.Person
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    //With Response<T>, you get the HTTP status code and other metadata,
    // which helps you handle various error states.
    @GET("api/v1/users")
    suspend fun getUsers(): Response<List<Person>>
}