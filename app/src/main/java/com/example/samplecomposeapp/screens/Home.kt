package com.example.samplecomposeapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.samplecomposeapp.R
import com.example.samplecomposeapp.model.Person
import com.google.gson.Gson

@Composable
fun Home(navController: NavHostController) {
    Column {
        Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.padding(10.dp).fillMaxWidth()) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_logout_24),
                contentDescription = "logout"
            )
        }
        LazyColumn(content = {
            items(createContent()) {
                ListItem(it, navController)
            }
        })
    }
}

fun createContent(): List<Person> {
    val list = ArrayList<Person>()
    repeat(10) { index ->
        list.add(
            Person(
                name = "Person $index",
                department = "Department $index",
                designation = "Designation $index",
                mobile = "Mobile $index"
            )
        )
    }
    return list
}

@Composable
fun ListItem(person: Person, navController: NavHostController) {
    val data = Gson().toJson(person)
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { navController.navigate("detail/$data") }
    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.round_person_24),
                contentDescription = "profile"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = person.name)
                Text(text = person.department)
            }
        }
    }
}