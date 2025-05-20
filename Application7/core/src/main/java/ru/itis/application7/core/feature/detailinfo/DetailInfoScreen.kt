package ru.itis.application7.core.feature.detailinfo

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.application7.core.R
import ru.itis.application7.core.domain.model.CurrentWeatherModel
import ru.itis.application7.core.domain.model.MainDataModel
import ru.itis.application7.core.domain.model.WeatherDataModel
import ru.itis.application7.core.domain.model.WindDataModel
import ru.itis.application7.core.feature.detailinfo.state.DetailInfoScreenEvent
import ru.itis.application7.core.feature.detailinfo.state.DetailInfoScreenState
import ru.itis.application7.core.feature.listcontent.state.ListContentScreenState
import ru.itis.application7.core.ui.BaseScreen
import ru.itis.application7.core.ui.components.ShimmerCustom
import ru.itis.application7.core.ui.theme.Application7Theme
import ru.itis.application7.core.ui.theme.CustomDimensions
import ru.itis.application7.core.ui.theme.CustomStyles

@Composable
fun DetailInfoScreen(
    cityName: String,
    previewWeatherItem: CurrentWeatherModel = CurrentWeatherModel.EMPTY, //для превьюшки
    viewModel: DetailInfoViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val pageState by viewModel.pageState.collectAsState(initial = ListContentScreenState.Initial)
    when (pageState) {
        is DetailInfoScreenState.Error -> {
            Toast.makeText(context, (pageState as DetailInfoScreenState.Error).message, Toast.LENGTH_SHORT).show()
        }
        is DetailInfoScreenState.ErrorHttp -> {
            val httpErrorList = (pageState as DetailInfoScreenState.ErrorHttp).message
            AlertDialog(
                onDismissRequest = { viewModel.reduce(DetailInfoScreenEvent.OnAlertDialogClosed) },
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
                        viewModel.reduce(DetailInfoScreenEvent.OnAlertDialogClosed)
                    }) {
                        Text(stringResource(R.string.alert_button_ok))
                    }
                }
            )
        }
        else -> Unit
    }

    LaunchedEffect(Unit) {
        viewModel.reduce(event = DetailInfoScreenEvent.OnScreenInit(cityName))
    }

    BaseScreen { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                when (pageState) {
                    is DetailInfoScreenState.Loading -> {
                        ShimmerCustom(
                            modifier = Modifier
                                .padding(top = 40.dp, start = 28.dp)
                                .fillMaxWidth()
                                .height(CustomDimensions.detailInfoCityNameShimmerHeight)
                        )
                    }
                    is DetailInfoScreenState.Result -> {
                        val weatherItem = (pageState as DetailInfoScreenState.Result).result
                        Text(
                            text = weatherItem.cityName,
                            style = CustomStyles.detailInfoCityNameHeader,
                            modifier = Modifier
                                .padding(top = 40.dp, start = 28.dp)
                        )
                    }
                }
            }
            item {
                LazyRow(
                    modifier = Modifier
                        .padding(top = 48.dp)
                        .fillMaxWidth()
                ) {
                    when (pageState) {
                        is DetailInfoScreenState.Loading -> {
                            items(viewModel.numberOfLoadingRowItems) {
                                ShimmerCustom(
                                    modifier = Modifier
                                        .padding(CustomDimensions.paddingCardInList)
                                        .width(CustomDimensions.listRowItemWidth)
                                )
                            }
                        }

                        is DetailInfoScreenState.Result -> {
                            val weatherItem = (pageState as DetailInfoScreenState.Result).result
                            items(weatherItem.weather) { item ->
                                ListRowItem(item = item)
                            }
                        }
                    }
                }
            }
            item {
                when (pageState) {
                    is DetailInfoScreenState.Loading -> {
                        ShimmerCustom(
                            modifier = Modifier
                                .padding(CustomDimensions.paddingCardInList)
                                .height(CustomDimensions.cardDetailInfoMainShimmerHeight)
                                .fillMaxWidth()
                        )
                    }

                   is DetailInfoScreenState.Result -> {
                       val weatherItem = (pageState as DetailInfoScreenState.Result).result
                       Card(
                           colors = CardDefaults.cardColors(
                               containerColor = MaterialTheme.colorScheme.surface,
                               contentColor = MaterialTheme.colorScheme.onSurface,
                           ),
                           modifier = Modifier
                               .padding(CustomDimensions.paddingCardInList)
                               .fillMaxWidth()
                       ) {
                           Column(
                               modifier = Modifier
                                   .padding(CustomDimensions.paddingCardContent)
                           ) {
                               Text( //пробелы в конце у строк игнорируются, поэтому таким образом я добавляю его здесь
                                   text = "${stringResource(R.string.temp_prefix)} " +
                                           weatherItem.main.temp.toString() +
                                           " ${stringResource(R.string.temp_postfix)}",
                                   style = CustomStyles.p2,
                                   modifier = Modifier
                                       .padding(CustomDimensions.paddingCardContent)
                               )
                               Text(
                                   text = "${stringResource(R.string.feels_like_prefix)} " +
                                           weatherItem.main.feelsLike.toString() +
                                           " ${stringResource(R.string.temp_postfix)}",
                                   style = CustomStyles.p2,
                                   modifier = Modifier
                                       .padding(CustomDimensions.paddingCardContent)
                               )
                               Text(
                                   text = "${stringResource(R.string.pressure_prefix)} " +
                                           weatherItem.main.pressure.toString() +
                                           " ${stringResource(R.string.pressure_postfix)}",
                                   style = CustomStyles.p2,
                                   modifier = Modifier
                                       .padding(CustomDimensions.paddingCardContent)
                               )
                               Text(
                                   text = "${stringResource(R.string.humidity_prefix)} " +
                                           weatherItem.main.humidity.toString() +
                                           " ${stringResource(R.string.humidity_postfix)}",
                                   style = CustomStyles.p2,
                                   modifier = Modifier
                                       .padding(CustomDimensions.paddingCardContent)
                               )
                           }
                       }
                   }
                }
            }

            item {
                when (pageState) {
                    is DetailInfoScreenState.Loading -> {
                        ShimmerCustom(
                            modifier = Modifier
                                .padding(CustomDimensions.paddingCardInList)
                                .height(CustomDimensions.cardDetailInfoWindShimmerHeight)
                                .fillMaxWidth()
                        )
                    }

                    is DetailInfoScreenState.Result -> {
                        val weatherItem = (pageState as DetailInfoScreenState.Result).result
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                            ),
                            modifier = Modifier
                                .padding(CustomDimensions.paddingCardInList)
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(CustomDimensions.paddingCardContent)
                            ) {
                                Text(
                                    text = "${stringResource(R.string.wind_speed_prefix)} " +
                                            weatherItem.wind.speed.toString() +
                                            " ${stringResource(R.string.wind_speed_postfix)}",
                                    style = CustomStyles.p2,
                                    modifier = Modifier
                                        .padding(CustomDimensions.paddingCardContent)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListRowItem(
    item: WeatherDataModel,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier
            .padding(CustomDimensions.paddingCardInList)
            .width(CustomDimensions.listRowItemWidth)
    ) {
        Column(
            modifier = Modifier
                .padding(CustomDimensions.paddingCardContent)
        ) {
            Text(
                text = item.main,
                style = CustomStyles.p3,
                modifier = Modifier
                    .padding(bottom = CustomDimensions.paddingMiniCardContent)
            )
            Text(
                text = item.description,
                style = CustomStyles.p3
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun DetailInfoScreenPreview() {
    Application7Theme {
        DetailInfoScreen(
            cityName = "",
            previewWeatherItem =
            CurrentWeatherModel(
                cityName = "Kazan",
                weather = listOf(
                    WeatherDataModel(
                        main = "Rain",
                        description = "wet"
                    ),
                    WeatherDataModel(
                        main = "Claudi",
                        description = "puff puff"
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
    }
}