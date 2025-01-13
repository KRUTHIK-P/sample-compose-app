package com.example.samplecomposeapp.data.repository

import com.example.samplecomposeapp.data.api.ApiService
import com.example.samplecomposeapp.data.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository(private val apiService: ApiService) {

    suspend fun getUsers(): Flow<List<Person>> = flow {
        try {
            val users = apiService.getUsers()
            emit(users)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
}