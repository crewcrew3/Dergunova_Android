package ru.itis.application6.activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.itis.application6.App
import ru.itis.application6.utils.Properties
import ru.itis.application6.R
import ru.itis.application6.databinding.ActivityMainBinding
import ru.itis.application6.fragments.AuthorizationFragment
import ru.itis.application6.fragments.MainFragment
import ru.itis.application6.utils.PermissionsHandler

class MainActivity : AppCompatActivity() {

    private var viewBinding: ActivityMainBinding? = null
    private val containerID: Int = R.id.container
    var sharedPref: SharedPreferences? = null
    var permissionHandler: PermissionsHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            sharedPref = (application as? App)?.getSharedPreferences()

            /*регистрируем лаунчеры тут, потому что на этапе onViewCreated() в AddContentFragment было уже поздно,
                тк он выдавал ошибку, что активность находится в состоянии resumed(), а регистрировать лаунчеры надо перед started()
             */
            permissionHandler = PermissionsHandler()
            permissionHandler?.initLaunchers(activity = this)

            //получаем ник юзера из префов(если он есть)
            val nickname = sharedPref?.getString(Properties.USER_NICK_SHARED_PREF, null)
            if (nickname != null) {
                supportFragmentManager.beginTransaction()
                    .add(containerID, MainFragment(), Properties.MAIN_FRAGMENT_TAG)
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(containerID, AuthorizationFragment(), Properties.AUTHORIZATION_FRAGMENT_TAG)
                    .commit()
            }
        }
    }

    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }
}