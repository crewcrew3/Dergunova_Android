package ru.itis.application5.utils

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PermissionsHandler(
    private val onMultiplePermissionGranted: Map<String, () -> Unit> = emptyMap(),
    private val onMultiplePermissionDenied: Map<String, () -> Unit> = emptyMap()
) {

    private var multiplePermissionResult:  ActivityResultLauncher<Array<String>>? = null

    fun initLaunchers(activity: AppCompatActivity) {
        if (multiplePermissionResult == null) {
            multiplePermissionResult = activity.registerForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                callback = { dataMap ->
                    dataMap.forEach { (permission, isGranted) -> //пара значений - разрешение и Boolean(дано оно или нет)
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