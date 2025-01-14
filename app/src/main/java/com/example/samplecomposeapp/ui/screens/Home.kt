package com.example.samplecomposeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.samplecomposeapp.R
import com.example.samplecomposeapp.data.model.Person
import com.example.samplecomposeapp.ui.viewmodel.ViewModel
import com.example.samplecomposeapp.utils.NetworkResult
import com.google.gson.Gson

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(viewModel(), itemClick = { }) {}
}

@Composable
fun Home(viewModel: ViewModel, itemClick: (data: String) -> Unit, logout: () -> Unit) {
    val users = viewModel.users.collectAsState().value
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

        when (users) {
            is NetworkResult.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                }
            }

            is NetworkResult.Success -> {
                LazyColumn(content = {
                    items((users).data) {
                        ListItem(it, itemClick)
                    }
                })
            }

            is NetworkResult.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.BottomCenter)
                        .padding(10.dp, 0.dp)
                ) {
                    Snackbar(containerColor = Color.Red) {
                        BasicText(text = users.message)
                    }
                }
            }
        }
    }
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
                Text(text = "Person ${person.name}")
                Text(text = person.department)
            }
        }
    }
}