package com.example.samplecomposeapp.ui.generic_views

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.samplecomposeapp.R

@Composable
fun GenericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    /*
    by is a delegation syntax in Kotlin. It means that passwordVisibility delegates its getter and setter to the State object created by mutableStateOf.
    You don’t have to manually access passwordVisibility.value or passwordVisibility.set(). You can simply use passwordVisibility directly,
    and Compose will take care of the state updates for you.
    */
    var passwordVisibility by remember { mutableStateOf(false) }

    val visualTransformation = if (isPassword && !passwordVisibility) {
        PasswordVisualTransformation()  // Mask the password by default
    } else {
        VisualTransformation.None  // Show the password if visibility is true
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    val icon = if (passwordVisibility)
                        R.drawable.baseline_visibility_24
                    else
                        R.drawable.baseline_visibility_off_24

                    Icon(
                        imageVector = ImageVector.vectorResource(id = icon),
                        contentDescription = stringResource(
                            R.string.toggle_password_visibility
                        )
                    )
                }
            } else {
                trailingIcon?.invoke()
            }
        },
        visualTransformation = visualTransformation,
        modifier = modifier
    )
}