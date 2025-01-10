package com.example.samplecomposeapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.samplecomposeapp.generic_views.NavGraph
import com.example.samplecomposeapp.utils.Preferences
import com.example.samplecomposeapp.utils.ScreenNames

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Preferences.initSharedPreference(LocalContext.current)
            // Set up the Navigation
            val navController = rememberNavController()

            // Provide the NavGraph to manage app's navigation
            NavGraph(navController = navController)
            val authViewModel: AuthViewModel by viewModels()
            App(authViewModel)
        }
    }
}

@Composable
fun App(authViewModel: AuthViewModel) {
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    // Create the NavController
    val navController = rememberNavController()

    var startDestination = ScreenNames.LOGIN
    if (Preferences.isLoggedIn())
        startDestination = ScreenNames.HOME

    val itemClick = { data: String -> itemClick(navController, data) }

    // Set up the NavHost and navigation graph
    NavHost(navController = navController, startDestination = startDestination) {
        composable(ScreenNames.LOGIN) {
            Login { username, password ->
                login(navController, context, authViewModel, username, password)
            }
        }
        composable(ScreenNames.HOME) {
            Home(itemClick) {
                logout(navController, context, authViewModel)
            }
        }
        composable("${ScreenNames.DETAIL}/{data}", arguments = listOf(
            navArgument("data") { type = NavType.StringType }
        )) {
            val data = it.arguments?.getString("data")
            val person = Gson().fromJson(data, Person::class.java)
            Detail(person)
        }
    }

    when (authState) {
        is AuthState.UnAuthenticated -> {
            navController.navigate(ScreenNames.LOGIN) {
                // This will clear the back stack, so pressing back won't return to home screen
                popUpTo(ScreenNames.HOME) { inclusive = true }
            }
        }
        is AuthState.Authenticated -> {
            val user = (authState as AuthState.Authenticated).user
            navController.navigate(ScreenNames.HOME) {
                popUpTo(ScreenNames.LOGIN) { inclusive = true }
            }
            Toast.makeText(context, "Welcome ${user.displayName ?: user.email}", Toast.LENGTH_SHORT).show()
        }
        is AuthState.Error -> {
            val errorMessage = (authState as AuthState.Error).message
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        }
    }
}

fun login(
    navController: NavHostController,
    context: Context,
    authViewModel: AuthViewModel,
    username: String,
    password: String
) {
//    Preferences.savePreference(true)
    authViewModel.loginOrSignUp(username, password)
}

fun logout(navController: NavHostController, context: Context, authViewModel: AuthViewModel) {
//    Preferences.savePreference(false)
    authViewModel.logout()
    Toast.makeText(context, context.getString(R.string.log_out), Toast.LENGTH_SHORT).show()
}

fun itemClick(navController: NavHostController, data: String) {
    navController.navigate("detail/$data")
}
