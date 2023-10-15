package com.tku.usrcare.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.tku.usrcare.R
import com.tku.usrcare.view.ui.sports.NavGraphs


class SportsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgSports)
        setContent {
            Sports()
        }
    }
}


@Composable
fun Sports() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}

