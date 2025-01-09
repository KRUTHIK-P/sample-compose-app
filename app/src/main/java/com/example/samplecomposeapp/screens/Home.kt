package com.example.samplecomposeapp.screens

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.samplecomposeapp.R
import com.example.samplecomposeapp.model.Person
import com.google.gson.Gson

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(itemClick = { }) {}
}

@Composable
fun Home(itemClick: (data: String) -> Unit, logout: () -> Unit) {
    val context = LocalContext.current
    Column {
        Box(
            contentAlignment = Alignment.CenterEnd, modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_logout_24),
                contentDescription = stringResource(id = R.string.log_out),
                modifier = Modifier.clickable {
                    logout()
                }
            )
        }
        LazyColumn(content = {
            items(createContent(context)) {
                ListItem(it, itemClick)
            }
        })
    }
}

fun createContent(context: Context): List<Person> {
    val list = ArrayList<Person>()
    repeat(20) { index ->
        list.add(
            Person(
                name = "${context.getString(R.string.person)} $index",
                department = "${context.getString(R.string.department)} $index",
                designation = "${context.getString(R.string.designation)} $index",
                mobile = "${context.getString(R.string.mobile)} $index"
            )
        )
    }
    return list
}

@Composable
fun ListItem(person: Person, itemClick: (data: String) -> Unit) {
    val data = Gson().toJson(person)
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { itemClick(data) }
    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.round_person_24),
                contentDescription = stringResource(R.string.profile_image)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = person.name)
                Text(text = person.department)
            }
        }
    }
}