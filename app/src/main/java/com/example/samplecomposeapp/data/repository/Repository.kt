package com.example.samplecomposeapp.data.repository

import com.example.samplecomposeapp.data.api.ApiService
import com.example.samplecomposeapp.data.model.Person
import com.example.samplecomposeapp.usecase.Repository
import com.example.samplecomposeapp.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*@Inject constructor() tells 2 things to hilt,
1.needs dependencies(params) to be injected, if required.
2.how repository object can be provided.
 */
class RepositoryImpl @Inject constructor(private val apiService: ApiService) : Repository {

    override suspend fun getUsers(): Flow<NetworkResult<List<Person>>> = flow {
        try {
            emit(NetworkResult.Loading)

            val response = apiService.getUsers()

            if (response.isSuccessful) {
                response.body()?.let {
                    emit(NetworkResult.Success(it))
                } ?: emit(NetworkResult.Error("Empty response body"))
            } else {
                emit(NetworkResult.Error("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error("Error fetching users: ${e.localizedMessage}"))
        }
    }
}