package com.example.samplecomposeapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.UnAuthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        _authState.value = if (currentUser != null){
            AuthState.Authenticated(currentUser)
        }
        else{
            AuthState.UnAuthenticated
        }
    }

    fun loginOrSignUp(email: String, password: String){
        viewModelScope.launch {
            /*try {
                // Attempt to sign in first
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            _authState.value = auth.currentUser?.let { AuthState.Authenticated(it) }
                                ?: AuthState.Error("Unknown Error")
                        }
                        else{
                            // Login failed try to sign up
                            when (task.exception) {
                                is FirebaseAuthInvalidCredentialsException -> {
                                    // Invalid credentials, probably wrong password
                                    _authState.value = AuthState.Error("Invalid credentials, please try again.")
                                }

                                is FirebaseAuthUserCollisionException -> {
                                    // User does not exist, try sign up
                                    createAccount(email, password)
                                }

                                else -> {
                                    _authState.value = AuthState.Error(task.exception?.message ?: "Unknown Error")
                                }
                            }
                        }
                    }
            }
            catch (e: Exception){
                _authState.value = AuthState.Error(e.message ?: "Error signing in")
            }*/
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { createTask ->
                    if (createTask.isSuccessful) {
                        // New user has been created
                        val newUser = createTask.result?.user
                        // Do something with the new user (e.g., update UI, show message)
                        _authState.value = createTask.result?.user?.let { AuthState.Authenticated(it) }
                            ?: AuthState.Error("Unknown Error")
                    } else {
                        // Handle sign-up error (e.g., email already exists)
                        Log.d("exception", createTask.exception?.message ?: "")

                        try {
                            // Attempt to sign in first
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful){
                                        _authState.value = task.result.user?.let { AuthState.Authenticated(it) }
                                            ?: AuthState.Error("Unknown Error")
                                    }
                                    else{
                                        // Login failed try to sign up
                                        when (task.exception) {
                                            is FirebaseAuthInvalidCredentialsException -> {
                                                // Invalid credentials, probably wrong password
                                                _authState.value = AuthState.Error("Invalid credentials, please try again.")
                                            }

                                            is FirebaseAuthUserCollisionException -> {
                                                // User does not exist, try sign up
//                                                createAccount(email, password)
                                            }

                                            else -> {
                                                _authState.value = AuthState.Error(task.exception?.message ?: "Unknown Error")
                                            }
                                        }
                                    }
                                }
                        }
                        catch (e: Exception){
                            _authState.value = AuthState.Error(e.message ?: "Error signing in")
                        }

                    }
                }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = auth.currentUser?.let { AuthState.Authenticated(it) }
                        ?: AuthState.Error("Unknown Error")
                } else {
                    // Sign-up failed, handle error
                    _authState.value = AuthState.Error(task.exception?.message ?: "Unknown Error")
                }
            }
    }

    fun logout(){
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }


}

sealed class AuthState {
    data object UnAuthenticated : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}