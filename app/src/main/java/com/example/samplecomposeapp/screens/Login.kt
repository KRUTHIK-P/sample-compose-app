package com.example.samplecomposeapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.samplecomposeapp.R

@Composable
fun Login(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(1f)
    ) {
        Column {
            val username = rememberSaveable { mutableStateOf("") }
            CreateEmailTextField(stringResource(R.string.username), username)
            val password = rememberSaveable { mutableStateOf("") }
            CreatePasswordTextField(stringResource(R.string.password), password)
            CreateButton(username.value, password.value, onClick)
        }
    }
}

@Composable
fun CreatePasswordTextField(placeholder: String, text: MutableState<String>) {
    /*
    by is a delegation syntax in Kotlin. It means that passwordVisibility delegates its getter and setter to the State object created by mutableStateOf.
    You don’t have to manually access passwordVisibility.value or passwordVisibility.set(). You can simply use passwordVisibility directly,
    and Compose will take care of the state updates for you.
     */
    var passwordVisibility by remember { mutableStateOf(false) }
    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text(placeholder) },
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                val icon = if (passwordVisibility)
                    R.drawable.baseline_visibility_24
                else
                    R.drawable.baseline_visibility_off_24

                Icon(imageVector = ImageVector.vectorResource(id = icon), contentDescription = stringResource(
                    R.string.toggle_password_visibility
                )
                )
            }
        }
    )
}

@Composable
fun CreateEmailTextField(placeholder: String, text: MutableState<String>) {
    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text(placeholder) },
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
    )
}

@Composable
fun CreateButton(
    username: String,
    password: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Button(onClick = {
        if (username.isNotEmpty() && password.isNotEmpty())
            onClick()
        else
            Toast.makeText(
                context,
                context.getString(R.string.username_and_password_cannot_be_empty),
                Toast.LENGTH_SHORT
            ).show()
    }) {
        Text(text = stringResource(R.string.login))
    }
}
