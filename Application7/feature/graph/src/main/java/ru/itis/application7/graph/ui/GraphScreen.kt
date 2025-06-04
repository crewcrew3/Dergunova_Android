package ru.itis.application7.graph.ui

import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.application7.core.ui.BaseScreen
import ru.itis.application7.core.ui.components.InputFieldCustom
import ru.itis.application7.core.R
import ru.itis.application7.core.ui.components.PrimaryButtonCustom
import ru.itis.application7.core.ui.theme.CustomDimensions
import ru.itis.application7.graph.state.GraphScreenEffect
import ru.itis.application7.graph.state.GraphScreenEvent
import ru.itis.application7.graph.state.GraphScreenState
import kotlin.math.ceil

@Composable
fun GraphScreen(
    viewModel: GraphViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is GraphScreenEffect.Error -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val pageState by viewModel.pageState.collectAsState(initial = GraphScreenState.Initial)

    var numberOfPoints by remember { mutableStateOf("") }
    var pointsValues by remember { mutableStateOf("") }

    BaseScreen { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                InputFieldCustom(
                    labelText = stringResource(R.string.label_number_of_points),
                    onValueChange = {numberOfPoints = it},
                    isError = (pageState is GraphScreenState.ErrorNumberPointInput),
                    modifier = Modifier
                        .padding(top = CustomDimensions.basePadding)
                )

                InputFieldCustom(
                    labelText = stringResource(R.string.label_points_value),
                    onValueChange = {pointsValues = it},
                    isError = (pageState is GraphScreenState.ErrorPointsValuesInput),
                    modifier = Modifier
                        .padding(top = CustomDimensions.basePadding)
                )

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButtonCustom(
                    onBtnText = stringResource(R.string.draw_graph_btn_text),
                    onClick = {
                        viewModel.reduce(
                            GraphScreenEvent.DrawGraph(
                                numberOfPoints = numberOfPoints,
                                pointsValues = pointsValues
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                (pageState as? GraphScreenState.Success)?.let { state ->
                    GraphPlotter(
                        points = state.result,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GraphPlotter(
    points: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = lineColor.copy(alpha = 0.3f)
) {

    Canvas(modifier = modifier) { //область для рисования

        val canvasWidth = size.width
        val canvasHeight = size.height
        val maxValue = points.maxOrNull() ?: 1f //берем макс точку из всех ранее введенных точек
        val minValue = points.minOrNull() ?: 0f //берем минимальную точку
        val valueRange = maxValue - minValue

        //в Canvas (0, 0) - левый верхний угол. Ось X направлена вправо, Y - вниз
        // Ось X
        drawLine(
            color = Color.Gray,
            start = Offset(0f, canvasHeight), // Начинается в левом нижнем углу
            end = Offset(canvasWidth, canvasHeight), // Заканчивается в правом нижнем
            strokeWidth = 2.dp.toPx()
        )
        // Ось Y
        drawLine(
            color = Color.Gray,
            start = Offset(0f, 0f), // Левый верхний угол
            end = Offset(0f, canvasHeight), // Левый нижний угол
            strokeWidth = 2.dp.toPx()
        )

        //посчитаем координаты точек
        // считаем шаг по оси X чтобы равномерно распределить точки по ширине
        val xStep = canvasWidth / (points.size - 1).coerceAtLeast(1)
        val coordinates =
            points.mapIndexed { index, value -> //проводим операции над самим значением коллекции и его индексом
                Offset(
                    x = index * xStep, //равномерно распределяем точки по оси X
                    y = canvasHeight - ((value - minValue) / valueRange.coerceAtLeast(0.1f)) * canvasHeight
                )
            }

        // Подписи значений по оси X
        points.forEachIndexed { index, _ ->
            val xPos = index * xStep
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "${index + 1}",
                    xPos,
                    canvasHeight + 20.dp.toPx(), //смещаем текст ниже оси X
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 12.sp.toPx()
                        textAlign = Paint.Align.CENTER
                    }
                )
            }
        }

        // Подписи значений по оси Y
        val yLabelValues = generateYLabels(minValue, maxValue)
        yLabelValues.forEach { value ->
            val yPos = canvasHeight - ((value - minValue) / valueRange.coerceAtLeast(0.1f)) * canvasHeight
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    value.toInt().toString(),
                    16.dp.toPx() * (-1),
                    yPos + 4.dp.toPx(),
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 12.sp.toPx()
                        textAlign = Paint.Align.LEFT
                    }
                )
            }
        }

        // Пунктирные линии от точек к осям
        coordinates.forEach { point ->
            // Вертикальная линия к оси X
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(point.x, point.y),
                end = Offset(point.x, canvasHeight),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
            )

            // Горизонтальная линия к оси Y
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(0f, point.y),
                end = Offset(point.x, point.y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
            )
        }

        //создаем объект пути - область для заливки
        val path = Path().apply {
            moveTo(0f, canvasHeight)
            coordinates.forEach { point -> lineTo(point.x, point.y) } //от точки до точки проводим линию
            lineTo(canvasWidth, canvasHeight)
            close() //замыкаем область под графиком
        }
        //заливаем область градиентом
        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(fillColor, fillColor.copy(alpha = 0f)),
                startY = coordinates.minByOrNull { it.y }?.y ?: 0f, //чем меньше y - тем выше точка в канвасе
                endY = canvasHeight
            )
        )

        // Рисуем сам график
        if (coordinates.size > 1) {
            for (i in 0 until coordinates.size - 1) {
                drawLine(
                    color = lineColor,
                    start = coordinates[i],
                    end = coordinates[i + 1],
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round //закругляем концы у линии
                )
            }
        }

        // Рисуем точки
        coordinates.forEach { point ->
            drawCircle(
                color = lineColor,
                radius = 5.dp.toPx(),
                center = point
            )
        }
    }
}

private fun generateYLabels(min: Float, max: Float): List<Float> {
    val range = max - min
    val step = when {
        range > 100 -> 20f
        range > 50 -> 10f
        range > 20 -> 5f
        range > 10 -> 2f
        else -> 1f
    }

    val labels = mutableListOf<Float>()
    var current = ceil(min / step) * step
    while (current <= max) {
        labels.add(current)
        current += step
    }
    return labels
}