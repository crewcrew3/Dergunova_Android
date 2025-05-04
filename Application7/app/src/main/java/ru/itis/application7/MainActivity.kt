package ru.itis.application7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.itis.application7.core.navigation.ApplicationNavHost
import ru.itis.application7.core.ui.theme.Application7Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Application7Theme {
                ApplicationNavHost()
            }
        }
    }
}