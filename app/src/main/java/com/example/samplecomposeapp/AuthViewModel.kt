package com.example.samplecomposeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _authState: MutableLiveData<AuthState?> =
        MutableLiveData(null)
    val authState: LiveData<AuthState?> = _authState

    fun getCurrentUser() = auth.currentUser

    fun loginOrSignUp(email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { createTask ->
                    if (createTask.isSuccessful) {
                        // New user has been created
                        val newUser = createTask.result?.user
                        _authState.value = newUser?.let { AuthState.Authenticated(it) }
                            ?: AuthState.Error("Unknown Error")
                    } else if (createTask.exception is FirebaseAuthUserCollisionException) {
                        // email already exists error. so, try sign in.
                        try {
                            // Attempt to sign in first
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        _authState.value =
                                            task.result.user?.let { AuthState.Authenticated(it) }
                                                ?: AuthState.Error("Unknown Error")
                                    } else {
                                        _authState.value = AuthState.Error(
                                            task.exception?.message ?: "Unknown Error"
                                        )
                                    }
                                }
                        } catch (e: Exception) {
                            _authState.value = AuthState.Error(e.message ?: "Error signing in")
                        }
                    } else {
                        _authState.value = AuthState.Error(
                            createTask.exception?.message ?: "Unknown Error"
                        )
                    }
                }
        }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }

    fun setAuthState(authState: AuthState?) {
        _authState.value = authState
    }

}

sealed class AuthState {
    data object UnAuthenticated : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}