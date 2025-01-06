package com.example.samplecomposeapp.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.samplecomposeapp.model.Person

@Composable
fun Detail(person: Person) {
    Toast.makeText(LocalContext.current, person.name, Toast.LENGTH_SHORT).show()
}