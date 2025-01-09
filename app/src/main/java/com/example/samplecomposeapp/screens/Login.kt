package com.example.samplecomposeapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.samplecomposeapp.R
import com.example.samplecomposeapp.generic_views.GenericTextField

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    Login {}
}

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
    GenericTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = placeholder,
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp),
        isPassword = true
    )
}

@Composable
fun CreateEmailTextField(placeholder: String, text: MutableState<String>) {
    GenericTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = placeholder,
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
