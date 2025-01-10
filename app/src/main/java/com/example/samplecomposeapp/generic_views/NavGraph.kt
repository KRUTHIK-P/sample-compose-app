package com.example.samplecomposeapp.generic_views

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.samplecomposeapp.AuthViewModel
import com.example.samplecomposeapp.itemClick
import com.example.samplecomposeapp.login
import com.example.samplecomposeapp.logout
import com.example.samplecomposeapp.model.Person
import com.example.samplecomposeapp.screens.Detail
import com.example.samplecomposeapp.screens.Home
import com.example.samplecomposeapp.screens.Login
import com.example.samplecomposeapp.utils.ScreenNames
import com.google.gson.Gson

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    errorMessage: String?
) {
    /*
    In this case, you are passing navController and errorMessage to NavGraph,
    but navController is remembered, and only errorMessage (which is a parameter) will affect recomposition.
    If errorMessage changes, only the part of the UI where Login depends on errorMessage
    will be recomposed (i.e., Login), not the entire NavHost.
     */
    val context = LocalContext.current
    val startDestination =
        if (authViewModel.getCurrentUser() != null)
            ScreenNames.HOME
        else
            ScreenNames.LOGIN

    // Set up the NavHost and navigation graph
    //unless params(navController, startDestination) passed to NavHost changes, NavHost won't recompose.
    NavHost(navController = navController, startDestination = startDestination) {
        composable(ScreenNames.LOGIN) {
            Login(errorMessage) { username, password ->
                login(authViewModel, username, password)
            }
        }
        composable(ScreenNames.HOME) {
            Home({ data: String -> itemClick(navController, data) }) {
                logout(context, authViewModel)
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