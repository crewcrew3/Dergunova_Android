package ru.itis.application5

object CoroutinesParameters {

    //число корутин, которые будут запущены из текстового поля
    var numberOfCoroutines: Int? = null

    //как будут запущены корутины - последовательно или параллельно из первой радио группы
    var isParallel: Boolean? = null

    //логика работы корутин - отменять при сворачивании приложения, либо продолжать работу, когда приложение свернуто из второй радио группы
    var isContinue: Boolean? = null

    // пул поток, в котором будут исполняться корутины - из выпадающего списка
    var pullThread: String? = null

}