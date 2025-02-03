package ru.itis.application5

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.itis.application5.compose.ComposeFragment
import ru.itis.application5.databinding.ActivityMainBinding
import ru.itis.application5.utils.PermissionsHandler
import ru.itis.application5.utils.Properties

class MainActivity : AppCompatActivity() {

    private var viewBinding: ActivityMainBinding? = null
    private val mainContainerId: Int = R.id.container
    private var openAppSettingsResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(mainContainerId, ComposeFragment(), Properties.COMPOSE_FRAGMENT_TAG).commit()
        }

        openAppSettingsResultLauncher = registerForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            callback = {}
        )

        requestNotificationPermission()
    }

    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            val permissionHandler = PermissionsHandler()
            permissionHandler.initLaunchers(activity = this)

            val grantedCode = ContextCompat.checkSelfPermission(this, permission)
            if (grantedCode != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.alert_dialog_title)
                        .setMessage(R.string.alert_dialog_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.alert_dialog_positive_btn_text) { dialog, _ ->
                            dialog.dismiss()
                            openAppSettings()
                        }
                        .create()
                        .show()
                } else {
                    permissionHandler.requestMultiplePermission(listOf(permission))
                }
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            /* Uri.parse("package:${packageName}") создает URI, который ссылается на пакет текущего приложения.
            packageName — это встроенная переменная, которая содержит имя пакета приложения.
            Таким образом, мы указываем системе, что хотим открыть настройки именно для этого приложения. */
            data = Uri.parse("package:${packageName}")
        }
        openAppSettingsResultLauncher?.launch(intent)
    }
}