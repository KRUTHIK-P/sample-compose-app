package com.example.samplecomposeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.samplecomposeapp.data.model.Person
import com.example.samplecomposeapp.itemClick
import com.example.samplecomposeapp.login
import com.example.samplecomposeapp.logout
import com.example.samplecomposeapp.ui.screens.Detail
import com.example.samplecomposeapp.ui.screens.Home
import com.example.samplecomposeapp.ui.screens.Login
import com.example.samplecomposeapp.ui.viewmodel.ViewModel
import com.example.samplecomposeapp.utils.Preferences
import com.example.samplecomposeapp.utils.ScreenNames
import com.google.gson.Gson

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: ViewModel
) {
    val context = LocalContext.current

    var startDestination = ScreenNames.LOGIN
    if (Preferences.isLoggedIn())
        startDestination = ScreenNames.HOME

    // Set up the NavHost and navigation graph
    NavHost(navController = navController, startDestination = startDestination) {
        composable(ScreenNames.LOGIN) {
            Login {
                login(navController, context)
            }
        }
        composable(ScreenNames.HOME) {
            Home(viewModel, { data: String -> itemClick(navController, data) }) {
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