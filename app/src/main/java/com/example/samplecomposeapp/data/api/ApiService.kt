package com.example.samplecomposeapp.data.api

import com.example.samplecomposeapp.data.model.Person
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/users")
    suspend fun getUsers(): List<Person>
}