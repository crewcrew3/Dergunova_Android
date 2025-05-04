package ru.itis.application7.feature.listcontent

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.itis.application7.R
import ru.itis.application7.core.ui.BaseScreen
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.application7.core.domain.model.CurrentWeatherModel
import ru.itis.application7.core.domain.model.MainDataModel
import ru.itis.application7.core.domain.model.WeatherDataModel
import ru.itis.application7.core.domain.model.WindDataModel
import ru.itis.application7.core.ui.components.InputFieldCustom
import ru.itis.application7.core.ui.theme.Application7Theme
import ru.itis.application7.core.ui.theme.CustomDimensions
import ru.itis.application7.core.ui.theme.CustomStyles

@Composable
fun ListContentScreen(
    onItemClick: (CurrentWeatherModel) -> Unit,
    previewList: List<CurrentWeatherModel> = listOf(), //для превьюшки
    viewModel: ListContentViewModel = hiltViewModel(),
) {

    // Подписка на список погоды (StateFlow)
    val list by viewModel.weatherInfoFlow.collectAsState(initial = emptyList())

    val isInputError by viewModel.errorInputFlow.collectAsState(initial = false)

    // Подписка на ошибки для отображения Toast (SharedFlow)
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    val httpErrorList by viewModel.errorHttpFlow.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(httpErrorList) {
        if (httpErrorList.isNotEmpty()) {
            showDialog = true
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    httpErrorList.getOrNull(0)
                        ?: stringResource(R.string.alert_title_unknown_error_code)
                )
            },
            text = {
                Text(
                    httpErrorList.getOrNull(1)
                        ?: stringResource(R.string.alert_title_unknown_error_message)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text(stringResource(R.string.alert_button_ok))
                }
            }
        )
    }

    BaseScreen(
        isTopBar = true,
        topBarHeader = stringResource(R.string.list_content_header),
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .padding(top = CustomDimensions.paddingTopFromTopBar)
            ) {
                item {
                    InputFieldCustom(
                        labelText = stringResource(R.string.label_input_field),
                        placeholderText = stringResource(R.string.hint_input_field),
                        onSearch = { citiesNames ->
                            viewModel.getCurrentWeatherByCitiesNames(citiesNames)
                        },
                        isError = isInputError,
                        modifier = Modifier
                            .padding(CustomDimensions.paddingCardInList)
                    )
                }

                items(list) { item ->
                    ListItem(
                        item = item,
                        onClick = { onItemClick(item)})
                }
            }
        }
    }
}

@Composable
fun ListItem(
    item: CurrentWeatherModel,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(CustomDimensions.paddingCardInList)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(CustomDimensions.paddingCardContent)
        ) {
            Text(
                text = item.cityName,
                style = CustomStyles.h1Normal,
            )
            item.weather.forEach { itemWeather ->
                Text(
                    text = itemWeather.main
                )
            }
            Text(
                text = item.main.temp.toString()
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun ListContentScreenPreview() {
    Application7Theme {
        ListContentScreen(
            {},
            previewList = listOf(
                CurrentWeatherModel(
                    cityName = "Kazan",
                    weather = listOf(
                        WeatherDataModel(
                            main = "Rain",
                            description = "Aoaoa"
                        )
                    ),
                    main = MainDataModel(
                        temp = 35f,
                        feelsLike = 40f,
                        pressure = 123,
                        humidity = 5,
                    ),
                    wind = WindDataModel(
                        speed = 10f
                    ),
                ),
                CurrentWeatherModel(
                    cityName = "Kazan",
                    weather = listOf(
                        WeatherDataModel(
                            main = "Rain",
                            description = "Aoaoa"
                        )
                    ),
                    main = MainDataModel(
                        temp = 35f,
                        feelsLike = 40f,
                        pressure = 123,
                        humidity = 5,
                    ),
                    wind = WindDataModel(
                        speed = 10f
                    ),
                ),CurrentWeatherModel(
                    cityName = "Kazan",
                    weather = listOf(
                        WeatherDataModel(
                            main = "Rain",
                            description = "Aoaoa"
                        )
                    ),
                    main = MainDataModel(
                        temp = 35f,
                        feelsLike = 40f,
                        pressure = 123,
                        humidity = 5,
                    ),
                    wind = WindDataModel(
                        speed = 10f
                    ),
                ),
                CurrentWeatherModel(
                    cityName = "Kazan",
                    weather = listOf(
                        WeatherDataModel(
                            main = "Rain",
                            description = "Aoaoa"
                        )
                    ),
                    main = MainDataModel(
                        temp = 35f,
                        feelsLike = 40f,
                        pressure = 123,
                        humidity = 5,
                    ),
                    wind = WindDataModel(
                        speed = 10f
                    ),
                ),
                CurrentWeatherModel(
                    cityName = "Kazan",
                    weather = listOf(
                        WeatherDataModel(
                            main = "Rain",
                            description = "Aoaoa"
                        )
                    ),
                    main = MainDataModel(
                        temp = 35f,
                        feelsLike = 40f,
                        pressure = 123,
                        humidity = 5,
                    ),
                    wind = WindDataModel(
                        speed = 10f
                    ),
                ),

            )
        )
    }
}