package com.example.samplecomposeapp.usecase

import com.example.samplecomposeapp.data.model.Person
import com.example.samplecomposeapp.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun execute(): Flow<NetworkResult<List<Person>>> = repository.getUsers()
}