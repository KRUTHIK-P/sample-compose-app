package com.example.samplecomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.samplecomposeapp.model.Person
import com.example.samplecomposeapp.screens.Detail
import com.example.samplecomposeapp.screens.Home
import com.example.samplecomposeapp.screens.Login
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    // Create the NavController
    val navController = rememberNavController()

    // Set up the NavHost and navigation graph
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { Login(navController) }
        composable("home") { Home(navController) }
        composable("detail/{data}", arguments = listOf(
            navArgument("data") { type = NavType.StringType }
        )) {
            val data = it.arguments?.getString("data")
            val person = Gson().fromJson(data, Person::class.java)
            Detail(person)
        }
    }
}