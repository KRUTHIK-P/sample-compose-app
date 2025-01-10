package com.example.samplecomposeapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Preview(showBackground = true)
@Composable
fun PreviewDetail() {
    Detail(
        person = Person(
            name = "John Doe",
            department = "Engineering",
            designation = "Software Developer",
            mobile = "+1 234 567 890"
        )
    ) {

    }
}

@Composable
fun Detail(person: Person, goBack: () -> Unit) {
    val context = LocalContext.current
    val mobile = remember {
        mutableStateOf(person.mobile)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(1f)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(imageVector = Icons.Outlined.Edit, contentDescription = "update",
                    modifier = Modifier.clickable {
                        val updateFields =
                            if (mobile.value == person.mobile) // interchanging just to have update effect
                                mapOf("mobile" to "1234567890")
                            else
                                mapOf("mobile" to person.mobile)
                        updateUserByName(person.name, updateFields,
                            success = {
                                Toast.makeText(
                                    context,
                                    "User updated successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                mobile.value = updateFields.getValue("mobile")
                            },
                            error = {
                                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT)
                                    .show()
                            })
                    })
                Image(imageVector = Icons.Outlined.Delete, contentDescription = "delete",
                    modifier = Modifier.clickable {
                        deleteUserByName(
                            person.name,
                            success = {
                                Toast.makeText(
                                    context,
                                    "User deleted successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                goBack()
                            },
                            error = {
                                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                    })
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.round_person_24),
                    contentDescription = stringResource(id = R.string.profile_image)
                )
                Text(text = person.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = person.department, style = MaterialTheme.typography.bodyLarge)
                Text(text = person.designation, style = MaterialTheme.typography.bodyLarge)
                Text(text = mobile.value, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

fun deleteUserByName(name: String, success: () -> Unit, error: (Exception) -> Unit) {
    val db = Firebase.firestore
    val userRef = db.collection("users").whereEqualTo("name", name)

    userRef.get()
        .addOnSuccessListener {
            if (it.isEmpty) {
                error(Exception("User not found"))
            } else {
                val userId = it.documents.first().id
                val userDocumentRef = db.collection("users").document(userId)

                userDocumentRef.delete()
                    .addOnSuccessListener {
                        success()
                    }
                    .addOnFailureListener { e ->
                        error(e)
                    }
            }
        }
        .addOnFailureListener { e ->
            error(e)
        }
}

fun updateUserByName(
    name: String,
    updateFields: Map<String, String>,
    success: () -> Unit,
    error: (Exception) -> Unit
) {
    val db = Firebase.firestore
    val userRef = db.collection("users").whereEqualTo("name", name) // Query to get the user by name

    userRef.get()
        .addOnSuccessListener {
            if (it.isEmpty) {
                error(Exception("User Not Found"))
            } else {
                // Get the document ID of the first result (assuming unique name)
                val documentId = it.documents.first().id
                val userDocumentRef = db.collection("users").document(documentId)

                userDocumentRef.update(updateFields)
                    .addOnSuccessListener {
                        success()
                    }
                    .addOnFailureListener { e ->
                        error(e)
                    }
            }
        }
        .addOnFailureListener { e ->
            error(e)
        }
}
