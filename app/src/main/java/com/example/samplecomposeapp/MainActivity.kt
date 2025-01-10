package com.example.samplecomposeapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.samplecomposeapp.generic_views.NavGraph
import com.example.samplecomposeapp.utils.Preferences
import com.example.samplecomposeapp.utils.ScreenNames

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Preferences.initSharedPreference(LocalContext.current)
            // Set up the Navigation
            val navController = rememberNavController()

            val authState by authViewModel.authState.observeAsState()
            val context = LocalContext.current

            // Extract error message when authState is an error
            val errorMessage = (authState as? AuthState.Error)?.message

            // Pass the errorMessage to NavGraph (only changes to errorMessage will trigger recomposition)
            NavGraph(navController = navController, authViewModel, errorMessage = errorMessage)

            // Handle navigation logic based on authState
            LaunchedEffect(authState) {
                when (val state = authState) {
                    is AuthState.UnAuthenticated -> {
                        navController.navigate(ScreenNames.LOGIN) {
                            popUpTo(ScreenNames.HOME) { inclusive = true }
                        }
                        authViewModel.setAuthState(null)
                    }

                    is AuthState.Authenticated -> {
                        val user = state.user
                        navController.navigate(ScreenNames.HOME) {
                            popUpTo(ScreenNames.LOGIN) { inclusive = true }
                        }
                        Toast.makeText(
                            context,
                            "Welcome ${user.displayName ?: user.email}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}

fun login(
    authViewModel: AuthViewModel,
    username: String,
    password: String
) {
    authViewModel.loginOrSignUp(username, password)
}

fun logout(context: Context, authViewModel: AuthViewModel) {
    authViewModel.logout()
    Toast.makeText(context, context.getString(R.string.log_out), Toast.LENGTH_SHORT).show()
}

fun itemClick(navController: NavHostController, data: String) {
    navController.navigate("detail/$data")
}
