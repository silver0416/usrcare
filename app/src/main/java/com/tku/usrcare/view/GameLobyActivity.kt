package com.tku.usrcare.view

import GameLobyPage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.core.content.ContextCompat
import com.tku.usrcare.R

class GameLobyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContent {
            MaterialTheme {
                GameLobyPage()
            }
        }
    }
}