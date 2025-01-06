package com.example.samplecomposeapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun Login(navController: NavHostController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(1f)
        ) {
        Column {
            val username = remember { mutableStateOf("") }
            CreateTextField("Username", username)
            val password = remember { mutableStateOf("") }
            CreateTextField("Password", password)
            CreateButton(navController, username.value, password.value)
        }
    }
}

@Composable
fun CreateButton(navController: NavHostController, username: String, password: String) {
    val context = LocalContext.current
    Button(onClick = {
        if (username.isNotEmpty() && password.isNotEmpty())
            navController.navigate("home")
        else Toast.makeText(context, "username and password cannot be empty", Toast.LENGTH_SHORT).show()
    }) {
        Text(text = "Login")
    }
}

@Composable
fun CreateTextField(placeholder: String, text: MutableState<String>) {
    // Remember the state of the text field

    // TextField composable
    TextField(
        value = text.value,
        onValueChange = { text.value = it},
        label = { Text(placeholder) },
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
    )
}
