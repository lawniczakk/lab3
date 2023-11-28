@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.firebase

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.firebase.ui.theme.FirebaseTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseTheme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(){

    fun data(): String {
        val currentDataTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return currentDataTime.format(formatter)
    }

    val database = Firebase.database
    val myRef = database.getReference(data())
    var wiadomosc:String? by remember {
        mutableStateOf("")
    }
    myRef.addValueEventListener(object: ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.getValue<String>()
            wiadomosc= value
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to read value.", error.toException())
        }

    })



    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(20.dp)) {
        TextField(
            value = text,
            onValueChange = { navtext -> text = navtext },
            label = { Text(text = "Podaj swoje imie")}

        )
        Button(

            onClick = { myRef.child(data()).setValue(text) },
            modifier = Modifier.padding(20.dp)
        )
        {
            Text(text = "Zapisz")
        }
        Spacer(modifier = Modifier.padding(30.dp))
        Text(text = "$wiadomosc")

    }


}