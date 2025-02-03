package ru.itis.application4.utils

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PermissionsHandler(
    private val onMultiplePermissionGranted: Map<String, () -> Unit> = emptyMap(),
    private val onMultiplePermissionDenied: Map<String, () -> Unit> = emptyMap()
) {

    private var activity: AppCompatActivity? = null
    private var multiplePermissionResult:  ActivityResultLauncher<Array<String>>? = null

    fun initLaunchers(activity: AppCompatActivity) {
        if (this.activity == null) {
            this.activity = activity
        }

        if (multiplePermissionResult == null) {
            multiplePermissionResult = this.activity?.registerForActivityResult(
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
        multiplePermissionResult?.launch(permissions.toTypedArray()) //преобразует список в массив с соответствующим типом данных
    }
}

/*
    Объяснение происходящего для себя (писалось на момент существования singlePermission, теперь его нет в этом классе. Но менять мне под multiple лень, все равно там одна и таже логика):
        Мы заранее создаем обработчик результата, который будет вызван когда пользователь уже ответит на разрешение.

        ActivityResultContract<I, O> - Контракт, определяющий, что действие может быть вызвано с использованием входных данных типа I и выдавать выходные данные типа O.
        В случае с ActivityResultContracts.RequestPermission() (который наследуется от ActivityResultContract<String, Boolean>()) - входные данные типа String это пермишон, который мы получили в методе requestSinglePermission(...),
        а выходные данные Boolean - дал пользователь разрешение или нет.

        С помощью метода registerForActivityResult(..) мы регистрируем запрос чтобы начать действие для результата, указанного в контракте(т.е. для получения значения типа Boolean, которое характеризует, дал ли пользователь разрешение или нет?? Это если рассматривать именно случай с контрактом RequestPermission()).
        Помимо разрешений можно также регистрировать запросы на получение списка контактов, файлов и т.д. Тип запроса зависит от передаваемого контракта, следовательно и тип результата, который мы получим, также зависит от контракта.

        Кроме контракта мы передаем в метод registerForActivityResult(...) callback - функция, которая будет вызвана в главном потоке, когда будет доступен результат действия(activity result, в нашем случае с isGranted: Boolean).
        callback имеет ActivityResultCallback<O> - т.е. он работает уже с выходными данными, в нашем случае с isGranted: Boolean

        Сама переменная singlePermissionResult имеет тип данных ActivityResultLauncher<String>, где String - тип входных данных контракта.
        ActivityResultLauncher представляет из себя средство, которое можно использовать для запуска действия (метод launch()) или удаления подготовленного вызова.
    */