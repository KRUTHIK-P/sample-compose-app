package com.example.samplecomposeapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
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
