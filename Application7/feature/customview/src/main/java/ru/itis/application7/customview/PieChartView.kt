package ru.itis.application7.customview

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import ru.itis.application7.core.utils.properties.OtherProperties
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var _pieData: Map<Int, Float> = emptyMap()
    var pieData: Map<Int, Float>
        get() = _pieData
        set(value) {
            require(value.values.sum() == 100f) { OtherProperties.PIE_CHART_EX_MSG }
            _pieData = value
            invalidate()
        }
    private var pieColors: IntArray = intArrayOf()
    private val gapSize: Float //промежутки между секторами
    private val activeColorOffset: Int //значение, на которое увеличивается яркость цвета активного сектора
    private var activeItem: Int? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG) //Флаг для сглаживания границ при рисовании, чтобы они не были пиксельными
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }
    private val rectF = RectF() //Прямоугольник, в который вписывается круг.
    private val centerRadius: Float  // Радиус центрального отверстия

    private val clipPath = Path()
    private var pathDirty = true // Флаг для пересоздания path при изменении размеров

    init {
        context.theme.obtainStyledAttributes( //Этот метод читает атрибуты View из XML.
            attrs,
            R.styleable.PieChartView,
            defStyleAttr,
            0
        ).apply {
            try {
                //данные
                val keysArray = resources.getIntArray(getResourceId(R.styleable.PieChartView_pieDataKeys, 0))
                val valuesTypedArray = resources.obtainTypedArray(getResourceId(R.styleable.PieChartView_pieDataValues, 0))
                val valuesArray = FloatArray(valuesTypedArray.length()).apply {
                    for (i in 0 until valuesTypedArray.length()) {
                        this[i] = valuesTypedArray.getFloat(i, 0f)
                    }
                }
                valuesTypedArray.recycle()
                pieData = keysArray.indices.associate {
                    keysArray[it] to valuesArray[it]
                }

                //цвета
                val colorsArrayId = getResourceId(R.styleable.PieChartView_pieColors, 0)
                if (colorsArrayId != 0) {
                    val typedArray = resources.obtainTypedArray(colorsArrayId)
                    pieColors = IntArray(typedArray.length()).apply {
                        for (i in 0 until typedArray.length()) {
                            set(i, typedArray.getColor(i, Color.BLACK))
                        }
                    }
                    typedArray.recycle()
                    //мы не можем иметь в массиве цветов одновременно 2 одинаковых цвета
                    val uniqueColors = pieColors.toSet()
                    require(uniqueColors.size == pieColors.size) { OtherProperties.PIE_CHART_EX_MSG_DUPL_COLOR }
                }

                gapSize = getDimension(R.styleable.PieChartView_gapSize, 2f)
                activeColorOffset = getDimension(R.styleable.PieChartView_activeColorOffset, 50f).toInt()
                centerRadius = getDimension(R.styleable.PieChartView_centerRadius, 150f)
            } finally {
                recycle() //При вызове obtainStyledAttributes() система выделяет временный буфер в памяти. recycle() освобождает эту память.
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pathDirty = true
    }

    private fun updateClipPath() {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 2f - 20f
        clipPath.run {
            reset()
            addCircle(centerX, centerY, radius, Path.Direction.CW)
            addCircle(centerX, centerY, centerRadius, Path.Direction.CCW)
            fillType = Path.FillType.EVEN_ODD
        }
        pathDirty = false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (pieData.isEmpty()) return

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 2f - 20f //Вычитаем 20 пикселей - отступ от краев
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        // Обновляем path только при необходимости чтобы при каждом onDraw не вызывался
        if (pathDirty) {
            updateClipPath()
        }
        canvas.save()
        canvas.clipPath(clipPath)

        var startAngle = -90f // Начинаем с 12 часов

        //угловой размер промежутка в градусах
        //gapSize - линейный размер промежутка в пикселях (из атрибутов)
        //Делим на радиус, чтобы перевести пиксели в радианы
        //Умножаем на 180/пи для перевода радиан в градусы
        val gapAngle = gapSize / radius * (180f / Math.PI).toFloat()

        pieData.entries.forEachIndexed { index, (key, value) ->
            //угол сектора
            //Умножаем значение в процентах на 3.6, потому что в 1% у нас 3.6 градусов
            //Вычитаем gapAngle, чтобы оставить место для промежутка
            val sweepAngle = value * 3.6f - gapAngle

            val colorIndex = if (index < pieColors.size) {
                index
            } else {
                //Когда все цвета исчерпаны, начинаем брать не как раньше по порядку, а как бы через 1 цвет - иначе получим 2 сектора одинакового цвета рядом
                (index - pieColors.size) % (pieColors.size - 1) + 1
            }

            val color = pieColors[colorIndex % pieColors.size]

            // Рисуем сектор
            paint.color = if (activeItem == key) adjustColor(color, activeColorOffset) else color
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)

            // Рисуем текст в центре сектора
            //Угол середины текущего сектора
            val midAngle = startAngle + sweepAngle / 2

            //Берем центр нашей вьюшки
            //смещаем текст немного на 60% от радиуса, чтобы текст был не на краю, а ближе к центру
            //cos/sin от midAngle для получения координат на окружности
            val textX = centerX + (radius * 0.6f) * cos(Math.toRadians(midAngle.toDouble())).toFloat()
            val textY = centerY + (radius * 0.6f) * sin(Math.toRadians(midAngle.toDouble())).toFloat()
            canvas.drawText("${value.toInt()}%", textX, textY, textPaint)

            //обновляем начальный угол для следующего сектора
            startAngle += sweepAngle + gapAngle
        }
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y

                val centerX = width / 2f
                val centerY = height / 2f
                val radius = minOf(width, height) / 2f - 20f

                // Считаем расстояние от центра до точки касания
                val dx = x - centerX
                val dy = y - centerY
                val dist = sqrt(dx * dx + dy * dy)

                // Если точка в "дыре" в центре, не выбираем сектор
                if (dist < centerRadius) {
                    if (activeItem != null) {
                        activeItem = null
                        invalidate()
                    }
                    return true
                }

                if (dist > radius) {
                    // Точка вне круга диаграммы
                    if (activeItem != null) {
                        activeItem = null
                        invalidate()
                    }
                    return true
                }

                // Найдем угол касания относительно центра
                // atan2 возвращает угол в радианах от -PI до PI, 0 — по оси X
                var touchAngle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()

                // Переводим угол так, чтобы 0 градусов был сверху (12 часов)
                touchAngle += 90f
                if (touchAngle < 0) touchAngle += 360f

                // Идем по секторам, чтобы определить, в какой угол попал клик
                var startAngle = 0f

                pieData.entries.forEach { (key, value) ->
                    val sweepAngle = value * 3.6f

                    val sectorStart = startAngle
                    val sectorEnd = startAngle + sweepAngle

                    if (touchAngle >= sectorStart && touchAngle < sectorEnd) {
                        if (activeItem != key) {
                            activeItem = key
                            invalidate()
                        }
                        return true
                    }
                    startAngle += sweepAngle
                }
                activeItem = null
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun adjustColor(color: Int, offset: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        // Увеличиваем яркость, но не выходим за пределы 0-1
        hsv[2] = minOf(1f, hsv[2] + offset / 255f)
        return Color.HSVToColor(hsv)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable(OtherProperties.PIE_CHART_PARCELABLE_SUPER_STATE, superState)
            putInt(OtherProperties.PIE_CHART_PARCELABLE_ACTIVE_ITEM, activeItem ?: -1)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(OtherProperties.PIE_CHART_PARCELABLE_SUPER_STATE))
            val savedActiveItem = state.getInt(OtherProperties.PIE_CHART_PARCELABLE_ACTIVE_ITEM, -1)
            activeItem = if (savedActiveItem != -1) savedActiveItem else null
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}