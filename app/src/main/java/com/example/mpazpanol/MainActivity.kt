package com.example.mpazpanol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mpazpanol.ui.navigation.AppNavigation
import com.example.mpazpanol.ui.theme.MPAZPanolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MPAZPanolTheme {
                AppNavigation()
            }
        }
    }
}
