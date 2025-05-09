package ru.itis.application7.core.feature.listcontent.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.itis.application7.core.R
import ru.itis.application7.core.ui.BaseScreen
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.application7.core.domain.model.CurrentWeatherModel
import ru.itis.application7.core.domain.model.MainDataModel
import ru.itis.application7.core.domain.model.WeatherDataModel
import ru.itis.application7.core.domain.model.WindDataModel
import ru.itis.application7.core.feature.listcontent.state.ListContentScreenEvent
import ru.itis.application7.core.feature.listcontent.state.ListContentScreenState
import ru.itis.application7.core.ui.components.InputFieldCustom
import ru.itis.application7.core.ui.components.ShimmerCustom
import ru.itis.application7.core.ui.theme.Application7Theme
import ru.itis.application7.core.ui.theme.CustomDimensions
import ru.itis.application7.core.ui.theme.CustomStyles

@Composable
fun ListContentScreen(
    onItemClick: (CurrentWeatherModel) -> Unit,
    toAuthorizationScreen: () -> Unit,
    previewList: List<CurrentWeatherModel> = listOf(), //для превьюшки
    viewModel: ListContentViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    val pageState by viewModel.pageState.collectAsState(initial = ListContentScreenState.Initial)
    when (pageState) {
        is ListContentScreenState.Error -> {
            Toast.makeText(context, (pageState as ListContentScreenState.Error).message, Toast.LENGTH_SHORT).show()
        }
        is ListContentScreenState.ErrorHttp -> {
            val httpErrorList = (pageState as ListContentScreenState.ErrorHttp).message
            AlertDialog(
                onDismissRequest = { viewModel.reduce(ListContentScreenEvent.OnAlertDialogClosed) },
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
                        viewModel.reduce(ListContentScreenEvent.OnAlertDialogClosed)
                    }) {
                        Text(stringResource(R.string.alert_button_ok))
                    }
                }
            )
        }
        else -> Unit
    }

    BaseScreen(
        isTopBar = true,
        topBarHeader = stringResource(R.string.list_content_header),
        isFloatingActionButton = true,
        onFloatingActBtnClick = {
            viewModel.reduce(event = ListContentScreenEvent.OnLogOut)
            toAuthorizationScreen()
        },
        floatingBtnText = stringResource(R.string.log_out_action)
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
                        onDone = { citiesNames ->
                            viewModel.reduce(event = ListContentScreenEvent.OnSearchQueryChanged(query = citiesNames))
                        },
                        isError = (pageState is ListContentScreenState.ErrorInput),
                        modifier = Modifier
                            .padding(CustomDimensions.paddingCardInList)
                    )
                }
                when (pageState) {
                    is ListContentScreenState.Loading -> {
                        items(viewModel.numberOfLoadingItems) {
                            ShimmerCustom(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(CustomDimensions.cardListContentShimmerHeight)
                                    .padding(CustomDimensions.paddingCardInList)
                            )
                        }
                    }
                    is ListContentScreenState.SearchResult -> {

                        val toastMessage = (pageState as ListContentScreenState.SearchResult).result.source
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()

                        val list = (pageState as ListContentScreenState.SearchResult).result.listWeatherModel
                        items(list) { item ->
                            ListItem(
                                item = item,
                                onClick = { onItemClick(item) })
                        }
                    }
                    else -> {}
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
            {}, {},
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