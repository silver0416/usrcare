package com.tku.usrcare.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tku.usrcare.view.ui.theme.UsrcareTheme

class ScaleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UsrcareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaleList()
                }
            }
        }
    }
}

@Composable
fun ScaleList() {
    LazyColumn(content = {
        items(100) {
            ScaleBox(item = it)
        }
    })
}

@Composable
fun ScaleBox(item : Int) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Hello World #${item}")
    }
}


@Preview(showBackground = true)
@Composable
fun ScalePreview() {
    UsrcareTheme {
        ScaleList()
    }
}