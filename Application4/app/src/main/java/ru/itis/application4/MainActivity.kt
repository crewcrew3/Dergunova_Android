package ru.itis.application4

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ru.itis.application4.databinding.ActivityMainBinding
import ru.itis.application4.fragments.MainFragment
import ru.itis.application4.utils.NotificationHandler
import ru.itis.application4.utils.PermissionsHandler

class MainActivity : AppCompatActivity() {

    private var viewBinding: ActivityMainBinding? = null
    var notificationHandler: NotificationHandler? = null
    private val mainContainerId: Int = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {

        intent?.extras?.getString(Properties.EXTRAS_THEME_ID)?.let { themeID ->
            setTheme(getThemeResID(themeID))
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (notificationHandler == null) {
            notificationHandler = NotificationHandler(appContext = this.applicationContext)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(mainContainerId, MainFragment(), Properties.MAIN_FRAGMENT_TAG).commit()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { //если выше 13 андроида
            val permissionHandler = PermissionsHandler()
            permissionHandler.initLaunchers(activity = this)
            val grantedCode = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
            if (grantedCode != PackageManager.PERMISSION_GRANTED) { //если разрешение НЕ было получено
                permissionHandler.requestMultiplePermission(listOf(android.Manifest.permission.POST_NOTIFICATIONS)) //запрашиваем разрешение
                /* Оставлю для примера как бы это выглядело в старой версии
                this.requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)*/
            }
        }

        intent?.extras?.let { extras ->
            extras.getString(Properties.EXTRAS_KEY_LAUNCHED_FROM_NOTIFICATION)?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                //почистим екстрас, чтобы если приложение было запущено из уведы, тост с сообщением не выскакивал каждый раз, когда мы меняем тему
                //Создаем новый бандл без удаляемого элемента
                val newExtras = Bundle(extras)
                newExtras.remove(Properties.EXTRAS_KEY_LAUNCHED_FROM_NOTIFICATION)
                intent.replaceExtras(newExtras) //заменяем бандл
            }
        }
    }

    override fun onDestroy() {
        viewBinding = null
        notificationHandler = null
        super.onDestroy()
    }

    fun changeTheme(themeID: String) {
        // Сохраняем айди выбранной темы в Intent
        intent.apply {
            putExtra(Properties.EXTRAS_THEME_ID, themeID)
        }
        // Пересоздаем активность
        recreate()
    }

    private fun getThemeResID(themeID: String): Int {
        return when (themeID) {
            Properties.THEME_ID_RED -> R.style.RedTheme
            Properties.THEME_ID_GREEN -> R.style.GreenTheme
            Properties.THEME_ID_YELLOW -> R.style.YellowTheme
            else -> R.style.Base_Theme_Application4
        }
    }
}