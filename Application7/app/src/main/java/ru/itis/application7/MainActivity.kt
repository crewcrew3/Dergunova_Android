package ru.itis.application7

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.key
import androidx.lifecycle.lifecycleScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.itis.application7.navigation.ApplicationNavHost
import ru.itis.application7.navigation.AuthorizationRoute
import ru.itis.application7.navigation.ListContentRoute
import ru.itis.application7.core.ui.theme.Application7Theme
import ru.itis.application7.core.utils.OtherProperties
import java.util.UUID
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
//            val userId = UUID.randomUUID()
//            Firebase.crashlytics.setCustomKeys {
//                key("userId", "$userId")
//            }

            Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show()
                }
                val flag = Firebase.remoteConfig.getBoolean("flag")
                println("TEST TAG - $flag")
            }

            val nickname = sharedPref.getString(OtherProperties.USER_NICK_SHARED_PREF, null)
            if (nickname != null) {
                startDestination = ListContentRoute
            }
        }

        setContent {
            Application7Theme {
                ApplicationNavHost(startDestination = startDestination)
            }
        }
    }
}