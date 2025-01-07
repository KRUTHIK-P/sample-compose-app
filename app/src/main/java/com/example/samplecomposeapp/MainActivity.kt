package com.example.samplecomposeapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.samplecomposeapp.model.Person
import com.example.samplecomposeapp.screens.Detail
import com.example.samplecomposeapp.screens.Home
import com.example.samplecomposeapp.screens.Login
import com.example.samplecomposeapp.utils.Preferences
import com.example.samplecomposeapp.utils.ScreenNames
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Preferences.initSharedPreference(LocalContext.current)
            App()
        }
    }
}

@Composable
fun App() {
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
            Login {
                login(navController, context)
            }
        }
        composable(ScreenNames.HOME) {
            Home(itemClick) {
                logout(navController, context)
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
}

fun login(navController: NavHostController, context: Context) {
    Preferences.savePreference(true)
    navController.navigate(ScreenNames.HOME) {
        popUpTo(ScreenNames.LOGIN) { inclusive = true }
    }
    Toast.makeText(context, context.getString(R.string.log_in), Toast.LENGTH_SHORT).show()
}

fun logout(navController: NavHostController, context: Context) {
    Preferences.savePreference(false)
    navController.navigate(ScreenNames.LOGIN) {
        // This will clear the back stack, so pressing back won't return to home screen
        popUpTo(ScreenNames.HOME) { inclusive = true }
    }
    Toast.makeText(context, context.getString(R.string.log_out), Toast.LENGTH_SHORT).show()
}

fun itemClick(navController: NavHostController, data: String) {
    navController.navigate("detail/$data")
}
