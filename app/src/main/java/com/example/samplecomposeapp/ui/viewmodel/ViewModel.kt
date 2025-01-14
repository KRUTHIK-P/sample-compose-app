package com.example.samplecomposeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplecomposeapp.data.model.Person
import com.example.samplecomposeapp.usecase.UseCase
import com.example.samplecomposeapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel: This annotation tells Hilt to inject the dependencies into the ViewModel
// automatically.
@HiltViewModel
class ViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private val _users = MutableStateFlow<NetworkResult<List<Person>>>(NetworkResult.Loading)
    val users: StateFlow<NetworkResult<List<Person>>> = _users

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            useCase.execute()
                .flowOn(Dispatchers.IO)
                .collect {
                    _users.value = it
                }
        }
    }
}