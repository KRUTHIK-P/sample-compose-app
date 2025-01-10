package com.example.samplecomposeapp.screens

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.samplecomposeapp.R
import com.example.samplecomposeapp.model.Person
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.gson.Gson

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(itemClick = { }) {}
}

@Composable
fun Home(itemClick: (data: String) -> Unit, logout: () -> Unit) {
    val db = Firebase.firestore
    addDummyUsersToFireStore(db)
    val users = remember { mutableStateOf<List<Person>>(emptyList()) }

    // Fetch users asynchronously (call inside a coroutine scope)
    LaunchedEffect(Unit) {
        fetchData(db) { users.value = it }
    }
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
            items(users.value) {
                ListItem(it, itemClick)
            }
        })
    }
}

fun fetchData(db: FirebaseFirestore, success: (List<Person>) -> Unit) {
    val list = mutableListOf<Person>()
    db.collection("users").get()
        .addOnSuccessListener { result ->
            result.forEach { document ->
                val person = document.toObject(Person::class.java)
                list.add(person)
            }
            success(list)
        }
        .addOnFailureListener {
            Log.d("firestore", "Error fetching users: $it")
        }
}

fun addDummyUsersToFireStore(db: FirebaseFirestore) {
    val usersRef = db.collection("users")

    usersRef.get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                val departments = listOf("Sales", "Marketing", "Engineering", "HR", "Finance")
                val designations = listOf("Manager", "Executive", "Lead", "Director", "Engineer")

                // Generate and add 20 dummy users
                for (i in 1..20) {
                    val userId =
                        java.util.UUID.randomUUID().toString()  // Generate a random user ID
                    val userName = "User $i"
                    val department = departments.random()  // Random department from the list
                    val designation = designations.random()  // Random designation from the list
                    val mobile = "123456789$i"  // Simulated mobile number

                    // Create a user data map
                    val user = Person(
                        name = userName,
                        department = department,
                        designation = designation,
                        mobile = mobile
                    )

                    // Add the user data to the "users" collection
                    usersRef.document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            println("User $i added successfully")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding user $i: $e")
                        }
                }
            }
        }
        .addOnFailureListener { exception ->
            Log.d("firestore", "Error checking collection: $exception")
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
                Text(text = person.name)
                Text(text = person.department)
            }
        }
    }
}