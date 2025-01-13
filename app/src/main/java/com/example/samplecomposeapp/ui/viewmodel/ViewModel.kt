package com.example.samplecomposeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplecomposeapp.data.model.Person
import com.example.samplecomposeapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _users = MutableStateFlow<List<Person>>(emptyList())
    val users: StateFlow<List<Person>> = _users

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            repository.getUsers()
                .flowOn(Dispatchers.IO)
                .collect {
                    _users.value = it
                }
        }
    }
}