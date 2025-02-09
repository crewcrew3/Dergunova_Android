package ru.itis.application5.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineDispatcher

object CoroutinesParameters {

    //число корутин, которые будут запущены из текстового поля
    var numberOfCoroutines: Int? by mutableStateOf(null)

    //как будут запущены корутины - последовательно или параллельно из первой радио группы
    var isParallel: Boolean? by mutableStateOf(null)

    //логика работы корутин - отменять при сворачивании приложения, либо продолжать работу, когда приложение свернуто из второй радио группы
    var isContinue: Boolean? by mutableStateOf(null)

    // пул поток, в котором будут исполняться корутины - из выпадающего списка
    var pullThread:  CoroutineDispatcher? by mutableStateOf(null)

}