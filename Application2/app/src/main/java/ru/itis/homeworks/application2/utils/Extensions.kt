package ru.itis.homeworks.application2.utils

import android.content.Context
import android.util.TypedValue

fun getValueInDp(value: Float, ctx: Context): Float {
    //metrics - класс, который предоставляет операционная система андроид
    val metrics = ctx.resources.displayMetrics
    //TypedValue.applyDimension(...) преобразует переданное значение в нужный нам тип данных
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics)
}