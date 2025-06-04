package ru.itis.application7

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.itis.application7.navigation.ApplicationNavHost
import ru.itis.application7.navigation.AuthorizationRoute
import ru.itis.application7.navigation.ListContentRoute
import ru.itis.application7.core.ui.theme.Application7Theme
import ru.itis.application7.core.utils.properties.OtherProperties
import ru.itis.application7.navigation.GraphRoute
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    private var startDestination: Any = AuthorizationRoute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (savedInstanceState == null) {
            handleIntent(intent)
        }

        setContent {
            Application7Theme {
                ApplicationNavHost(startDestination = startDestination)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.getStringExtra(OtherProperties.INTENT_START_DESTINATION_KEY) != null) {
            startDestination = GraphRoute
            intent.removeExtra(OtherProperties.INTENT_START_DESTINATION_KEY)
        } else {
            val nickname = sharedPref.getString(OtherProperties.USER_NICK_SHARED_PREF, null)
            if (nickname != null) {
                startDestination = ListContentRoute
            }
        }
    }
}