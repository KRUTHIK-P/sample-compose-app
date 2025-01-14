package com.example.samplecomposeapp.usecase

import com.example.samplecomposeapp.data.model.Person
import com.example.samplecomposeapp.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getUsers(): Flow<NetworkResult<List<Person>>>
}