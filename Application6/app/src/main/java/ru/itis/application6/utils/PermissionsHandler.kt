package ru.itis.application6.utils

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PermissionsHandler(
    var onMultiplePermissionGranted: Map<String, () -> Unit> = emptyMap(),
    private val onMultiplePermissionDenied: Map<String, () -> Unit> = emptyMap()
) {

    private var multiplePermissionResult:  ActivityResultLauncher<Array<String>>? = null

    fun initLaunchers(activity: AppCompatActivity) {
        if (multiplePermissionResult == null) {
            multiplePermissionResult = activity.registerForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                callback = { dataMap ->
                    dataMap.forEach { (permission, isGranted) ->
                        if (isGranted) {
                            onMultiplePermissionGranted[permission]?.invoke()
                        } else {
                            onMultiplePermissionDenied[permission]?.invoke()
                        }
                    }
                }
            )
        }
    }

    fun requestMultiplePermission(permissions: List<String>) {
        multiplePermissionResult?.launch(permissions.toTypedArray())
    }
}